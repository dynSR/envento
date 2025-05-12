package com.dyns.evento.events.dtos;

import com.dyns.evento.events.enums.EventStatus;

import java.util.UUID;

public class EventDto {
    private UUID id;
    private String title;
    private String description;
    private EventStatus status;
//    private User creator;
//    private Set<RegistrationDto> registrations;
}
