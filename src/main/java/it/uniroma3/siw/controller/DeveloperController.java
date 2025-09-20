package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Developer;
import it.uniroma3.siw.model.Videogame;
import it.uniroma3.siw.service.DeveloperService;
import it.uniroma3.siw.service.VideogameService;
import it.uniroma3.siw.validator.DeveloperValidator;
import jakarta.validation.Valid;

@Controller
public class DeveloperController {

	@Autowired
	private DeveloperValidator developerValidator;

	@Autowired
	private DeveloperService developerService;

	@Autowired
	private VideogameService videogameService;
	
	@GetMapping("/searchDevelopers")
	public String searchDevelopers(@RequestParam(required = false) String name,
								   @RequestParam(required = false) String nationality,
								   Model model) {
		
		List<Developer> result = new ArrayList<>();
		
		if(name != null && !name.isBlank()) {
			result.addAll(developerService.getDeveloperByName(name));
		}
		if(nationality != null && !name.isBlank()) {
			result.addAll(developerService.getDeveloperByNationality(nationality));
		}
		
		model.addAttribute("result", result);
		
		return "searchDevelopersResult";
	}

	@GetMapping("/admin/formUpdateDeveloper/{id}")
	public String formUpdateDeveloper(@PathVariable("id") Long id, Model model) {
		model.addAttribute("developer", developerService.getDeveloperById(id));
		model.addAttribute("videogames", videogameService.getAllVideoGame());
		return "admin/formUpdateDeveloper";
	}

	@PostMapping("/admin/updateDeveloper/{id}")
	public String updateDeveloper(@PathVariable("id") Long id, @ModelAttribute("developer") Developer updatedDeveloper,
			@RequestParam(required = false) List<Long> videogamesDeveloped,
			@RequestParam(required = false) List<Long> videogamesPublished) {

		Developer developer = developerService.getDeveloperById(id);

		if (updatedDeveloper.getName() != null && !updatedDeveloper.getName().isBlank()) {
		    developer.setName(updatedDeveloper.getName());
		}
		if (updatedDeveloper.getNationality() != null && !updatedDeveloper.getNationality().isBlank()) {
		    developer.setNationality(updatedDeveloper.getNationality());
		}

		if (updatedDeveloper.getDateOfBirth() != null) {
			developer.setDateOfBirth(updatedDeveloper.getDateOfBirth());
		}

		// riscrivo completamente la lista dei giochi
		Set<Videogame> developed = new HashSet<>();
		if (videogamesDeveloped != null) {
			for (Long vId : videogamesDeveloped) {
				developed.add(videogameService.getVideogameById(vId));
			}
			
		}
		developer.setGamesDeveloped(developed);
		
		// anche qui riscrivo comletamente la lista 
		Set<Videogame> published = new HashSet<>();
		if (videogamesPublished != null) {
			for (Long vId : videogamesPublished) {
				published.add(videogameService.getVideogameById(vId));
			}
			
		}
		developer.setGamesPublished(published);
		
		developerService.saveDeveloper(developer);

		return "redirect:/developer";
	}

	@GetMapping(value = "/admin/indexDeveloper")
	public String indexDeveloper() {
		return "admin/indexDeveloper";
	}

	@GetMapping("/admin/formNewDeveloper")
	public String formNewDeveloper(Model model) {
		model.addAttribute("developer", new Developer());
		model.addAttribute("videogames", videogameService.getAllVideoGame());
		return "admin/formNewDeveloper";
	}

	@PostMapping("/admin/developer")
	public String newDeveloper(@Valid @ModelAttribute("developer") Developer developer, BindingResult bindingResult,
			Model model) {
		developerValidator.validate(developer, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.developerService.saveDeveloper(developer);
			model.addAttribute("developer", developer);
			return "redirect:/developer";
		} else {
			model.addAttribute("messaggioErrore", "Questo developer esiste gia nel catalogo, inseriscine un altro:");
			return "admin/formNewDeveloper";
		}
	}

	@PostMapping("/admin/deleteDeveloper/{developerId}")
	public String deleteDeveloper(@PathVariable("developerId") Long id) {
		Developer developer = this.developerService.getDeveloperById(id);
		
		if(developer.getGamesDeveloped() != null) {
			for(Videogame v : developer.getGamesDeveloped()) {
				v.getDevelopers().remove(developer);
			}
		}
		
		if(developer.getGamesPublished() != null) {
			for(Videogame v : developer.getGamesPublished()) {
				v.setPublisher(null);
			}
		}
		
		developerService.deleteDeveloper(developer);
		return "redirect:/admin/manageDevelopers";
	}

	@GetMapping("/admin/manageDevelopers")
	public String operazioniDeveloper(Model model) {
		model.addAttribute("developers", developerService.getAllDevelopers());
		return "admin/manageDevelopers";
	}

	@GetMapping("/developer")
	public String getAllDevelopers(Model model) {
		model.addAttribute("developers", this.developerService.getAllDevelopers());
		return "developers";
	}

	@GetMapping("/developer/{id}")
	public String getDeveloper(@PathVariable("id") Long id, Model model) {
		model.addAttribute("developer", this.developerService.getDeveloperById(id));
		return "developer";
	}
}
