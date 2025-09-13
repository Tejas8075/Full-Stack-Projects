package com.authentify.backend.service.impl;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authentify.backend.entity.UserEntity;
import com.authentify.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository uRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		UserEntity existingUser = uRepo.findByEmail(email)
		     .orElseThrow(() -> new UsernameNotFoundException("Email not found for the email: " + email));
		
		return new User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
		
	}
	
	
	
}
