package com.powersphere.organization.exception;

public class TeamNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
