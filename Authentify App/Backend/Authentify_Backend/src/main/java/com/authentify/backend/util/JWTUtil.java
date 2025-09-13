package com.authentify.backend.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	// Generate a secret key for JWT
	@Value("${jwt.secret.key}")
	private String SECRET_KEY;
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		
		return createToken(claims, userDetails.getUsername());
		
	}

	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder()
		    .claims(claims)
		    .subject(email)
		    .issuedAt(new Date(System.currentTimeMillis()))
		    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hr expiration
		    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
		    .compact();
	}
	
	private Claims extractAllClaims(String token) {
		
		return Jwts.parser()
				   .setSigningKey(SECRET_KEY)
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
		
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		
		final Claims claims = extractAllClaims(token);
		
		return claimsResolver.apply(claims);
		
	}
	
	public String extractEmail(String token) {
		
		return extractClaim(token, Claims::getSubject);
		
	}
	
	public Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
		
	}
	
	private Boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
		
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		final String email = extractEmail(token);
		
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
		
	}
	
}
