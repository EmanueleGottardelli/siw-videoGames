package it.uniroma3.siw.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.siw.model.Credentials;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
	
	@Autowired
	DataSource dataSource;
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception{
		http
			// AUTORIZZAZIONE: definiamo chi può accedere a cosa
		.authorizeHttpRequests()
		//chiunque puo accedere alle pagine di index register login cataloghi e ricerca
		.requestMatchers(HttpMethod.GET,"/","/index","/login","/register","/css/**","/images/**",
				"/videogame","/developer","/videogame/**","/developer/**",
				"/searchVideogames","/searchDevelopers")
		.permitAll()
		//chiunque puo mandare richieste post a login e register
		.requestMatchers(HttpMethod.POST,"/login","/register").permitAll()
		// solo gli utenti autenticati con ruolo ADMIN possono accedere al path /admin/**
		.requestMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
		.requestMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
		// tutti gli utenti autenticati posso accedere al resto delle pagine
		.anyRequest().authenticated().and().exceptionHandling().accessDeniedPage("/index")
		
		// LOGIN : qui definisco il login
		.and().formLogin()
		.loginPage("/login")
		.defaultSuccessUrl("/success",true)
		
		// LOGOUT : definiszione del logout
		.and().logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true).permitAll();
	
		return http.build();
	}
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication().dataSource(dataSource)
		//query per recupero username e ruolo
		.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
		// query per recupero password
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
