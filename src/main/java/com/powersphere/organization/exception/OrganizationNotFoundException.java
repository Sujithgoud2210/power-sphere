package com.powersphere.organization.exception;

import java.io.Serial;

public class OrganizationNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OrganizationNotFoundException(String message) {
        super(message);
    }

    public OrganizationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
