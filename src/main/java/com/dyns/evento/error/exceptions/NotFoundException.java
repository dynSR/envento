package com.dyns.evento.error.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Expected resource was not found.");
    }

    public NotFoundException(String objectClassName) {
        super(String.format("Expected %s was not found.", objectClassName));
    }
}
