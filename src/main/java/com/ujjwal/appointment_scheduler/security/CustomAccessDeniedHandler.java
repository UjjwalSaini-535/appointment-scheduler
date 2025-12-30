package com.ujjwal.appointment_scheduler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwal.appointment_scheduler.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	CustomAccessDeniedHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException {

		ErrorResponseDto error = new ErrorResponseDto(
				HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN.name(),
				"You do not have permission to access this resource"
		);

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");
		response.getWriter()
				.write(objectMapper.writeValueAsString(error));
	}
}
