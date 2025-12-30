package com.ujjwal.appointment_scheduler.exception;

import com.ujjwal.appointment_scheduler.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 400 - Bad Request
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponseDto> handleBadRequest(
			BadRequestException ex) {

		ErrorResponseDto response = new ErrorResponseDto(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.name(),
				ex.getMessage()
		);

		return ResponseEntity.badRequest().body(response);
	}

	// 404 - Not Found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleNotFound(
			ResourceNotFoundException ex) {

		ErrorResponseDto response = new ErrorResponseDto(
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.name(),
				ex.getMessage()
		);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	// 401 - Unauthorized
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponseDto> handleUnauthorized(
			UnauthorizedException ex) {

		ErrorResponseDto response = new ErrorResponseDto(
				HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.name(),
				ex.getMessage()
		);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	// Validation errors (@Valid)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidation(
			MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();

		ErrorResponseDto response = new ErrorResponseDto(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.name(),
				message
		);

		return ResponseEntity.badRequest().body(response);
	}

	// 500 - Fallback (VERY IMPORTANT)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleGeneric(
			Exception ex) {

		ErrorResponseDto response = new ErrorResponseDto(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.name(),
				"Something went wrong"
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(response);
	}
}
