package com.powersphere.energy.dto.response;

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
@Schema(description = "Response DTO for calculated consumption")
public class ConsumptionResponse {

    @Schema(description = "Meter ID", example = "1")
    private Long meterId;

    @Schema(description = "Previous reading value", example = "1000.000")
    private BigDecimal previousReading;

    @Schema(description = "Current reading value", example = "1500.000")
    private BigDecimal currentReading;

    @Schema(description = "Calculated consumption", example = "500.000")
    private BigDecimal consumption;

    @Schema(description = "Percentage change from previous reading", example = "50.00")
    private BigDecimal percentageChange;

    @Schema(description = "Timestamp of the previous reading")
    private LocalDateTime previousReadingTimestamp;

    @Schema(description = "Timestamp of the current reading")
    private LocalDateTime currentReadingTimestamp;

    @Schema(description = "Unit of measurement", example = "kWh")
    private String unit;
}
