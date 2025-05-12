package com.dyns.evento.registrations.utils;

import com.dyns.evento.registrations.enums.RegistrationStatus;

import java.time.LocalDateTime;

public final class DefaultRegistration {
    public static final RegistrationStatus STATUS = RegistrationStatus.CONFIRMED;
    public static final LocalDateTime CREATED_AT = LocalDateTime.now();
}
