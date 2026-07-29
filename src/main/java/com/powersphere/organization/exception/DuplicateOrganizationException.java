package com.powersphere.organization.exception;

public class DuplicateOrganizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateOrganizationException(String message) {
        super(message);
    }

    public DuplicateOrganizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
