package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Videogame;
import it.uniroma3.siw.service.VideogameService;

@Component
public class VideogameValidator implements Validator{
	
	@Autowired
	private VideogameService videogameService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Videogame.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Videogame videogame = (Videogame) target;
		
		if(videogameService.existsByTitleAndReleaseDateAndGenre(videogame)) {
			errors.reject("errors.alreadyExistsByTitleAndReleaseDateandGenre");
		}
		
	}
}
