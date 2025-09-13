package com.authentify.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.authentify.backend.dto.ProfileRequest;
import com.authentify.backend.dto.ProfileResponse;
import com.authentify.backend.service.ProfileService;
import com.authentify.backend.service.impl.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
//@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService pService;
	
	private final EmailService eService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ProfileResponse register(@Valid @RequestBody ProfileRequest request) {

		ProfileResponse response = pService.createProfile(request);

		// send welcome email
		eService.sendWelcomeEmail(response.getEmail(), response.getName());

		return response;

	}

	// If the User is not logged in the customAuthenticationEntryPoint throws the
	// exception in write()
	@GetMapping("/profile")
	// Get the UserDetails from the securityContext
	public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {

		// Send to the service layer
		return pService.getProfile(email);

	}

//	@GetMapping("/test")
//	public String test() {
//		return "Auth is working";
//	}

}
