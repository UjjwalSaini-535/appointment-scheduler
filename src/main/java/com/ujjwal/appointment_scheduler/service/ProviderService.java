package com.ujjwal.appointment_scheduler.service;

import com.ujjwal.appointment_scheduler.dto.ProviderCreateRequestDto;
import com.ujjwal.appointment_scheduler.dto.ProviderResponseDto;

public interface ProviderService {
    ProviderResponseDto createProvider(ProviderCreateRequestDto request);
}
