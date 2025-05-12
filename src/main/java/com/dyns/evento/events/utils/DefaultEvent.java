package com.dyns.evento.events.utils;

import com.dyns.evento.events.enums.EventStatus;

import java.time.LocalDateTime;

public final class DefaultEvent {
    public static final String TITLE = "Default event";
    public static final String DESCRIPTION = "Default description.";
    public static final EventStatus STATUS = EventStatus.DRAFT;
    public static final Long MAX_PARTICIPANTS = 10L;
    public static final LocalDateTime STARTING_DATE_TIME = LocalDateTime.now().plusDays(5);
    public static final LocalDateTime ENDING_DATE_TIME = STARTING_DATE_TIME.plusDays(3);
    public static final LocalDateTime REGISTRATION_DEADLINE = STARTING_DATE_TIME.minusDays(2);
}
