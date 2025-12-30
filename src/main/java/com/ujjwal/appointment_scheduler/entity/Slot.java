package com.ujjwal.appointment_scheduler.entity;

import com.ujjwal.appointment_scheduler.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
		name = "slots",
		uniqueConstraints = {
				@UniqueConstraint(
						columnNames = {"provider_id", "date", "startTime"}
				)
		}
)
@Getter
@Setter
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SlotStatus status;
}
