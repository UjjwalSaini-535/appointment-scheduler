package com.ujjwal.appointment_scheduler.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String SECRET;

	@Value("${jwt.expiry}")
	private long EXPIRATION;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	public String generateToken(Long userId, String role) {

		return Jwts.builder()
				.setSubject(userId.toString())
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Claims extractClaims(String token) {
		return Jwts.parser()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}
