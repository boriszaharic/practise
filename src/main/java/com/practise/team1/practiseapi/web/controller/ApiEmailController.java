package com.practise.team1.practiseapi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.practise.team1.practiseapi.service.EmailService;
import com.practise.team1.practiseapi.service.impl.CustomUserDetailsService;
import com.practise.team1.practiseapi.support.EmailDTOToEmail;
import com.practise.team1.practiseapi.web.dto.EmailDTO;

@RestController
@RequestMapping(value = "/recover")
public class ApiEmailController {

	@Autowired
	EmailService emailService;

	@Autowired
	CustomUserDetailsService userService;
	
	@Autowired
	EmailDTOToEmail toUsername;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> recover(@RequestBody(required = true) EmailDTO email) {
		String username = toUsername.convert(email);
		if (username != null) {
			UserDetails user = userService.loadUserByUsername(username);
			//System.out.println(user);
			if (user != null) {
				String password = user.getPassword();
				String subject = "Password recovery";
				String text = password;
				emailService.sendSimpleMessage(username, subject, text);
				//System.out.println("What happened to email?");
				return new ResponseEntity<>("Check your email for recovered password", HttpStatus.OK);
			} else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
