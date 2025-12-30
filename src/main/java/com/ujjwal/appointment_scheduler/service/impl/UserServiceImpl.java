package com.ujjwal.appointment_scheduler.service.impl;

import com.ujjwal.appointment_scheduler.dto.UpdateUserRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserResponseDto;
import com.ujjwal.appointment_scheduler.entity.Appointment;
import com.ujjwal.appointment_scheduler.entity.Provider;
import com.ujjwal.appointment_scheduler.entity.Slot;
import com.ujjwal.appointment_scheduler.entity.User;
import com.ujjwal.appointment_scheduler.enums.AppointmentStatus;
import com.ujjwal.appointment_scheduler.enums.Role;
import com.ujjwal.appointment_scheduler.enums.SlotStatus;
import com.ujjwal.appointment_scheduler.exception.BadRequestException;
import com.ujjwal.appointment_scheduler.exception.ResourceNotFoundException;
import com.ujjwal.appointment_scheduler.exception.UnauthorizedException;
import com.ujjwal.appointment_scheduler.repository.*;
import com.ujjwal.appointment_scheduler.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ProviderRepository providerRepository;
	private final AvailabilityRepository availabilityRepository;
	private final SlotRepository slotRepository;
	private final AppointmentRepository appointmentRepository;

	@Override
	public UserResponseDto createUser(UserCreateRequestDto request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadRequestException("Email already exists");
		}

		if (request.getRole() == Role.ADMIN) {
			throw new UnauthorizedException("You cannot assign ADMIN role");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());

		User savedUser = userRepository.save(user);

		return mapToResponse(savedUser);
	}

	@Override
	public UserResponseDto updateUser(UpdateUserRequestDto request) {

		Long userId = getLoggedInUserId();

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (request.getName() != null && !request.getName().isBlank()) {
			user.setName(request.getName());
		}

		if (request.getNewRole() != null) {
			if (request.getNewRole() == Role.ADMIN) {
				throw new UnauthorizedException("You cannot assign ADMIN role");
			}

			user.setRole(request.getNewRole());
		}

		User updatedUser = userRepository.save(user);

		return mapToResponse(updatedUser);
	}

	@Override
	@Transactional
	public void deactivateUser() {
		Long userId = getLoggedInUserId();

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setIsActive(false);

		if (user.getRole() == Role.PROVIDER) {
			handleProviderDeactivation(user);
		}

		userRepository.save(user);
	}

	@Override
	public void reactivateUser(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setIsActive(true);
		userRepository.save(user);
	}

	private void handleProviderDeactivation(User user) {

		Provider provider = providerRepository.findByUser(user)
				.orElseThrow(() ->
						new ResourceNotFoundException("Provider not found"));

		LocalDate today = LocalDate.now();

		// DELETE availability (future scheduling template)
		availabilityRepository.deleteByProvider(provider);

		// CANCEL future slots
		List<Slot> futureSlots =
				slotRepository.findByProviderAndDateAfter(provider, today.minusDays(1));

		for (Slot slot : futureSlots) {
			slot.setStatus(SlotStatus.CANCELLED);
		}
		slotRepository.saveAll(futureSlots);

		// CANCEL future appointments
		List<Appointment> appointments =
				appointmentRepository.findBySlotIn(futureSlots);

		for (Appointment appt : appointments) {
			appt.setStatus(AppointmentStatus.CANCELLED);
		}
		appointmentRepository.saveAll(appointments);
	}


	private Long getLoggedInUserId() {
		return Long.parseLong(
				SecurityContextHolder.getContext()
						.getAuthentication()
						.getPrincipal()
						.toString()
		);
	}

	private UserResponseDto mapToResponse(User user) {
		return UserResponseDto
				.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.role(user.getRole())
				.createdAt(user.getCreatedAt())
				.build();
	}
}
