package com.stegvis_api.stegvis_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StegvisApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StegvisApiApplication.class, args);
	}
}
