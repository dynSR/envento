package com.dyns.evento.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
//    private Set<RegistrationDto> registrations = new HashSet<>();
//    private Set<EventDto> events = new HashSet<>();
}
