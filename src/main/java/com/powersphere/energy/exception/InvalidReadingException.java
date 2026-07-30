package com.powersphere.energy.exception;

public class InvalidReadingException extends RuntimeException {

    public InvalidReadingException(String message) {
        super(message);
    }

    public InvalidReadingException(String message, Object... args) {
        super(String.format(message, args));
    }
}
