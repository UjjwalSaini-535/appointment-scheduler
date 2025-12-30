package com.ujjwal.appointment_scheduler.service.impl;

import com.ujjwal.appointment_scheduler.dto.ProviderCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.ProviderResponseDto;
import com.ujjwal.appointment_scheduler.entity.Provider;
import com.ujjwal.appointment_scheduler.entity.User;
import com.ujjwal.appointment_scheduler.enums.Role;
import com.ujjwal.appointment_scheduler.exception.BadRequestException;
import com.ujjwal.appointment_scheduler.exception.ResourceNotFoundException;
import com.ujjwal.appointment_scheduler.repository.ProviderRepository;
import com.ujjwal.appointment_scheduler.repository.UserRepository;
import com.ujjwal.appointment_scheduler.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

	private final ProviderRepository providerRepository;
	private final UserRepository userRepository;

	@Override
	public ProviderResponseDto createProvider(ProviderCreateRequestDto request) {

		Long userId = getLoggedInUserId();
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (user.getRole() != Role.PROVIDER) {
			throw new BadRequestException("Only PROVIDER role can add provider details");
		}

		if (providerRepository.existsByUser(user)) {
			throw new BadRequestException("Provider details already exist");
		}

		Provider provider = new Provider();
		provider.setUser(user);
		provider.setServiceName(request.getServiceName());

		Provider savedProvider = providerRepository.save(provider);

		return mapToResponse(savedProvider);
	}

	private Long getLoggedInUserId() {
		return Long.parseLong(
				SecurityContextHolder.getContext()
						.getAuthentication()
						.getPrincipal()
						.toString()
		);
	}

	private ProviderResponseDto mapToResponse(Provider provider) {
		return ProviderResponseDto
				.builder()
				.providerId(provider.getId())
				.userId(provider.getUser().getId())
				.userName(provider.getUser().getName())
				.serviceName(provider.getServiceName())
				.build();
	}
}