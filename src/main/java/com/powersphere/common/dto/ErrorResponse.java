package com.powersphere.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for REST API errors.
 * Provides detailed error information for client-side handling.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response returned when an API request fails")
public class ErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status reason phrase", example = "Bad Request")
    private String error;

    @Schema(description = "Error message describing what went wrong", example = "Validation failed")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/api/v1/auth/register")
    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Schema(description = "Timestamp when the error occurred", example = "2024-01-15T10:30:00.000Z")
    private LocalDateTime timestamp;

    @Schema(description = "List of field-level validation errors (only present for 400 Bad Request)")
    private List<ValidationError> validationErrors;

    /**
     * Represents a single validation field error.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Individual field validation error details")
    public static class ValidationError implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "Name of the field that failed validation", example = "email")
        private String field;

        @Schema(description = "The rejected/invalid value", example = "invalid-email")
        private String rejectedValue;

        @Schema(description = "Validation error message", example = "must be a valid email address")
        private String message;
    }
}
