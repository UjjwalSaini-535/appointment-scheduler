package com.ujjwal.appointment_scheduler.service;

import com.ujjwal.appointment_scheduler.dto.UpdateUserRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserResponseDto;

public interface UserService {
	UserResponseDto createUser(UserCreateRequestDto request);

	UserResponseDto updateUser(UpdateUserRequestDto request);

	void deactivateUser();

	void reactivateUser(String email);
}
