package com.powersphere.energy.dto.response;

import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingSource;
import com.powersphere.energy.enums.ReadingType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response DTO for energy reading")
public class EnergyReadingResponse {

    @Schema(description = "Reading ID", example = "1")
    private Long id;

    @Schema(description = "Meter ID", example = "1")
    private Long meterId;

    @Schema(description = "Timestamp of the reading", example = "2026-07-29T10:30:00")
    private LocalDateTime readingTimestamp;

    @Schema(description = "Type of reading", example = "AUTOMATIC")
    private ReadingType readingType;

    @Schema(description = "Previous meter reading", example = "1000.000")
    private BigDecimal previousReading;

    @Schema(description = "Current meter reading", example = "1500.000")
    private BigDecimal currentReading;

    @Schema(description = "Calculated consumption", example = "500.000")
    private BigDecimal consumption;

    @Schema(description = "Voltage in volts", example = "230.000")
    private BigDecimal voltage;

    @Schema(description = "Current in amperes", example = "15.000")
    private BigDecimal current;

    @Schema(description = "Power factor", example = "0.950")
    private BigDecimal powerFactor;

    @Schema(description = "Frequency in Hz", example = "50.000")
    private BigDecimal frequency;

    @Schema(description = "Power in watts", example = "3500.000")
    private BigDecimal power;

    @Schema(description = "Temperature in Celsius", example = "25.00")
    private BigDecimal temperature;

    @Schema(description = "Battery level in percentage", example = "85.00")
    private BigDecimal batteryLevel;

    @Schema(description = "Signal strength", example = "4.5")
    private BigDecimal signalStrength;

    @Schema(description = "Source of the reading", example = "WEB")
    private ReadingSource readingSource;

    @Schema(description = "Quality status of the reading", example = "VALID")
    private QualityStatus qualityStatus;

    @Schema(description = "Additional remarks", example = "Routine monthly reading")
    private String remarks;

    @Schema(description = "Whether the reading is active", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Created by user")
    private String createdBy;

    @Schema(description = "Last updated by user")
    private String updatedBy;
}
