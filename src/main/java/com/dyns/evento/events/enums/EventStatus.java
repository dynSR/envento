package com.dyns.evento.events.enums;

public enum EventStatus {
    /**
     * Default status — not visible by users.
     */
    DRAFT,

    /**
     * Visible by users.
     */
    PUBLISHED,

    /**
     * Deadline reached, maximum number reached or cancelled by creator.
     */
    CLOSED,

    /**
     * Completed after its end date.
     */
    FINISHED
}
