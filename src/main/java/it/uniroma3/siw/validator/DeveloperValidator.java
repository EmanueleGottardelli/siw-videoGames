package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Developer;
import it.uniroma3.siw.service.DeveloperService;

@Component
public class DeveloperValidator implements Validator{
	
	@Autowired
	private DeveloperService developerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Developer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Developer developer = (Developer) target;
		
		if(developerService.alreadyExistsByNameAndNationalityAndDateOfBirth(developer)) {
			errors.reject("errors.alreadyExistsByNameAndNationalityAndDateOfBirth");
		}
	}
}
