package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.util.FileUploadUtil;
import it.uniroma3.siw.model.Developer;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.Videogame;
import it.uniroma3.siw.service.DeveloperService;
import it.uniroma3.siw.service.VideogameService;
import it.uniroma3.siw.validator.VideogameValidator;
import jakarta.validation.Valid;

@Controller
public class VideogameController {
	
	@Autowired
	private VideogameService videogameService;
	
	@Autowired
	private VideogameValidator videogameValidator;
	
	@Autowired
	private DeveloperService developerService;
	
	@GetMapping("/searchVideogames")
	public String searchVideogames(@RequestParam(required = false) String title,
								   @RequestParam(required = false) String genre,
								   @RequestParam(required = false) String platform,
								   @RequestParam(required = false) LocalDate releaseDate,
								   Model model) {
		List<Videogame> result = new ArrayList<>();
		
		if(title != null && !title.isBlank()) {
			result.addAll(videogameService.getVideoGameByTitle(title));
		}
		if(genre != null && !genre.isBlank()) {
			result.addAll(videogameService.getVideoGamesByGenre(genre));
		}
		if(platform!=null && !platform.isBlank()) {
			result.addAll(videogameService.getVideogamesByPlatform(platform));
		}
		if(releaseDate != null) {
			result.addAll(videogameService.getVideoGamesByReleaseDate(releaseDate));
		}
		
		model.addAttribute("result", result);
		
		return "searchVideogamesResult";
	}
	
	@GetMapping(value="/admin/formUpdateVideogame/{id}")
	public String formUpdateVideogame(@PathVariable("id") Long id, Model model) {
		model.addAttribute("videogame", videogameService.getVideogameById(id));
		model.addAttribute("allDevelopers", developerService.getAllDevelopers());
		return "admin/formUpdateVideogame";
	}
	
	@GetMapping(value="/admin/indexVideogame")
	public String indexVideogame() {
		return "admin/indexVideogame";
	}
	
	@PostMapping("/admin/updateVideogame/{id}")
	public String updateVideogame(@PathVariable("id") Long id,
								  @ModelAttribute("videogame") Videogame updatedVideogame,
								  @RequestParam(required = false) List<Long> newDevelopers,
								  @RequestParam(required = false) Long publisherId) {
		
		Videogame videogame = videogameService.getVideogameById(id);
		
		if (updatedVideogame.getTitle() != null && !updatedVideogame.getTitle().isBlank()) {
	        videogame.setTitle(updatedVideogame.getTitle());
	    }
	    if (updatedVideogame.getDescription() != null && !updatedVideogame.getDescription().isBlank()) {
	        videogame.setDescription(updatedVideogame.getDescription());
	    }
	    if (updatedVideogame.getReleaseDate() != null) {
	        videogame.setReleaseDate(updatedVideogame.getReleaseDate());
	    }
	    if (updatedVideogame.getGenre() != null && !updatedVideogame.getGenre().isBlank()) {
	        videogame.setGenre(updatedVideogame.getGenre());
	    }
	    if (updatedVideogame.getPlatform() != null && !updatedVideogame.getPlatform().isBlank()) {
	    	videogame.setPlatform(updatedVideogame.getPlatform());
	    }
	    
	    // sostituisco la lista dei developer
	    List<Developer> newDevs = new ArrayList<>();
	    if(newDevelopers != null) {
	    	for(Long dIv : newDevelopers) {
	    		newDevs.add(developerService.getDeveloperById(dIv));
	    	}
	    }
	    videogame.setDevelopers(newDevs);
	    
	    if(publisherId != null) {
	    	Developer newPub = developerService.getDeveloperById(publisherId);
	    	videogame.setPublisher(newPub);
	    }
	    
	    videogameService.saveVideogame(videogame);
	    
	    return "redirect:/manageVideogames";
	}
	
	@PostMapping("/admin/deleteVideogame/{id}")
	public String deleteVideogame(@PathVariable("id") Long id) {
		videogameService.deleteVideogame(videogameService.getVideogameById(id));
		return "redirect:/admin/manageVideogames";
	}
	
	@GetMapping("/admin/manageVideogames")
	public String operazioniVideogame(Model model) {
		model.addAttribute("videogames", videogameService.getAllVideoGame());
		return "admin/manageVideogames";
	}
	
	@GetMapping("/admin/formNewVideogame")
	public String formNewVideogame(Model model) {
		Videogame videogame = new Videogame();
		model.addAttribute("videogame", videogame);
		model.addAttribute("developers", developerService.getAllDevelopers());
		return "admin/formNewVideogame";
	}

	@PostMapping("/admin/videogame")
	public String newVideogame(@Valid @ModelAttribute("videogame") Videogame videogame, BindingResult bindingResult,
			Model model) {
		videogameValidator.validate(videogame, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.videogameService.saveVideogame(videogame);
			model.addAttribute("videogame", videogame);
			return "redirect:/videogame";
		} else {
			model.addAttribute("messaggioErrore", "Questo gioco è già presente nel catalogo, inseriscine un altro:");
			return "admin/formNewVideogame";
		}

	}
	
	@GetMapping("/videogame")
	public String getAllVideogames(Model model) {
		model.addAttribute("videogames", this.videogameService.getAllVideoGame());
		return "videogames";
	}
	
	@GetMapping("/videogame/{id}")
	public String getVideogame(@PathVariable("id") Long id, Model model) {
		model.addAttribute("videogame", videogameService.getVideogameById(id));
		model.addAttribute("newReview", new Review());
		return "videogame";
	}
	
	@PostMapping("/admin/saveVideogameImage/{id}")
    public String saveVideogameImage(@PathVariable("id") Long id,
                                 @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Videogame videogame = videogameService.getVideogameById(id);
//      if(videogame == null) return "errors/videogameNotFoundError";

        videogame.setImageFileName(fileName);
        videogameService.saveVideogame(videogame);
        String uploadDir = "src/main/upload/images/videogames_pics/" + videogame.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return "redirect:/videogame/"+ id;
    }
	
	
}
