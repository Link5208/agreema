package com.example.demo.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			// Use ISO format for dates
			builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		};
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		// Use ISO_DATE format
		registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
		registrar.registerFormatters(registry);
	}
}