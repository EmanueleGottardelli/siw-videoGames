package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Developer;

public interface DeveloperRepository extends CrudRepository<Developer,Long>{
	
	public List<Developer> findByName(String name);
	public List<Developer> findByNationality(String nationality);
	
	public boolean existsByNameAndNationalityAndDateOfBirth(String name, String nationality,LocalDate dateOfBirth);
}
