package it.uniroma3.siw.restController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Developer;
import it.uniroma3.siw.service.DeveloperService;

@RestController
public class DeveloperRestController {
	@Autowired
	private DeveloperService developerService;

	@GetMapping(value = "/rest/developer/{id}")
	public Developer getVideogame(@PathVariable("id") Long id) {
		return this.developerService.getDeveloperById(id);
	}

	@GetMapping(value = "/rest/developer")
	public List<Developer> getAllDevelopers() {
		return this.developerService.getAllDevelopers();
	}

	@GetMapping(value = "/rest/searchDevelopers")
	public List<Developer> searchVideogames(@RequestParam(required = false) String name,
			@RequestParam(required = false) String nationality) {
		
		List<Developer> result = new ArrayList<>();
		if(name != null && !name.isBlank()) {
			result.addAll(developerService.getDeveloperByName(name));
		}
		if(nationality != null && !nationality.isBlank()) {
			result.addAll(developerService.getDeveloperByNationality(nationality));
		}
		return result;
	}

}
