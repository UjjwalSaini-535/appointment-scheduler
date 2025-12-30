package com.ujjwal.appointment_scheduler.controller;

import com.ujjwal.appointment_scheduler.dto.ProviderCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.ProviderResponseDto;
import com.ujjwal.appointment_scheduler.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

	private final ProviderService providerService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProviderResponseDto createProvider(
			@Valid @RequestBody ProviderCreateRequestDto request) {

		return providerService.createProvider(request);
	}
}
