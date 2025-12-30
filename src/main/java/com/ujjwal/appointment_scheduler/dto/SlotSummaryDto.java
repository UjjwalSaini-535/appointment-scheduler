package com.ujjwal.appointment_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotSummaryDto {

	private Long slotId;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private String status;
	private ProviderSummaryDto provider;
}
