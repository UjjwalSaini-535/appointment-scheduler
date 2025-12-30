package com.ujjwal.appointment_scheduler.entity;

import com.ujjwal.appointment_scheduler.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	private LocalDateTime createdAt;

	private Boolean isActive;

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.isActive = true;
	}
}
