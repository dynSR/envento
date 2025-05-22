package com.dyns.evento.events.dtos;

import com.dyns.evento.events.enums.EventStatus;
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
public class EventDto {
    private UUID id;
    private String title;
    private String description;
    private EventStatus status;
    private LocalDateTime startingDateTime;
    private LocalDateTime endingDateTime;
    private LocalDateTime registrationDeadline;
    private UserDto creator;
}
