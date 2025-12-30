package com.ujjwal.appointment_scheduler.service.impl;

import com.ujjwal.appointment_scheduler.dto.AvailabilityCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.AvailabilityResponseDto;
import com.ujjwal.appointment_scheduler.entity.Availability;
import com.ujjwal.appointment_scheduler.entity.Provider;
import com.ujjwal.appointment_scheduler.enums.Role;
import com.ujjwal.appointment_scheduler.exception.BadRequestException;
import com.ujjwal.appointment_scheduler.exception.ResourceNotFoundException;
import com.ujjwal.appointment_scheduler.repository.AvailabilityRepository;
import com.ujjwal.appointment_scheduler.repository.ProviderRepository;
import com.ujjwal.appointment_scheduler.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

	private final AvailabilityRepository availabilityRepository;
	private final ProviderRepository providerRepository;

	@Override
	public AvailabilityResponseDto addAvailability(
			AvailabilityCreateRequestDto request) {

		Long userId = Long.parseLong(
				SecurityContextHolder.getContext()
						.getAuthentication()
						.getPrincipal().toString()
		);

		Provider provider = providerRepository.findById(request.getProviderId())
				.orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

		// Safety check (role-based business rule)
		if (provider.getUser().getRole() != Role.PROVIDER) {
			throw new BadRequestException("Only PROVIDER can add availability");
		}

		if (!Objects.equals(provider.getUser().getId(), userId)) {
			throw new BadRequestException("You are not allowed to add availability for another user");
		}

		validateTime(request.getStartTime(), request.getEndTime());
		validateOverlap(provider, request.getDayOfWeek(),
				request.getStartTime(), request.getEndTime());

		Availability availability = new Availability();
		availability.setProvider(provider);
		availability.setDayOfWeek(request.getDayOfWeek());
		availability.setStartTime(request.getStartTime());
		availability.setEndTime(request.getEndTime());
		availability.setSlotDurationMinutes(request.getSlotDurationMinutes());

		Availability saved = availabilityRepository.save(availability);
		return mapToResponse(saved);
	}

	private void validateTime(
			java.time.LocalTime start,
			java.time.LocalTime end) {

		if (!start.isBefore(end)) {
			throw new BadRequestException("Start time must be before end time");
		}
	}

	private void validateOverlap(
			Provider provider,
			DayOfWeek day,
			java.time.LocalTime start,
			java.time.LocalTime end) {

		List<Availability> existing =
				availabilityRepository.findByProviderAndDayOfWeek(provider, day);

		for (Availability a : existing) {
			boolean overlap =
					start.isBefore(a.getEndTime())
							&& end.isAfter(a.getStartTime());

			if (overlap) {
				throw new BadRequestException(
						"Availability overlaps with existing time window");
			}
		}
	}

	private AvailabilityResponseDto mapToResponse(Availability a) {
		return AvailabilityResponseDto
				.builder()
				.availabilityId(a.getId())
				.providerId(a.getProvider().getId())
				.dayOfWeek(a.getDayOfWeek())
				.startTime(a.getStartTime())
				.endTime(a.getEndTime())
				.slotDurationMinutes(a.getSlotDurationMinutes())
				.build();
	}
}
