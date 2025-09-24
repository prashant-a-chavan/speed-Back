package com.project.speedBack;

import com.project.speedBack.service.SpeedbackService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpeedBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeedBackApplication.class, args);
	}

}
