package com.dyns.evento.utils;

public class ClassUtils {
    public static String getName(Class<?> input) {
        var parts = input.getName().split("\\.");
        return parts[parts.length - 1];
    }
}
