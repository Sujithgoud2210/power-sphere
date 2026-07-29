package com.powersphere.energy.dto.request;

import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search/filter request DTO for energy readings")
public class EnergySearchRequest {

    @Schema(description = "Meter number to filter by", example = "MTR-001")
    private String meterNumber;

    @Schema(description = "Meter ID to filter by", example = "1")
    private Long meterId;

    @Schema(description = "Reading type filter", example = "AUTOMATIC")
    private ReadingType readingType;

    @Schema(description = "Quality status filter", example = "VALID")
    private QualityStatus qualityStatus;

    @Schema(description = "Start date for reading timestamp range")
    private LocalDateTime startDate;

    @Schema(description = "End date for reading timestamp range")
    private LocalDateTime endDate;

    @Schema(description = "Search keyword for remarks", example = "monthly")
    private String searchKeyword;

    @Schema(description = "Page number (0-based)", example = "0")
    @Builder.Default
    private int page = 0;

    @Schema(description = "Page size", example = "10")
    @Builder.Default
    private int size = 10;

    @Schema(description = "Sort field", example = "readingTimestamp")
    @Builder.Default
    private String sortBy = "readingTimestamp";

    @Schema(description = "Sort direction", example = "DESC")
    @Builder.Default
    private Sort.Direction sortDirection = Sort.Direction.DESC;
}
