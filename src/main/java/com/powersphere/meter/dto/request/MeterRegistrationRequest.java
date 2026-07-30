package com.powersphere.meter.dto.request;

import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to register a new smart meter")
public class MeterRegistrationRequest {

    @NotBlank(message = "Meter number is required")
    @Size(max = 50, message = "Meter number must not exceed 50 characters")
    @Schema(description = "Unique meter number", example = "MTR-2026-00001")
    private String meterNumber;

    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    @Schema(description = "Unique serial number", example = "SN-2026-XYZ-12345")
    private String serialNumber;

    @NotBlank(message = "Manufacturer is required")
    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    @Schema(description = "Meter manufacturer name", example = "Siemens")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model must not exceed 100 characters")
    @Schema(description = "Meter model number", example = "SM-3000X")
    private String model;

    @Size(max = 50, message = "Firmware version must not exceed 50 characters")
    @Schema(description = "Firmware version", example = "v2.1.4")
    private String firmwareVersion;

    @Schema(description = "Installation date and time")
    private LocalDateTime installationDate;

    @NotNull(message = "Meter type is required")
    @Schema(description = "Type of meter", example = "SMART")
    private MeterType meterType;

    @Schema(description = "Phase type", example = "THREE_PHASE")
    private PhaseType phaseType;

    @NotNull(message = "Connection type is required")
    @Schema(description = "Type of connection", example = "COMMERCIAL")
    private ConnectionType connectionType;

    @Schema(description = "Voltage rating", example = "240.00")
    private BigDecimal voltage;

    @Schema(description = "Current rating in amperes", example = "100.00")
    private BigDecimal currentRating;

    @Schema(description = "Maximum load capacity", example = "50.00")
    private BigDecimal maxLoad;

    @Pattern(regexp = "^[-+]?\\d{1,3}\\.?\\d{0,8}$", message = "Invalid latitude format")
    @Schema(description = "Latitude coordinate", example = "40.71280000")
    private BigDecimal latitude;

    @Pattern(regexp = "^[-+]?\\d{1,3}\\.?\\d{0,8}$", message = "Invalid longitude format")
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

    @Schema(description = "Additional remarks")
    private String remarks;

    @Schema(description = "Organization ID to associate the meter with")
    private UUID organizationId;

    @Schema(description = "Department ID for optional ownership")
    private UUID departmentId;
}
