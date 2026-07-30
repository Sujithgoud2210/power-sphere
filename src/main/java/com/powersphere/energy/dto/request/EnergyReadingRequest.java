package com.powersphere.energy.dto.request;

import com.powersphere.energy.enums.ReadingSource;
import com.powersphere.energy.enums.ReadingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating or updating an energy reading")
public class EnergyReadingRequest {

    @NotNull(message = "Meter ID is required")
    @Min(value = 1, message = "Meter ID must be a positive number")
    @Schema(description = "ID of the meter", example = "1")
    private Long meterId;

    @NotNull(message = "Reading timestamp is required")
    @PastOrPresent(message = "Reading timestamp must be in the past or present")
    @Schema(description = "Timestamp of the reading", example = "2026-07-29T10:30:00")
    private LocalDateTime readingTimestamp;

    @NotNull(message = "Reading type is required")
    @Schema(description = "Type of reading", example = "AUTOMATIC")
    private ReadingType readingType;

    @NotNull(message = "Previous reading is required")
    @DecimalMin(value = "0.000", message = "Previous reading must be non-negative")
    @Schema(description = "Previous meter reading value", example = "1000.000")
    private BigDecimal previousReading;

    @NotNull(message = "Current reading is required")
    @DecimalMin(value = "0.000", message = "Current reading must be non-negative")
    @Schema(description = "Current meter reading value", example = "1500.000")
    private BigDecimal currentReading;

    @DecimalMin(value = "0.000", message = "Voltage must be non-negative")
    @DecimalMax(value = "500000.000", message = "Voltage exceeds maximum range")
    @Schema(description = "Voltage in volts", example = "230.000")
    private BigDecimal voltage;

    @DecimalMin(value = "0.000", message = "Current must be non-negative")
    @DecimalMax(value = "10000.000", message = "Current exceeds maximum range")
    @Schema(description = "Current in amperes", example = "15.000")
    private BigDecimal current;

    @DecimalMin(value = "0.000", message = "Power factor must be non-negative")
    @DecimalMax(value = "1.000", message = "Power factor must not exceed 1.000")
    @Schema(description = "Power factor", example = "0.950")
    private BigDecimal powerFactor;

    @DecimalMin(value = "45.000", message = "Frequency must be at least 45 Hz")
    @DecimalMax(value = "65.000", message = "Frequency must not exceed 65 Hz")
    @Schema(description = "Frequency in Hz", example = "50.000")
    private BigDecimal frequency;

    @DecimalMin(value = "0.000", message = "Power must be non-negative")
    @Schema(description = "Power in watts", example = "3500.000")
    private BigDecimal power;

    @DecimalMin(value = "-50.00", message = "Temperature must be at least -50°C")
    @DecimalMax(value = "150.00", message = "Temperature must not exceed 150°C")
    @Schema(description = "Temperature in Celsius", example = "25.00")
    private BigDecimal temperature;

    @DecimalMin(value = "0.00", message = "Battery level must be non-negative")
    @DecimalMax(value = "100.00", message = "Battery level must not exceed 100%")
    @Schema(description = "Battery level in percentage", example = "85.00")
    private BigDecimal batteryLevel;

    @DecimalMin(value = "0.0", message = "Signal strength must be non-negative")
    @DecimalMax(value = "5.0", message = "Signal strength must not exceed 5.0")
    @Schema(description = "Signal strength", example = "4.5")
    private BigDecimal signalStrength;

    @NotNull(message = "Reading source is required")
    @Schema(description = "Source of the reading", example = "WEB")
    private ReadingSource readingSource;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    @Schema(description = "Additional remarks", example = "Routine monthly reading")
    private String remarks;
}
