package com.ujjwal.appointment_scheduler.service.impl;

import com.ujjwal.appointment_scheduler.entity.Availability;
import com.ujjwal.appointment_scheduler.entity.Provider;
import com.ujjwal.appointment_scheduler.entity.Slot;
import com.ujjwal.appointment_scheduler.enums.SlotStatus;
import com.ujjwal.appointment_scheduler.repository.AvailabilityRepository;
import com.ujjwal.appointment_scheduler.repository.ProviderRepository;
import com.ujjwal.appointment_scheduler.repository.SlotRepository;
import com.ujjwal.appointment_scheduler.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

	private final SlotRepository slotRepository;
	private final ProviderRepository providerRepository;
	private final AvailabilityRepository availabilityRepository;

	@Override
	public void generateSlotsForNextDays(int days) {

		LocalDate today = LocalDate.now();
		LocalDate endDate = today.plusDays(days);

		List<Provider> providers = providerRepository.findAll();

		for (Provider provider : providers) {

			LocalDate date = today;

			while (!date.isAfter(endDate)) {

				List<Availability> availabilities =
						availabilityRepository.findByProviderAndDayOfWeek(
								provider,
								date.getDayOfWeek()
						);

				for (Availability availability : availabilities) {
					generateSlotsForDate(provider, availability, date);
				}

				date = date.plusDays(1);
			}
		}
	}

	private void generateSlotsForDate(
			Provider provider,
			Availability availability,
			LocalDate date) {

		LocalTime slotStart = availability.getStartTime();
		int duration = availability.getSlotDurationMinutes();

		while (true) {
			LocalTime slotEnd = slotStart.plusMinutes(duration);

			if (slotEnd.isAfter(availability.getEndTime())) {
				break;
			}

			boolean exists =
					slotRepository.existsByProviderAndDateAndStartTime(
							provider, date, slotStart);

			if (!exists) {
				Slot slot = new Slot();
				slot.setProvider(provider);
				slot.setDate(date);
				slot.setStartTime(slotStart);
				slot.setEndTime(slotEnd);
				slot.setStatus(SlotStatus.AVAILABLE);

				slotRepository.save(slot);
			}

			slotStart = slotEnd;
		}
	}
}
