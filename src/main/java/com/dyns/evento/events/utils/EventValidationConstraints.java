package com.dyns.evento.events.utils;

import com.dyns.evento.events.Event;

public final class EventValidationConstraints {
    public static final int TITLE_MAX_LENGTH = 150;
    public static final int MIN_PARTICIPANT = 1;

    public static final String INVALID_ENDING_DATE_MESSAGE = "The ending date must be after the starting date of the event.";
    public static final String INVALID_REGISTRATION_DEADLINE_MESSAGE =  "The registration deadline must be after the starting date and before the ending date of the event.";

    public static void validateEventDates(Event input) {
        if (!input.getEndingDateTime().isAfter(input.getStartingDateTime())) {
            throw new IllegalArgumentException(EventValidationConstraints.INVALID_ENDING_DATE_MESSAGE);
        }

        if (input.getRegistrationDeadline().isBefore(input.getStartingDateTime())
                || input.getRegistrationDeadline().isAfter(input.getEndingDateTime())
        ) {
            throw new IllegalArgumentException(
                    EventValidationConstraints.INVALID_REGISTRATION_DEADLINE_MESSAGE
            );
        }
    }
}
