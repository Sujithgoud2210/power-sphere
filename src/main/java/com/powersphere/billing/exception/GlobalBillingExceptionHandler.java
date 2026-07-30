package com.powersphere.billing.exception;

import com.powersphere.billing.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the billing module. Provides consistent
 * error responses wrapped in ApiResponse for all billing-related exceptions.
 */
@RestControllerAdvice(basePackages = "com.powersphere.billing.controller")
public class GlobalBillingExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalBillingExceptionHandler.class);

    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBillNotFound(BillNotFoundException ex) {
        log.warn("Bill not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, ex.getMessage(), ex.getErrorCode()));
    }

    @ExceptionHandler(TariffPlanNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTariffPlanNotFound(TariffPlanNotFoundException ex) {
        log.warn("Tariff plan not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, ex.getMessage(), ex.getErrorCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage(), "INVALID_ARGUMENT"));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, ex.getMessage(), "CONFLICT"));
    }

    @ExceptionHandler(BillingException.class)
    public ResponseEntity<ApiResponse<Void>> handleBillingException(BillingException ex) {
        log.error("Billing error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, ex.getMessage(), ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "An unexpected error occurred", "INTERNAL_ERROR"));
    }
}
