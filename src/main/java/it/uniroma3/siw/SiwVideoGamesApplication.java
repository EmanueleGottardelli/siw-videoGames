package it.uniroma3.siw;

import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SiwVideoGamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiwVideoGamesApplication.class, args);
	}
	
	@Configuration
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			String absolutePathToImages = Paths.get("src/main/upload").toAbsolutePath().toString();
			registry.addResourceHandler("/upload/**")
					.addResourceLocations("file:" + absolutePathToImages + "/")
					.setCachePeriod(0);
		}
	}

}
