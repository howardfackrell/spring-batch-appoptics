package com.example.springbatchappoptics;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchAppopticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchAppopticsApplication.class, args);
	}

}
