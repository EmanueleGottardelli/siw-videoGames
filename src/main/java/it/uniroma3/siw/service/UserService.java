package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected CredentialsService credentialsService;
	
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User getCurrentUser() {
		User user = null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String name = authentication.getName();
			user = credentialsService.getCredentials(name).getUser();
		}
		
		return user;
	}
	
	@Transactional
	public boolean alreadyExists(User user) {
		return this.userRepository.existsByNameAndSurnameAndEmail(user.getName(), user.getSurname(), user.getEmail());
	}
	
	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	@Transactional
	public List<User> getAllUsers(){
		List<User> result = new ArrayList<>();
		
		for(User u : this.userRepository.findAll()) {
			result.add(u);
		}
		
		return result;
	}
}
