package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesWebConfiguration
		implements WebMvcConfigurer {
	@Value("${hoanglong.upload-file.base-uri}")
	private String baseURI;

	@Override
	public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/storage/**")
				.addResourceLocations(baseURI);
	}

}
