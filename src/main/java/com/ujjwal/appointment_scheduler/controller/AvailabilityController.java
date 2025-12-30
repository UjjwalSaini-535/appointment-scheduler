package com.ujjwal.appointment_scheduler.controller;

import com.ujjwal.appointment_scheduler.dto.AvailabilityCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.AvailabilityResponseDto;
import com.ujjwal.appointment_scheduler.service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

	private final AvailabilityService availabilityService;

	public AvailabilityController(AvailabilityService availabilityService) {
		this.availabilityService = availabilityService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AvailabilityResponseDto addAvailability(
			@Valid @RequestBody AvailabilityCreateRequestDto request) {

		return availabilityService.addAvailability(request);
	}
}
