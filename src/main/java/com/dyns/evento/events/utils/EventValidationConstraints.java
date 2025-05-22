package com.dyns.evento.events.utils;

public final class EventValidationConstraints {
    public static final int TITLE_MAX_LENGTH = 150;
    public static final int MIN_PARTICIPANT = 1;

    public static final String INVALID_ENDING_DATE_MESSAGE = "The ending date must be after the starting date of the event.";
    public static final String INVALID_REGISTRATION_DEADLINE_MESSAGE =  "The registration deadline must be after the starting date and before the ending date of the event.";
}
