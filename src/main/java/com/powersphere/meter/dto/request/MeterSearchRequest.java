package com.powersphere.meter.dto.request;

import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search/filter request for smart meters")
public class MeterSearchRequest {

    @Schema(description = "Search keyword for meter number, serial number, manufacturer, model")
    private String searchTerm;

    @Schema(description = "Filter by meter status", example = "ACTIVE")
    private MeterStatus status;

    @Schema(description = "Filter by meter type", example = "SMART")
    private MeterType meterType;

    @Schema(description = "Filter by phase type", example = "THREE_PHASE")
    private PhaseType phaseType;

    @Schema(description = "Filter by connection type", example = "COMMERCIAL")
    private ConnectionType connectionType;

    @Schema(description = "Filter by organization ID")
    private UUID organizationId;

    @Schema(description = "Filter by assigned user ID")
    private UUID assignedUserId;

    @Schema(description = "Filter meters installed on or after this date")
    private LocalDateTime installationDateFrom;

    @Schema(description = "Filter meters installed on or before this date")
    private LocalDateTime installationDateTo;

    @Schema(description = "Filter by city")
    private String city;

    @Schema(description = "Filter by state")
    private String state;

    @Schema(description = "Filter by country")
    private String country;

    @Schema(description = "Page number (0-based)", example = "0")
    private int page;

    @Schema(description = "Page size", example = "10")
    private int size;

    @Schema(description = "Sort field", example = "createdAt")
    private String sortBy;

    @Schema(description = "Sort direction (ASC/DESC)", example = "DESC")
    private String sortDirection;
}
