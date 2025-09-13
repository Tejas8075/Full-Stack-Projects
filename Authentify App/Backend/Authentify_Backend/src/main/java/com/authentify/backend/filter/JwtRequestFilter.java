package com.authentify.backend.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authentify.backend.service.impl.AppUserDetailsServiceImpl;
import com.authentify.backend.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private final AppUserDetailsServiceImpl appUserDetailsServiceImpl;

  	private final JWTUtil jwtUtil;
  	
  	private static final List<String> PUBLIC_URLS = List.of("/login", "/register", "/send-reset-otp", "/reset-password", "/logout");
	
  	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
  		// Step 1: Get the servlet path
  		String path = request.getServletPath();
  		
  		// Step 2: Skip the JWT validation for the public url's
  		if(PUBLIC_URLS.contains(path)) {
  			// Ask the filterChain Pass the request and response to the next filter
  			filterChain.doFilter(request, response);
  			return;
  		}
  		
  		String jwt = null;
  		
  		String email = null;
  		
  		// 1) Check the Authorization Header
  		// if not using COOKIE we get "Authorization"
  		final String authorizationHeader = request.getHeader("Authorization");
  		
  		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ) {
  			// Gives only the token and extract the token and store it in jwt
  			jwt = authorizationHeader.substring(7);
  		}
  		
  		// 2) If Authorization Header is NOT FOUND, check the COOKIES
  		if(jwt == null) {
  			Cookie[] cookies = request.getCookies();
  			if(cookies != null) {
  				for(Cookie cookie : cookies) {
  					if("jwt".equals(cookie.getName())) {
  						jwt = cookie.getValue();
  						break;
  					}
  				}
  			}
  		}
  		
  		// 3) Validate the token and set the Security Context
  		if(jwt != null) {
  			email = jwtUtil.extractEmail(jwt);
  			// if email is not null and Security Context Holder is empty then
  			if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
  				// Get the PRINCIPAL Object
  				UserDetails userDetails = appUserDetailsServiceImpl.loadUserByUsername(email);
  				// if token is validated
  				if(jwtUtil.validateToken(jwt, userDetails)) {
  					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
  					// Set the PRINCIPAL Object wrapped in UsernamePasswordAuthenticationToken to the Security Context Holder
  					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  				}
  			}
  		}
  		
  		 // Pass to the next filter
		filterChain.doFilter(request, response);
		
	}

}
