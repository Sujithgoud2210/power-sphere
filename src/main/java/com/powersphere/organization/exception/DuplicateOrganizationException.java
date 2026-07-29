package com.powersphere.organization.exception;

import java.io.Serial;

public class DuplicateOrganizationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateOrganizationException(String message) {
        super(message);
    }

    public DuplicateOrganizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
