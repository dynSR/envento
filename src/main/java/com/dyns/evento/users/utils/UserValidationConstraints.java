package com.dyns.evento.users.utils;

/**
 * @see
 * <a href="https://owasp.org/www-community/OWASP_Validation_Regex_Repository">OWASP Validation Regex Repository</a>
 */
public final class UserValidationConstraints {
    public static final int ALIAS_MAX_LENGTH = 50;
    public static final int FIRST_NAME_MAX_LENGTH = 80;
    public static final int LAST_NAME_MAX_LENGTH = 150;
    public static final int PASSWORD_MIN_LENGTH = 12;
    public static final int PASSWORD_MAX_LENGTH = 128;

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    public static final String PASSWORD_PATTERN = "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[^A-Za-z0-9])(?=" +
            ".*[a-z])|(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))(?!.*(.)\\1{2," +
            "})[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()\\|\\[\\]\\{\\}\\-\\$\\^\\@\\/]{12,128}$";

    public static final String INVALID_EMAIL_MESSAGE = "The email address format is invalid. It must follow the format " +
            "example@domain.com and may only include letters, digits, dots, dashes, or symbols like (_ + & * -)";
    public static final String INVALID_PASSWORD_PATTERN_MESSAGE = "The password must be between 12 and 128 characters long " +
            "and include at least three of the following: an uppercase letter, a lowercase letter, a digit, and a " +
            "special character. No more than two identical characters can appear consecutively.";
}
