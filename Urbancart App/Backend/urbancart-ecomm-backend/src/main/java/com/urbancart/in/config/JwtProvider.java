package com.urbancart.in.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

	private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	public String generateToken(Authentication auth) {
	    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
	    String roles = populateAuthorities(authorities);

	    String username = auth.getName();
	    if (username.startsWith("seller_")) {
	        username = username.substring("seller_".length());
	    }

	    return Jwts.builder()
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
	            .claim("email", username) // <-- always store pure email
	            .claim("authorities", roles)
	            .signWith(key, Jwts.SIG.HS256)
	            .compact();
	}


	public String getEmailFromJwtToken(String jwt) {
	    if (jwt == null) return null;

	    String token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;

	    try {
	        Claims claims = Jwts.parser()
	                .verifyWith(key)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();

	        return String.valueOf(claims.get("email"));
	    } catch (io.jsonwebtoken.ExpiredJwtException eje) {
	        // handle expiry upstream if needed
	        throw eje;
	    } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
	        // return null or rethrow a meaningful exception
	        return null;
	    }
	}



	public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> auths = new HashSet<>();

		for (GrantedAuthority authority : collection) {
			auths.add(authority.getAuthority());
		}
		return String.join(",", auths);
	}

}
