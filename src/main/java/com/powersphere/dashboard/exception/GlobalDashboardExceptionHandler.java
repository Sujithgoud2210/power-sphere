package com.powersphere.dashboard.exception;

import com.powersphere.dashboard.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(basePackages = "com.powersphere.dashboard")
public class GlobalDashboardExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalDashboardExceptionHandler.class);

    @ExceptionHandler(DashboardException.class)
    public ResponseEntity<ApiResponse<Void>> handleDashboardException(DashboardException ex, WebRequest request) {
        log.error("Dashboard exception: {}", ex.getMessage(), ex);
        ApiResponse<Void> response = ApiResponse.error(ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParams(MissingServletRequestParameterException ex) {
        log.warn("Missing request parameter: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "Missing required parameter: " + ex.getParameterName()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Unexpected error in dashboard module: {}", ex.getMessage(), ex);
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
