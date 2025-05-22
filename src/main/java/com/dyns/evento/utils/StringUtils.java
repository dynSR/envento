package com.dyns.evento.utils;

public class StringUtils {
    public static String capitalized(String input) {
        if (isNullOrEmpty(input)) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }
}
