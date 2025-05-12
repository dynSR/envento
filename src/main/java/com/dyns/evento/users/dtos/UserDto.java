package com.dyns.evento.users.dtos;

import com.dyns.evento.users.utils.DefaultUser;
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
    private UUID id = UUID.randomUUID();
    private String email = DefaultUser.EMAIL;
    private String firstName = DefaultUser.FIRST_NAME;
    private String lastName = DefaultUser.LAST_NAME;
//    private List<RegistrationDto> registrations = new ArrayList<>();
//    private List<EventDto> events = new ArrayList<>();
}
