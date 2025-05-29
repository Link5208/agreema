package com.example.demo.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final ZoneId ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			builder.timeZone(java.util.TimeZone.getTimeZone(ZONE_ID));
			builder.simpleDateFormat(DATE_TIME_FORMAT);
		};
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
				.withZone(ZONE_ID));
		registrar.registerFormatters(registry);
	}
}