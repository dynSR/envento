package com.dyns.evento.utils;

public final class GeneralConstraints {
    public static final String SAFE_TEXT_PATTERN = "^[a-zA-Z0-9 .-]+$";
    public static final String INVALID_SAFE_TEXT_MESSAGE = "This field can only contain letters, digits, spaces, "
            + "dots, and "
            + "dashes. Special characters are not allowed.";
}
