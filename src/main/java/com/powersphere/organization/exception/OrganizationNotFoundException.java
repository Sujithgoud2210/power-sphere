package com.powersphere.organization.exception;

public class OrganizationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrganizationNotFoundException(String message) {
        super(message);
    }

    public OrganizationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
