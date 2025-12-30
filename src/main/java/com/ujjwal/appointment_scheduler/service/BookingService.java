package com.ujjwal.appointment_scheduler.service;

import com.ujjwal.appointment_scheduler.dto.AppointmentResponseDto;

public interface BookingService {

	AppointmentResponseDto bookSlot(Long userId, Long slotId);

	void cancelAppointment(Long appointmentId, Long userId);

	AppointmentResponseDto rescheduleAppointment(
			Long appointmentId,
			Long newSlotId,
			Long userId
	);
}
