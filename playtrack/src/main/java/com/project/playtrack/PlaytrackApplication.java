package com.project.playtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.project.playtrack")
public class PlaytrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaytrackApplication.class, args);
	}
}