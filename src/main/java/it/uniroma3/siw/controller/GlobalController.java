package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;

@ControllerAdvice
public class GlobalController {
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute("userDetails")
	public UserDetails getUserAuthDetails() {
		UserDetails userDetails = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		}
		
		return userDetails;
	}
	
	@ModelAttribute("globalUser")
	public User getUser() {
		return userService.getCurrentUser();
	}
}
