package com.dyns.evento.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Expected resource was not found.");
    }
}
