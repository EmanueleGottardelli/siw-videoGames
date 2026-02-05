package it.uniroma3.siw.restController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Videogame;
import it.uniroma3.siw.service.VideogameService;

@RestController
public class VideogameRestController {
	@Autowired
	private VideogameService videogameService;

	@GetMapping(value = "/rest/videogame/{id}")
	public Videogame getVideogame(@PathVariable("id") Long id) {
		return this.videogameService.getVideogameById(id);
	}

	@GetMapping(value = "/rest/videogame")
	public List<Videogame> getAllVideogames() {
		return this.videogameService.getAllVideoGame();
	}
	
	@GetMapping(value = "/rest/searchVideogames")
	public List<Videogame> searchVideogames(@RequestParam(required = false) String title,
			@RequestParam(required = false) String genre, @RequestParam(required = false) String platform,
			@RequestParam(required = false) LocalDate releaseDate) {
		List<Videogame> result = new ArrayList<>();

		if (title != null && !title.isBlank()) {
			result.addAll(videogameService.getVideoGameByTitle(title));
		}
		if (genre != null && !genre.isBlank()) {
			result.addAll(videogameService.getVideoGamesByGenre(genre));
		}
		if (platform != null && !platform.isBlank()) {
			result.addAll(videogameService.getVideogamesByPlatform(platform));
		}
		if (releaseDate != null) {
			result.addAll(videogameService.getVideoGamesByReleaseDate(releaseDate));
		}

		return result;
	}

}
