package com.ujjwal.appointment_scheduler.dto;

import com.ujjwal.appointment_scheduler.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
