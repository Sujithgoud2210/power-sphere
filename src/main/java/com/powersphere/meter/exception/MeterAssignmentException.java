package com.powersphere.meter.exception;

public class MeterAssignmentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MeterAssignmentException(String message) {
        super(message);
    }

    public MeterAssignmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
