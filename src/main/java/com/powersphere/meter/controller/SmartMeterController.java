package com.powersphere.meter.controller;

import com.powersphere.common.dto.ApiResponse;
import com.powersphere.meter.dto.request.*;
import com.powersphere.meter.dto.response.MeterResponse;
import com.powersphere.meter.service.MeterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meters")
@RequiredArgsConstructor
@Tag(name = "Smart Meter", description = "Smart meter management APIs")
public class SmartMeterController {

    private static final Logger log = LoggerFactory.getLogger(SmartMeterController.class);

    private final MeterService meterService;

    @PostMapping
    @Operation(summary = "Register a new smart meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Meter registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate meter number or serial number")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> registerMeter(
            @Valid @RequestBody MeterRegistrationRequest request) {
        log.info("REST request to register new meter: {}", request.getMeterNumber());
        MeterResponse response = meterService.registerMeter(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Smart meter registered successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing smart meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> updateMeter(
            @Parameter(description = "Meter UUID") @PathVariable UUID id,
            @Valid @RequestBody MeterUpdateRequest request) {
        log.info("REST request to update meter: {}", id);
        MeterResponse response = meterService.updateMeter(id, request);
        return ResponseEntity.ok(ApiResponse.success("Smart meter updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a smart meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteMeter(
            @Parameter(description = "Meter UUID") @PathVariable UUID id) {
        log.info("REST request to delete meter: {}", id);
        meterService.deleteMeter(id);
        return ResponseEntity.ok(ApiResponse.success("Smart meter deleted successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all smart meters with pagination and sorting")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful retrieval")
    })
    public ResponseEntity<ApiResponse<Page<MeterResponse>>> getAllMeters(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("REST request to get all meters - page: {}, size: {}", page, size);
        Page<MeterResponse> response = meterService.getAllMeters(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success("Meters retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get smart meter by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> getMeterById(
            @Parameter(description = "Meter UUID") @PathVariable UUID id) {
        log.info("REST request to get meter by id: {}", id);
        MeterResponse response = meterService.getMeterById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search smart meters by keyword")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results")
    })
    public ResponseEntity<ApiResponse<Page<MeterResponse>>> searchMeters(
            @Parameter(description = "Search keyword (meter number, serial number, manufacturer, model)")
            @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("REST request to search meters with query: {}", q);
        Page<MeterResponse> response = meterService.searchMeters(q, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", response));
    }

    @PostMapping("/filter")
    @Operation(summary = "Filter smart meters with dynamic criteria")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Filter results")
    })
    public ResponseEntity<ApiResponse<Page<MeterResponse>>> filterMeters(
            @Valid @RequestBody MeterSearchRequest searchRequest) {
        log.info("REST request to filter meters");
        Page<MeterResponse> response = meterService.filterMeters(searchRequest);
        return ResponseEntity.ok(ApiResponse.success("Filter results retrieved successfully", response));
    }

    @GetMapping("/number/{meterNumber}")
    @Operation(summary = "Find smart meter by meter number")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> getMeterByNumber(
            @Parameter(description = "Meter number") @PathVariable String meterNumber) {
        log.info("REST request to get meter by number: {}", meterNumber);
        MeterResponse response = meterService.getMeterByMeterNumber(meterNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/serial/{serialNumber}")
    @Operation(summary = "Find smart meter by serial number")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> getMeterBySerialNumber(
            @Parameter(description = "Serial number") @PathVariable String serialNumber) {
        log.info("REST request to get meter by serial number: {}", serialNumber);
        MeterResponse response = meterService.getMeterBySerialNumber(serialNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a smart meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid meter state"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> activateMeter(
            @Parameter(description = "Meter UUID") @PathVariable UUID id) {
        log.info("REST request to activate meter: {}", id);
        MeterResponse response = meterService.activateMeter(id);
        return ResponseEntity.ok(ApiResponse.success("Smart meter activated successfully", response));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a smart meter")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid meter state"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> deactivateMeter(
            @Parameter(description = "Meter UUID") @PathVariable UUID id) {
        log.info("REST request to deactivate meter: {}", id);
        MeterResponse response = meterService.deactivateMeter(id);
        return ResponseEntity.ok(ApiResponse.success("Smart meter deactivated successfully", response));
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign a smart meter to a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid meter state or assignment error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter or user not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> assignMeter(
            @Parameter(description = "Meter UUID") @PathVariable UUID id,
            @Valid @RequestBody AssignMeterRequest request) {
        log.info("REST request to assign meter {} to user {}", id, request.getUserId());
        MeterResponse response = meterService.assignMeter(id, request);
        return ResponseEntity.ok(ApiResponse.success("Smart meter assigned successfully", response));
    }

    @PutMapping("/{id}/transfer")
    @Operation(summary = "Transfer a smart meter from one user to another")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter transferred successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid transfer request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter or user not found")
    })
    public ResponseEntity<ApiResponse<MeterResponse>> transferMeter(
            @Parameter(description = "Meter UUID") @PathVariable UUID id,
            @Valid @RequestBody TransferMeterRequest request) {
        log.info("REST request to transfer meter {} from user {} to user {}",
                id, request.getCurrentUserId(), request.getNewUserId());
        MeterResponse response = meterService.transferMeter(id, request);
        return ResponseEntity.ok(ApiResponse.success("Smart meter transferred successfully", response));
    }
}
