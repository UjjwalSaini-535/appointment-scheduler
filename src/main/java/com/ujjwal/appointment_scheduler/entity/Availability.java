package com.ujjwal.appointment_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(
		name = "availability",
		uniqueConstraints = {
				@UniqueConstraint(
						columnNames = {"provider_id", "dayOfWeek", "startTime", "endTime"}
				)
		}
)
@Getter
@Setter
public class Availability {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	private LocalTime startTime;

	private LocalTime endTime;

	private int slotDurationMinutes;
}

