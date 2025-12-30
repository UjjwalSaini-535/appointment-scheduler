package com.ujjwal.appointment_scheduler.controller;

import com.ujjwal.appointment_scheduler.dto.AppointmentResponseDto;
import com.ujjwal.appointment_scheduler.service.BookingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public AppointmentResponseDto bookSlot(
			@RequestParam Long slotId) {
		return bookingService.bookSlot(getLoggedInUserId(), slotId);
	}

	@DeleteMapping("/{appointmentId}")
	public String cancelAppointment(
			@PathVariable Long appointmentId) {

		bookingService.cancelAppointment(appointmentId, getLoggedInUserId());
		return "Appointment cancelled successfully";
	}

	@PatchMapping("/{appointmentId}/reschedule")
	public AppointmentResponseDto rescheduleAppointment(
			@PathVariable Long appointmentId,
			@RequestParam Long newSlotId,
			@RequestParam Long userId) {

		return bookingService.rescheduleAppointment(
				appointmentId, newSlotId, userId);
	}

	private Long getLoggedInUserId() {
		return Long.parseLong(
				SecurityContextHolder.getContext()
						.getAuthentication()
						.getPrincipal()
						.toString()
		);
	}

}
