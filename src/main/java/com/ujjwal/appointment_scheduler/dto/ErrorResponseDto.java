package com.ujjwal.appointment_scheduler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponseDto {

	private int status;
	private String error;
	private String message;
	@JsonFormat(
			shape = JsonFormat.Shape.STRING,
			pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
	)
	private LocalDateTime timestamp;

	public ErrorResponseDto(int status, String error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
}
