package com.billox.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String uploadDir = Paths.get("uploads").toAbsolutePath().toString();
		
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:" + uploadDir + "/");
		
	}
	
}
