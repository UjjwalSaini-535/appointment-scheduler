package com.ujjwal.appointment_scheduler.entity;

import com.ujjwal.appointment_scheduler.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "slot_id")
	private Slot slot;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus status;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
