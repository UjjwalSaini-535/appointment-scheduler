package com.ujjwal.appointment_scheduler.service;

import com.ujjwal.appointment_scheduler.dto.AvailabilityCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.AvailabilityResponseDto;

public interface AvailabilityService {

	AvailabilityResponseDto addAvailability(AvailabilityCreateRequestDto request);
}
