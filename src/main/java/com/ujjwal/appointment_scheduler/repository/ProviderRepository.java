package com.ujjwal.appointment_scheduler.repository;

import com.ujjwal.appointment_scheduler.entity.Provider;
import com.ujjwal.appointment_scheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByUser(User user);

    boolean existsByUser(User user);
}