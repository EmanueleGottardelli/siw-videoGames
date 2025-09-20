package it.uniroma3.siw.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.util.FileUploadUtil;
import it.uniroma3.siw.model.Videogame;
import it.uniroma3.siw.repository.VideogameRepository;

@Service
public class VideogameService {

	@Autowired
	private VideogameRepository videogameRepository;

	public void saveVideogame(Videogame videogame) {
		this.videogameRepository.save(videogame);
	}

	public void deleteVideogame(Videogame videogame) {
		this.videogameRepository.delete(videogame);
	}

	public List<Videogame> getAllVideoGame() {
		List<Videogame> result = new ArrayList<>();

		for (Videogame v : this.videogameRepository.findAll()) {
			result.add(v);
		}

		return result;
	}

	public Videogame getVideogameById(Long id) {
		return this.videogameRepository.findById(id).get();
	}

	public List<Videogame> getVideoGameByTitle(String title) {
		return this.videogameRepository.findByTitle(title);
	}

	public List<Videogame> getVideoGamesByGenre(String genre) {
		return this.videogameRepository.findByGenre(genre);
	}
	
	public List<Videogame> getVideogamesByPlatform(String platform){
		return this.videogameRepository.findByPlatform(platform);
	}

	public List<Videogame> getVideoGamesByReleaseDate(LocalDate releaseDate) {
		return this.videogameRepository.findByReleaseDate(releaseDate);
	}

	public List<Videogame> getVideoGamesByOrderByReleaseDateDesc() {
		return this.videogameRepository.findAllByOrderByReleaseDateDesc();
	}
	
	public boolean existsByTitleAndReleaseDateAndGenre(Videogame videogame) {
		return this.videogameRepository.existsByTitleAndReleaseDateAndGenre(videogame.getTitle(), videogame.getReleaseDate(),videogame.getGenre());
	}
	
	public void addImageToVideogame(Videogame videogame, MultipartFile multipartFile) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		videogame.setImageFileName(fileName);
		String uploadDir = "src/main/upload/images/videogamesImages/";
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	}
}
