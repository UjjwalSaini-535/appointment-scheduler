package com.ujjwal.appointment_scheduler.service.impl;

import com.ujjwal.appointment_scheduler.dto.AppointmentResponseDto;
import com.ujjwal.appointment_scheduler.dto.ProviderSummaryDto;
import com.ujjwal.appointment_scheduler.dto.SlotSummaryDto;
import com.ujjwal.appointment_scheduler.dto.UserResponseDto;
import com.ujjwal.appointment_scheduler.entity.Appointment;
import com.ujjwal.appointment_scheduler.entity.Slot;
import com.ujjwal.appointment_scheduler.entity.User;
import com.ujjwal.appointment_scheduler.enums.AppointmentStatus;
import com.ujjwal.appointment_scheduler.enums.SlotStatus;
import com.ujjwal.appointment_scheduler.exception.BadRequestException;
import com.ujjwal.appointment_scheduler.exception.ResourceNotFoundException;
import com.ujjwal.appointment_scheduler.repository.AppointmentRepository;
import com.ujjwal.appointment_scheduler.repository.SlotRepository;
import com.ujjwal.appointment_scheduler.repository.UserRepository;
import com.ujjwal.appointment_scheduler.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private final SlotRepository slotRepository;
	private final UserRepository userRepository;
	private final AppointmentRepository appointmentRepository;

	@Override
	@Transactional
	public AppointmentResponseDto bookSlot(Long userId, Long slotId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// LOCK slot row
		Slot slot = slotRepository.findByIdForUpdate(slotId)
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		if (slot.getStatus() != SlotStatus.AVAILABLE) {
			throw new BadRequestException("Slot already booked");
		}

		// Update slot
		slot.setStatus(SlotStatus.BOOKED);

		Appointment appointment = new Appointment();
		appointment.setSlot(slot);
		appointment.setUser(user);
		appointment.setStatus(AppointmentStatus.CONFIRMED);

		return mapToResponse(appointmentRepository.save(appointment));
	}

	@Override
	@Transactional
	public void cancelAppointment(Long appointmentId, Long userId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		if (!appointment.getUser().getId().equals(userId)) {
			throw new BadRequestException("You can cancel only your own appointment");
		}

		if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
			throw new BadRequestException("Appointment already cancelled");
		}

		// Lock slot
		Slot slot = slotRepository.findByIdForUpdate(
				appointment.getSlot().getId()
		).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		// Free slot
		slot.setStatus(SlotStatus.AVAILABLE);

		// Update appointment
		appointment.setStatus(AppointmentStatus.CANCELLED);

		appointmentRepository.save(appointment);
	}

	@Override
	@Transactional
	public AppointmentResponseDto rescheduleAppointment(
			Long appointmentId,
			Long newSlotId,
			Long userId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		if (!appointment.getUser().getId().equals(userId)) {
			throw new BadRequestException("You can reschedule only your own appointment");
		}

		if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
			throw new BadRequestException("Cancelled appointment cannot be rescheduled");
		}

		// Lock old slot
		Slot oldSlot = slotRepository.findByIdForUpdate(
				appointment.getSlot().getId()
		).orElseThrow(() -> new ResourceNotFoundException("Old slot not found"));

		// Lock new slot
		Slot newSlot = slotRepository.findByIdForUpdate(newSlotId)
				.orElseThrow(() -> new ResourceNotFoundException("New slot not found"));

		if (newSlot.getStatus() != SlotStatus.AVAILABLE) {
			throw new ResourceNotFoundException("New slot is not available");
		}

		// Free old slot
		oldSlot.setStatus(SlotStatus.AVAILABLE);

		// Book new slot
		newSlot.setStatus(SlotStatus.BOOKED);

		// Update appointment
		appointment.setSlot(newSlot);
		appointment.setStatus(AppointmentStatus.CONFIRMED);

		return mapToResponse(appointmentRepository.save(appointment));
	}

	private AppointmentResponseDto mapToResponse(Appointment appointment) {

		AppointmentResponseDto dto = AppointmentResponseDto.builder()
				.appointmentId(appointment.getId())
				.status(appointment.getStatus().name())
				.createdAt(appointment.getCreatedAt()).build();

		// USER
		UserResponseDto userDto = UserResponseDto.builder()
				.id(appointment.getUser().getId())
				.name(appointment.getUser().getName())
				.email(appointment.getUser().getEmail())
				.role(appointment.getUser().getRole())
				.createdAt(appointment.getUser().getCreatedAt())
				.build();

		dto.setUser(userDto);

		// PROVIDER
		ProviderSummaryDto providerDto = ProviderSummaryDto.builder()
				.providerId(
						appointment.getSlot().getProvider().getId()
				).serviceName(
						appointment.getSlot().getProvider().getServiceName()
				).build();

		// SLOT
		SlotSummaryDto slotDto = SlotSummaryDto.builder()
				.slotId(appointment.getSlot().getId())
				.date(appointment.getSlot().getDate())
				.startTime(appointment.getSlot().getStartTime())
				.endTime(appointment.getSlot().getEndTime())
				.status(appointment.getSlot().getStatus().name())
				.provider(providerDto)
				.build();

		dto.setSlot(slotDto);

		return dto;
	}
}
