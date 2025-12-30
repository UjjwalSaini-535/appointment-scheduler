package com.ujjwal.appointment_scheduler.controller;

import com.ujjwal.appointment_scheduler.dto.LoginRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.UserResponseDto;
import com.ujjwal.appointment_scheduler.entity.User;
import com.ujjwal.appointment_scheduler.exception.UnauthorizedException;
import com.ujjwal.appointment_scheduler.repository.UserRepository;
import com.ujjwal.appointment_scheduler.security.JwtUtil;
import com.ujjwal.appointment_scheduler.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final UserService userService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createUser(
			@Valid @RequestBody UserCreateRequestDto request) {

		return userService.createUser(request);
	}

	@PostMapping("/login")
	public String login(
			@Valid @RequestBody LoginRequestDto request,
			HttpServletResponse response) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("Invalid credentials");
		}

		String token = jwtUtil.generateToken(
				user.getId(),
				user.getRole().name()
		);

		Cookie cookie = new Cookie("JWT_TOKEN", token);
		cookie.setHttpOnly(true);     // JS cannot access
		cookie.setSecure(false);      // true in production (HTTPS)
		cookie.setPath("/");
		cookie.setMaxAge(24 * 60 * 60); // 1 day

		response.addCookie(cookie);

		return "Login successful";
	}

	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {

		Cookie cookie = new Cookie("JWT_TOKEN", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);   // true in prod (HTTPS)
		cookie.setPath("/");
		cookie.setMaxAge(0);       // ⬅️ delete cookie

		response.addCookie(cookie);

		return "Logout successful";
	}
}
