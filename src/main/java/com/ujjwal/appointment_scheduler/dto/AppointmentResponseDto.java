package com.ujjwal.appointment_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDto {

	private Long appointmentId;
	private String status;
	private LocalDateTime createdAt;
	private UserResponseDto user;
	private SlotSummaryDto slot;
}
