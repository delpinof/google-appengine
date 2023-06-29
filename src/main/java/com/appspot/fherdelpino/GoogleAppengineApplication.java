package com.appspot.fherdelpino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class GoogleAppengineApplication {

	private static final Logger LOG = LoggerFactory
			.getLogger(GoogleAppengineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GoogleAppengineApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(Environment environment) {
		return args -> LOG.info("fhdpr | mongodburi={}", environment.getProperty("spring.data.mongodb.uri"));
	}
}
