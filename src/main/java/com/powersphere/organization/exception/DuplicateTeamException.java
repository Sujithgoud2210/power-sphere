package com.powersphere.organization.exception;

import java.io.Serial;

public class DuplicateTeamException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateTeamException(String message) {
        super(message);
    }

    public DuplicateTeamException(String message, Throwable cause) {
        super(message, cause);
    }
}
