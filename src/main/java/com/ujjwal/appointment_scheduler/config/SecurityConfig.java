package com.ujjwal.appointment_scheduler.config;

import com.ujjwal.appointment_scheduler.enums.Role;
import com.ujjwal.appointment_scheduler.security.CustomAccessDeniedHandler;
import com.ujjwal.appointment_scheduler.security.JwtAuthFilter;
import com.ujjwal.appointment_scheduler.security.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	public SecurityConfig(
			JwtAuthFilter jwtAuthFilter,
			JwtAuthenticationEntryPoint authenticationEntryPoint,
			CustomAccessDeniedHandler accessDeniedHandler) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.httpBasic(basic -> basic.disable())
				.formLogin(form -> form.disable())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				).exceptionHandling(ex ->
						ex
								.authenticationEntryPoint(authenticationEntryPoint)
								.accessDeniedHandler(accessDeniedHandler)
				).authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/users/reactivate").permitAll()
						.requestMatchers("/api/availability/**").hasRole(String.valueOf(Role.PROVIDER))
						.requestMatchers("/api/providers/**").hasRole(String.valueOf(Role.PROVIDER))
						.requestMatchers("/api/slots/**").hasRole(String.valueOf(Role.PROVIDER))
						.requestMatchers("/api/bookings/**").hasRole(String.valueOf(Role.USER))
						.anyRequest().authenticated()
				)
				.addFilterBefore(
						jwtAuthFilter,
						UsernamePasswordAuthenticationFilter.class
				);

		return http.build();
	}
}
