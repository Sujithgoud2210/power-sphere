package com.powersphere.organization.exception;

import java.io.Serial;

public class TeamNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
