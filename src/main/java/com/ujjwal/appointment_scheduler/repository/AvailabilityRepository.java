package com.ujjwal.appointment_scheduler.repository;

import com.ujjwal.appointment_scheduler.entity.Availability;
import com.ujjwal.appointment_scheduler.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

	List<Availability> findByProviderAndDayOfWeek(
			Provider provider,
			DayOfWeek dayOfWeek
	);

	void deleteByProvider(Provider provider);
}
