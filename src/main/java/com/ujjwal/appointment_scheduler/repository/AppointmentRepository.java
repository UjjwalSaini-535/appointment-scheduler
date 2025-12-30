package com.ujjwal.appointment_scheduler.repository;

import com.ujjwal.appointment_scheduler.entity.Appointment;
import com.ujjwal.appointment_scheduler.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	List<Appointment> findBySlotIn(List<Slot> slots);
}
