package com.ujjwal.appointment_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderSummaryDto {

	private Long providerId;
	private String serviceName;
}
