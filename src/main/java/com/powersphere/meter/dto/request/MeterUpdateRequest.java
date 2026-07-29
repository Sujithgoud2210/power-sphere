package com.powersphere.meter.dto.request;

import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Request to update an existing smart meter")
public class MeterUpdateRequest {

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    @Schema(description = "Meter manufacturer name", example = "Siemens")
    private String manufacturer;

    @Size(max = 100, message = "Model must not exceed 100 characters")
    @Schema(description = "Meter model number", example = "SM-4000X")
    private String model;

    @Size(max = 50, message = "Firmware version must not exceed 50 characters")
    @Schema(description = "Firmware version", example = "v2.2.0")
    private String firmwareVersion;

    @Schema(description = "Installation date and time")
    private LocalDateTime installationDate;

    @Schema(description = "Type of meter", example = "SMART")
    private MeterType meterType;

    @Schema(description = "Phase type", example = "THREE_PHASE")
    private PhaseType phaseType;

    @Schema(description = "Type of connection", example = "COMMERCIAL")
    private ConnectionType connectionType;

    @Schema(description = "Voltage rating", example = "240.00")
    private BigDecimal voltage;

    @Schema(description = "Current rating in amperes", example = "100.00")
    private BigDecimal currentRating;

    @Schema(description = "Maximum load capacity", example = "50.00")
    private BigDecimal maxLoad;

    @Schema(description = "Latitude coordinate", example = "40.71280000")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate", example = "-74.00600000")
    private BigDecimal longitude;

    @Size(max = 500, message = "Installation address must not exceed 500 characters")
    @Schema(description = "Installation address", example = "123 Main Street")
    private String installationAddress;

    @Size(max = 100, message = "City must not exceed 100 characters")
    @Schema(description = "City", example = "New York")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    @Schema(description = "State/Province", example = "NY")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Schema(description = "Country", example = "USA")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    @Schema(description = "Postal/ZIP code", example = "10001")
    private String postalCode;

    @Schema(description = "Next maintenance date")
    private LocalDateTime nextMaintenanceDate;

    @Schema(description = "Additional remarks")
    private String remarks;
}
