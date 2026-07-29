package com.powersphere.organization.exception;

public class DuplicateDepartmentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateDepartmentException(String message) {
        super(message);
    }

    public DuplicateDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
