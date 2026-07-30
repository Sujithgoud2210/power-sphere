package com.powersphere.meter.exception;

public class InvalidMeterStateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidMeterStateException(String message) {
        super(message);
    }

    public InvalidMeterStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
