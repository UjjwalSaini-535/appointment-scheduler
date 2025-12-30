package com.ujjwal.appointment_scheduler.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityCreateRequestDto {

	@NotNull
	private Long providerId;

	@NotNull
	private DayOfWeek dayOfWeek;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

	@Min(15)
	@Max(120)
	private int slotDurationMinutes;
}
