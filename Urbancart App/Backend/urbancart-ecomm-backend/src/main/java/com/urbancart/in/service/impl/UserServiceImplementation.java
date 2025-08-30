package com.urbancart.in.service.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.urbancart.in.config.JwtProvider;
import com.urbancart.in.exception.UserException;
import com.urbancart.in.model.User;
import com.urbancart.in.repository.PasswordResetTokenRepository;
import com.urbancart.in.repository.UserRepository;
import com.urbancart.in.service.UserService;

@Service
public class UserServiceImplementation implements UserService {


	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	private PasswordEncoder passwordEncoder;
	private PasswordResetTokenRepository passwordResetTokenRepository;
	private JavaMailSender javaMailSender;
	
	public UserServiceImplementation(
			UserRepository userRepository,
			JwtProvider jwtProvider,
			PasswordEncoder passwordEncoder,
			PasswordResetTokenRepository passwordResetTokenRepository,
			JavaMailSender javaMailSender) {
		
		this.userRepository=userRepository;
		this.jwtProvider=jwtProvider;
		this.passwordEncoder=passwordEncoder;
		this.passwordResetTokenRepository=passwordResetTokenRepository;
		this.javaMailSender=javaMailSender;
		
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
	    if (jwt == null || jwt.trim().isEmpty()) {
	        throw new UserException("Missing JWT token");
	    }

	    // If header contains "Bearer <token>", strip prefix
	    String token = jwt.startsWith("Bearer ") ? jwt.substring(7).trim() : jwt.trim();

	    String email = jwtProvider.getEmailFromJwtToken(token); // pass raw token

	    if (email == null || email.trim().isEmpty()) {
	        throw new UserException("Invalid JWT token");
	    }

	    User user = userRepository.findByEmail(email);

	    if (user == null) {
	        throw new UserException("user not exist with email " + email);
	    }
	    return user;
	}



	
	@Override
	public User findUserByEmail(String username) throws UserException {
		
		User user=userRepository.findByEmail(username);
		
		if(user!=null) {
			
			return user;
		}
		
		throw new UserException("user not exist with username "+username);
	}



}
