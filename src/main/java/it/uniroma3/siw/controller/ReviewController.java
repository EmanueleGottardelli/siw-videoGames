package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.Videogame;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.VideogameService;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private VideogameService videogameService;

	@Autowired
	private CredentialsService credentialsService;

	@GetMapping("/review/{id}/edit")
	public String formEditReview(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		Review review = reviewService.getReviewById(id);

		// controllo solo l'autore puo modificare
		if (!review.getAuthor().getCredentials().getUsername().equals(userDetails.getUsername())) {
			return "redirect:/videogame/" + review.getVideogame().getId();
		}

		model.addAttribute("review", review);
		return "formEditReview";
	}

	@PostMapping("/review/{id}/edit")
	public String updateReview(@PathVariable("id") Long id, @ModelAttribute("review") Review updatedReview) {
//			@AuthenticationPrincipal UserDetails userDetails 

		Review review = reviewService.getReviewById(id);

//		// controllo solo l'autore puo modificare
//		if (!review.getAuthor().getCredentials().getUsername().equals(userDetails.getUsername())) {
//			return "redirect:/videogame/" + review.getVideogame().getId();
//		}

		if (updatedReview.getRating() != null) {
			review.setRating(updatedReview.getRating());
		}

		if (updatedReview.getText() != null) {
			review.setText(updatedReview.getText());
		}

		reviewService.saveReview(review);

		// redirect alla pagina del videogame
		return "redirect:/videogame/" + review.getVideogame().getId();
	}

	@GetMapping("/review/{id}/delete")
	public String deleteReview(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {

		// recupero il commento da cancellare
		Review review = reviewService.getReviewById(id);
		Long videogameId = review.getVideogame().getId();

		reviewService.deleteReview(review);

		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			// redirect alla pagina di update del videogame
			return "redirect:/admin/formUpdateVideogame/" + videogameId;
		}

		return "redirect:/videogame/" + videogameId;
	}

	@PostMapping("/videogame/{videogameId}/review")
	public String addReview(@PathVariable("videogameId") Long videogameId,
			@ModelAttribute("newReview") Review newReview, @AuthenticationPrincipal UserDetails userDetails) {

		// forzo login se non si è registrati
		if (userDetails == null)
			return "redirect:/login";

		// recupera il videogame
		Videogame videogame = videogameService.getVideogameById(videogameId);

		// collego la review al videogame
		newReview.setVideogame(videogame);

		// collego credenziali e user alla review
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user = credentials.getUser();
		newReview.setAuthor(user);

		// salvo
		reviewService.saveReview(newReview);
		
		// redirect alla pagina del videogame
		return "redirect:/videogame/" + videogameId;
	}

}
