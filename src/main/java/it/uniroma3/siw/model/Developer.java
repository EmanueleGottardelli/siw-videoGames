package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Developer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String nationality;
	private LocalDate dateOfBirth;
	
	@ManyToMany
	private Set<Videogame> gamesDeveloped;
	
	@OneToMany(mappedBy="publisher")
	private Set<Videogame> gamesPublished;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Set<Videogame> getGamesDeveloped() {
		return gamesDeveloped;
	}

	public void setGamesDeveloped(Set<Videogame> gamesDeveloped) {
		this.gamesDeveloped = gamesDeveloped;
	}

	public Set<Videogame> getGamesPublished() {
		return gamesPublished;
	}

	public void setGamesPublished(Set<Videogame> gamesPublished) {
		this.gamesPublished = gamesPublished;
	}
}
