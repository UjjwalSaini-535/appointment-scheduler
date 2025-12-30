package com.ujjwal.appointment_scheduler.controller;

import com.ujjwal.appointment_scheduler.dto.UpdateUserRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserResponseDto;
import com.ujjwal.appointment_scheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PatchMapping("/update")
	public UserResponseDto changeUserRole(
			@RequestBody UpdateUserRequestDto request) {

		return userService.updateUser(request);
	}

	@DeleteMapping("/deactivate")
	public String deactivateUser() {
		userService.deactivateUser();

		return "User deactivated successfully";
	}

	@PatchMapping("/reactivate")
	public String reactivateUser(@RequestParam String email) {
		userService.reactivateUser(email);

		return "User reactivated successfully";
	}
}
