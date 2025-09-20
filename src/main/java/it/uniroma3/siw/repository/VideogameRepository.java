package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Videogame;

public interface VideogameRepository extends CrudRepository<Videogame,Long>{
	
	public List<Videogame> findByTitle(String title);
	public List<Videogame> findByGenre(String genre);
	public List<Videogame> findByPlatform(String platform);
	public List<Videogame> findByReleaseDate(LocalDate releaseDate);
	
	public List<Videogame> findAllByOrderByReleaseDateDesc();
	
	public boolean existsByTitleAndReleaseDateAndGenre(String title, LocalDate releaseDate,String genre);
}
