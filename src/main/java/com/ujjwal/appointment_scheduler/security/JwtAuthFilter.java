package com.ujjwal.appointment_scheduler.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/api/auth/");
	}
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

		String token = extractTokenFromCookie(request);

		if (token != null) {
			Claims claims = jwtUtil.extractClaims(token);

			String userId = claims.getSubject();
			String role = claims.get("role", String.class);

			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(
							userId,
							null,
							List.of(() -> "ROLE_" + role)
					);

			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String extractTokenFromCookie(HttpServletRequest request) {

		if (request.getCookies() == null) {
			return null;
		}

		for (Cookie cookie : request.getCookies()) {
			if ("JWT_TOKEN".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
