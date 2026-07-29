package com.powersphere.meter.exception;

public class DuplicateMeterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateMeterException(String message) {
        super(message);
    }

    public DuplicateMeterException(String message, Throwable cause) {
        super(message, cause);
    }
}
