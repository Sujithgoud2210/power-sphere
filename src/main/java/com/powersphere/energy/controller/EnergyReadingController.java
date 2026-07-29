package com.powersphere.energy.controller;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.dto.request.EnergySearchRequest;
import com.powersphere.energy.dto.response.ConsumptionResponse;
import com.powersphere.energy.dto.response.EnergyReadingResponse;
import com.powersphere.energy.service.ConsumptionService;
import com.powersphere.energy.service.EnergyReadingService;
import com.powersphere.shared.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/energy-readings")
@RequiredArgsConstructor
@Tag(name = "Energy Readings", description = "Energy reading management APIs for CRUD operations, search, filter, and consumption calculation")
public class EnergyReadingController {

    private final EnergyReadingService energyReadingService;
    private final ConsumptionService consumptionService;

    @PostMapping
    @Operation(summary = "Create energy reading", description = "Creates a new energy reading after validating reading order, duplicate detection, and field ranges")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Energy reading created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate reading detected")
    })
    public ResponseEntity<ApiResponse<EnergyReadingResponse>> createReading(
            @Valid @RequestBody EnergyReadingRequest request) {
        EnergyReadingResponse response = energyReadingService.createReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update energy reading", description = "Updates an existing energy reading by ID with full re-validation")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Energy reading updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Energy reading not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate reading detected")
    })
    public ResponseEntity<ApiResponse<EnergyReadingResponse>> updateReading(
            @Parameter(description = "Reading ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EnergyReadingRequest request) {
        EnergyReadingResponse response = energyReadingService.updateReading(id, request);
        return ResponseEntity.ok(ApiResponse.success("Energy reading updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete energy reading (soft delete)", description = "Performs a soft delete by marking the reading as inactive")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Energy reading deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Energy reading not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteReading(
            @Parameter(description = "Reading ID", example = "1")
            @PathVariable Long id) {
        energyReadingService.deleteReading(id);
        return ResponseEntity.ok(ApiResponse.success("Energy reading deleted successfully", null));
    }

    @GetMapping
    @Operation(summary = "List energy readings", description = "Retrieves a paginated list of all active energy readings, sorted by timestamp descending")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Energy readings retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<EnergyReadingResponse>>> listReadings(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<EnergyReadingResponse> readings = energyReadingService.listReadings(page, size);
        return ResponseEntity.ok(ApiResponse.success(readings));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get energy reading by ID", description = "Retrieves a single energy reading by its unique identifier")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Energy reading retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Energy reading not found")
    })
    public ResponseEntity<ApiResponse<EnergyReadingResponse>> getReading(
            @Parameter(description = "Reading ID", example = "1")
            @PathVariable Long id) {
        EnergyReadingResponse response = energyReadingService.getReading(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/latest/{meterId}")
    @Operation(summary = "Get latest reading by meter ID", description = "Retrieves the most recent energy reading for a specific meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Latest reading retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No readings found for the meter")
    })
    public ResponseEntity<ApiResponse<EnergyReadingResponse>> getLatestReading(
            @Parameter(description = "Meter ID", example = "1")
            @PathVariable Long meterId) {
        EnergyReadingResponse response = energyReadingService.getLatestReading(meterId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/history/{meterId}")
    @Operation(summary = "Get reading history by meter ID", description = "Retrieves all energy readings for a specific meter, ordered by timestamp descending")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reading history retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No readings found for the meter")
    })
    public ResponseEntity<ApiResponse<List<EnergyReadingResponse>>> getReadingHistory(
            @Parameter(description = "Meter ID", example = "1")
            @PathVariable Long meterId) {
        List<EnergyReadingResponse> history = energyReadingService.getReadingHistory(meterId);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @GetMapping("/search")
    @Operation(summary = "Search and filter energy readings", description = "Advanced search with multiple filters: date range, meter ID, reading type, quality status, and keyword search in remarks. Supports pagination and sorting.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<EnergyReadingResponse>>> searchReadings(
            @Parameter(description = "Meter ID filter") @RequestParam(required = false) Long meterId,
            @Parameter(description = "Reading type filter") @RequestParam(required = false) String readingType,
            @Parameter(description = "Quality status filter") @RequestParam(required = false) String qualityStatus,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) LocalDateTime endDate,
            @Parameter(description = "Search keyword in remarks") @RequestParam(required = false) String searchKeyword,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (e.g., readingTimestamp, consumption, createdAt)", example = "readingTimestamp")
            @RequestParam(defaultValue = "readingTimestamp") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        EnergySearchRequest searchRequest = EnergySearchRequest.builder()
                .meterId(meterId)
                .readingType(com.powersphere.energy.util.EnumUtils.safeParseEnum(
                        com.powersphere.energy.enums.ReadingType.class, readingType))
                .qualityStatus(com.powersphere.energy.util.EnumUtils.safeParseEnum(
                        com.powersphere.energy.enums.QualityStatus.class, qualityStatus))
                .startDate(startDate)
                .endDate(endDate)
                .searchKeyword(searchKeyword)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection("ASC".equalsIgnoreCase(sortDirection)
                        ? org.springframework.data.domain.Sort.Direction.ASC
                        : org.springframework.data.domain.Sort.Direction.DESC)
                .build();

        Page<EnergyReadingResponse> results = energyReadingService.searchReadings(searchRequest);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @GetMapping("/consumption/{meterId}")
    @Operation(summary = "Get consumption for a meter", description = "Calculates consumption for a specific meter based on the latest reading")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Consumption calculated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No readings found for the meter")
    })
    public ResponseEntity<ApiResponse<ConsumptionResponse>> getConsumption(
            @Parameter(description = "Meter ID", example = "1")
            @PathVariable Long meterId) {
        ConsumptionResponse response = consumptionService.getLatestConsumption(meterId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/consumption-range/{meterId}")
    @Operation(summary = "Get consumption between dates", description = "Calculates total consumption for a meter within a specific date range")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Consumption calculated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No readings found in the date range")
    })
    public ResponseEntity<ApiResponse<ConsumptionResponse>> getConsumptionBetween(
            @Parameter(description = "Meter ID", example = "1")
            @PathVariable Long meterId,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam LocalDateTime endDate) {
        ConsumptionResponse response = consumptionService.calculateConsumptionBetween(
                meterId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/consumption-history/{meterId}")
    @Operation(summary = "Get consumption history", description = "Retrieves historical consumption data for a meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Consumption history retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No consumption history found")
    })
    public ResponseEntity<ApiResponse<List<ConsumptionResponse>>> getConsumptionHistory(
            @Parameter(description = "Meter ID", example = "1")
            @PathVariable Long meterId) {
        List<ConsumptionResponse> history = consumptionService.getConsumptionHistory(meterId);
        return ResponseEntity.ok(ApiResponse.success(history));
    }
}
