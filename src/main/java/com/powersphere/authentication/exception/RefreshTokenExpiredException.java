package com.powersphere.authentication.exception;

public class RefreshTokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

    public RefreshTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
