package com.practise.team1.practiseapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.practise.team1.practiseapi.model.CustomUserDetails;
import com.practise.team1.practiseapi.model.User;
import com.practise.team1.practiseapi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> optionalUser = userRepository.findByEmail(username);
		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found."));
		
		return optionalUser.map(CustomUserDetails::new).get();
	}

}
