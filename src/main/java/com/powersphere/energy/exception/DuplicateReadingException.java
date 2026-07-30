package com.powersphere.energy.exception;

public class DuplicateReadingException extends RuntimeException {

    public DuplicateReadingException(String message) {
        super(message);
    }

    public DuplicateReadingException(Long meterId, String timestamp) {
        super("Duplicate reading detected for meter ID " + meterId + " at timestamp " + timestamp);
    }
}
