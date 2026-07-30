package com.powersphere.users.exception;

public class UserProfileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserProfileNotFoundException(String message) {
        super(message);
    }

    public UserProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
