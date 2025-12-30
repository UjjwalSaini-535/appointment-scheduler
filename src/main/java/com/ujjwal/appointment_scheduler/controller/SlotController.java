package com.ujjwal.appointment_scheduler.controller;

import com.ujjwal.appointment_scheduler.service.SlotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

	private final SlotService slotService;

	public SlotController(SlotService slotService) {
		this.slotService = slotService;
	}

	@PostMapping("/generate")
	public String generateSlots(@RequestParam(defaultValue = "7") int days) {

		slotService.generateSlotsForNextDays(days);
		return "Slots generated for next " + days + " days";
	}
}
