package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Developer;
import it.uniroma3.siw.repository.DeveloperRepository;

@Service
public class DeveloperService {
	
	@Autowired
	private DeveloperRepository developerRepository;
	
	public Developer getDeveloperById(Long id) {
		return this.developerRepository.findById(id).get();
	}
	
	public List<Developer> getDeveloperByName(String name){
		return this.developerRepository.findByName(name);
	}
	
	public List<Developer> getDeveloperByNationality(String nationality){
		return this.developerRepository.findByNationality(nationality);
	}
	
	public Developer saveDeveloper(Developer developer) {
		return this.developerRepository.save(developer);
	}
	
	public void deleteDeveloper(Developer developer) {
		this.developerRepository.delete(developer);
	}
	
	public boolean alreadyExistsByNameAndNationalityAndDateOfBirth(Developer developer) {
		return this.developerRepository.existsByNameAndNationalityAndDateOfBirth(developer.getName(), developer.getNationality() , developer.getDateOfBirth());
	}

	public List<Developer> getAllDevelopers() {
		List<Developer> result = new ArrayList<>();
		
		for(Developer d : this.developerRepository.findAll()) {
			result.add(d);
		}
		return result;
	}
}
