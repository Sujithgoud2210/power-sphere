package com.powersphere.meter.exception;

public class MeterNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MeterNotFoundException(String message) {
        super(message);
    }

    public MeterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
