package com.dyns.evento.registrations.dtos;

import com.dyns.evento.events.dtos.EventDto;
import com.dyns.evento.registrations.enums.RegistrationStatus;
import com.dyns.evento.users.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {
    private UUID id = UUID.randomUUID();
    private LocalDateTime createdAt = LocalDateTime.now();
    private RegistrationStatus status = RegistrationStatus.CONFIRMED;
    private UserDto user;
    private EventDto event;
}
