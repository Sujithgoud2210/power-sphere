package com.powersphere.organization.exception;

public class DuplicateTeamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateTeamException(String message) {
        super(message);
    }

    public DuplicateTeamException(String message, Throwable cause) {
        super(message, cause);
    }
}
