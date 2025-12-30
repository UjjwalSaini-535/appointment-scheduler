package com.ujjwal.appointment_scheduler.repository;

import com.ujjwal.appointment_scheduler.entity.Provider;
import com.ujjwal.appointment_scheduler.entity.Slot;
import com.ujjwal.appointment_scheduler.enums.SlotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {

	boolean existsByProviderAndDateAndStartTime(
			Provider provider,
			LocalDate date,
			LocalTime startTime
	);

	List<Slot> findByProviderAndDateAndStatus(
			Provider provider,
			LocalDate date,
			SlotStatus status
	);

	List<Slot> findByProviderAndDateAfter(
			Provider provider,
			LocalDate date
	);


	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from Slot s where s.id = :slotId")
	Optional<Slot> findByIdForUpdate(Long slotId);
}
