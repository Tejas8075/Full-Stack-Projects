package com.urbancart.in.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtTokenValidator.class);

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		// skip auth endpoints, public resources and review endpoint
		return path.startsWith("/auth") || path.startsWith("/api/products/") && path.matches("/api/products/.+/reviews")
				|| path.startsWith("/public");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(JwtConstant.JWT_HEADER);

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7).trim();

			if (token.isEmpty()) {
				log.warn("Empty Bearer token received from request {}", request.getRequestURI());
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
				return;
			}
			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

				// validate and parse
				Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));

				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (io.jsonwebtoken.MalformedJwtException mje) {
				log.warn("Malformed JWT for request {}: {}", request.getRequestURI(), mje.getMessage());
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
				return;
			} catch (ExpiredJwtException eje) {
				log.warn("JWT token expired: {}", eje.getMessage());
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
				return;
			} catch (JwtException e) { // includes signature exceptions, malformed, etc
				log.warn("Invalid JWT token: {} ({})", e.getMessage(), e.getClass().getSimpleName());
				throw new BadCredentialsException("invalid token...", e);
			} catch (Exception e) {
				log.error("Unexpected error while validating JWT: {}", e.getMessage(), e);
				throw new BadCredentialsException("invalid token...", e);
			}
		}

		filterChain.doFilter(request, response);
	}
}
