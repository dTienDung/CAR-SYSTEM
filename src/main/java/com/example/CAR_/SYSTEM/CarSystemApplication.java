package com.example.CAR_.SYSTEM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarSystemApplication.class, args);
	}

}
