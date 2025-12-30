package com.ujjwal.appointment_scheduler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwal.appointment_scheduler.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException)
			throws IOException {

		ErrorResponseDto error = new ErrorResponseDto(
				HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.name(),
				"Authentication required"
		);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.getWriter()
				.write(objectMapper.writeValueAsString(error));
	}
}
