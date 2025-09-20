package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Videogame {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;
	private LocalDate releaseDate;
	private String description;
	private String genre;
	private String platform;
	
	@Column(nullable = true)
	private String imageFileName;

	// Molti videogiochi possono avere più sviluppatori
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Developer> developers;

	// Un videogioco ha un publisher (che è sempre un Developer)
	@ManyToOne
	@JoinColumn(name="publisher_id",nullable = true)
	private Developer publisher;
	

	@OneToMany(mappedBy = "videogame")
	private List<Review> reviews;
	
	public String getPicPath(){
		if(imageFileName != null) return "/upload/images/videogames_pics/" + this.getId() + "/"
				+this.getImageFileName();
		return "/images/default_videogame_pic.png";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(List<Developer> developers) {
		this.developers = developers;
	}

	public Developer getPublisher() {
		return publisher;
	}

	public void setPublisher(Developer publisher) {
		this.publisher = publisher;
	}
}
