package com.billox.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.billox.io.AuthRequest;
import com.billox.io.AuthResponse;
import com.billox.service.AppUserDetailsService;
import com.billox.service.UserService;
import com.billox.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final PasswordEncoder pEncoder;
	
	private final AuthenticationManager aManager;
	
	private final AppUserDetailsService appUserDetailsService;
	
	private final JwtUtil jwtUtil;
	
	private final UserService uService;
	
	@PostMapping("/login")
	AuthResponse login(@RequestBody AuthRequest request) throws Exception {
		
		authenticate(request.getEmail(), request.getPassword());
		
		// generate JWT token
		final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());
		
		final String jwtToken = jwtUtil.generateToken(userDetails);
		
		// fetch the role from repository
		String role = uService.getUserRole(request.getEmail());
		
		return new AuthResponse(request.getEmail(), jwtToken, role);
		
	}
	
	private void authenticate(String email, String password) throws Exception {

		try {
			
			aManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			
		} catch (DisabledException e) {
			throw new Exception("User Disabled");
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password is incorrect");
		}
		
	}

	// When we deploy our project for production then we need to generate the password
	@PostMapping("/encode")
	String encodePassword(@RequestBody Map<String, String> request) {
		
		return pEncoder.encode(request.get("password"));
		
	}
	
}
