package com.powersphere.organization.exception;

import java.io.Serial;

public class DepartmentNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
