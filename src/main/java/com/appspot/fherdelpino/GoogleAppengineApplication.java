package com.appspot.fherdelpino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleAppengineApplication implements CommandLineRunner {

	@Value("${spring.data.mongodb.uri}")
	private String mongoDbUri;


	private static final Logger LOG = LoggerFactory
			.getLogger(GoogleAppengineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GoogleAppengineApplication.class, args);
	}
	@Override
	public void run(String... args) {
		LOG.info("fhdpr | mongodburi={}", mongoDbUri);
	}

}
