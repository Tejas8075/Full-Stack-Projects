package com.authentify.backend.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.authentify.backend.dto.AuthRequest;
import com.authentify.backend.dto.AuthResponse;
import com.authentify.backend.dto.ResetPasswordRequest;
import com.authentify.backend.service.ProfileService;
import com.authentify.backend.service.impl.AppUserDetailsServiceImpl;
import com.authentify.backend.util.JWTUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	
	private final JWTUtil jwtUtil;
	
	private final AppUserDetailsServiceImpl appUserDetailsServiceImpl;
	
	private final ProfileService pService;
	
	@PostMapping("/login")
	ResponseEntity<?> login(@RequestBody AuthRequest request){
		
		try {
			authenticate(request.getEmail(), request.getPassword());
			
			// Step 1: Get the UserDetailsService
			// we can pass the userDetials to the JWT token to create the JWT token
			final UserDetails userDetails = appUserDetailsServiceImpl.loadUserByUsername(request.getEmail());
			
			// Step 2: Creating token
			final String jwtToken = jwtUtil.generateToken(userDetails);
			
			// Send the JWT token inside the cookie
			// Step 3: Create a Cookie
			ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
					                              .httpOnly(true)
					                              .path("/")
					                              .maxAge(Duration.ofDays(1))
					                              .sameSite("Strict")
					                              .build();
			
			return ResponseEntity.ok()
					             .header(HttpHeaders.SET_COOKIE, cookie.toString())
					             .body(new AuthResponse(request.getEmail(), jwtToken));
					
			
			
		} catch(BadCredentialsException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", true);
			error.put("message", "Email or password is incorrect");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		} catch(DisabledException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", true);
			error.put("message", "Account is disabled");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		} catch(Exception ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", true);
			error.put("message", "Authentication failed");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
		
	}

	private void authenticate(String email, String password) {

		// Calling the auth manager
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

	}
	
	@GetMapping("/is-authenticated")
	ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email) {
		
		return ResponseEntity.ok(email!=null);
		
	}
	
	@PostMapping("/send-reset-otp")
	public void sendResetOtp(@RequestParam String email) {
		
		try {
			
			pService.sendResetOtp(email);
			
		} catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
		
	}
	
	@PostMapping("/reset-password")
	void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		
		try {
			
			pService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
			
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}
	
	@PostMapping("/send-otp")
	void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email) {
		
		try {
			
			pService.sendOtp(email);
			
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}
	
}
