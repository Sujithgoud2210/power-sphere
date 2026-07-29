package com.powersphere.meter.dto.response;

import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Smart meter response")
public class MeterResponse {

    @Schema(description = "Meter unique identifier")
    private UUID id;

    @Schema(description = "Unique meter number", example = "MTR-2026-00001")
    private String meterNumber;

    @Schema(description = "Unique serial number", example = "SN-2026-XYZ-12345")
    private String serialNumber;

    @Schema(description = "Meter manufacturer", example = "Siemens")
    private String manufacturer;

    @Schema(description = "Meter model", example = "SM-3000X")
    private String model;

    @Schema(description = "Firmware version", example = "v2.1.4")
    private String firmwareVersion;

    @Schema(description = "Installation date")
    private LocalDateTime installationDate;

    @Schema(description = "Activation date")
    private LocalDateTime activationDate;

    @Schema(description = "Current meter status")
    private MeterStatus status;

    @Schema(description = "Type of meter")
    private MeterType meterType;

    @Schema(description = "Phase type")
    private PhaseType phaseType;

    @Schema(description = "Connection type")
    private ConnectionType connectionType;

    @Schema(description = "Voltage rating")
    private BigDecimal voltage;

    @Schema(description = "Current rating")
    private BigDecimal currentRating;

    @Schema(description = "Maximum load")
    private BigDecimal maxLoad;

    @Schema(description = "Latitude")
    private BigDecimal latitude;

    @Schema(description = "Longitude")
    private BigDecimal longitude;

    @Schema(description = "Installation address")
    private String installationAddress;

    @Schema(description = "City")
    private String city;

    @Schema(description = "State")
    private String state;

    @Schema(description = "Country")
    private String country;

    @Schema(description = "Postal code")
    private String postalCode;

    @Schema(description = "QR code data")
    private String qrCode;

    @Schema(description = "Barcode")
    private String barcode;

    @Schema(description = "Last communication time")
    private LocalDateTime lastCommunicationTime;

    @Schema(description = "Last maintenance date")
    private LocalDateTime lastMaintenanceDate;

    @Schema(description = "Next maintenance date")
    private LocalDateTime nextMaintenanceDate;

    @Schema(description = "Remarks")
    private String remarks;

    @Schema(description = "Whether the meter is active")
    private Boolean isActive;

    @Schema(description = "Organization ID")
    private UUID organizationId;

    @Schema(description = "Organization name")
    private String organizationName;

    @Schema(description = "Assigned user ID")
    private UUID assignedUserId;

    @Schema(description = "Assigned user name")
    private String assignedUserName;

    @Schema(description = "Department ID")
    private UUID departmentId;

    @Schema(description = "Department name")
    private String departmentName;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Created by")
    private String createdBy;

    @Schema(description = "Updated by")
    private String updatedBy;
}
