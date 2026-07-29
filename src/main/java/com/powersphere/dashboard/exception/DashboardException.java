package com.powersphere.dashboard.exception;

public class DashboardException extends RuntimeException {

    private final int statusCode;

    public DashboardException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public DashboardException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public DashboardException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public DashboardException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
