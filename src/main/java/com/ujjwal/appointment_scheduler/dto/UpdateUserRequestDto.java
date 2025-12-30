package com.ujjwal.appointment_scheduler.dto;

import com.ujjwal.appointment_scheduler.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequestDto {
	private Role newRole;
	private String name;
}
