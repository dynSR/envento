package com.dyns.evento.users.dtos;

import com.dyns.evento.events.dtos.EventDto;
import com.dyns.evento.registrations.dtos.RegistrationDto;
import com.dyns.evento.roles.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String email;
    private String alias;
    private String firstName;
    private String lastName;
    private RoleName role;

    @Builder.Default
    private Set<RegistrationDto> registrations = new HashSet<>();

    @Builder.Default
    private Set<EventDto> events = new HashSet<>();
}
