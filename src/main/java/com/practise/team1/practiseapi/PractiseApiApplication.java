package com.practise.team1.practiseapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PractiseApiApplication {

	@Autowired 
	private TestData td;
	
	public static void main(String[] args) {
		SpringApplication.run(PractiseApiApplication.class, args);
	}
}
