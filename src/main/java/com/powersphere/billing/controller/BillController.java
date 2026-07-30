package com.powersphere.billing.controller;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.dto.request.SearchBillRequest;
import com.powersphere.billing.dto.request.UpdateBillRequest;
import com.powersphere.billing.dto.response.ApiResponse;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.dto.response.PageResponse;
import com.powersphere.billing.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for the complete bill management lifecycle.
 * Supports bill generation, updates, cancellation, search, and retrieval.
 */
@RestController
@RequestMapping("/api/v1/bills")
@Tag(name = "Billing", description = "Bill generation, management, cancellation, and search endpoints")
public class BillController {

    private static final Logger log = LoggerFactory.getLogger(BillController.class);

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    /**
     * POST /api/v1/bills/generate - Generate a new electricity bill.
     */
    @PostMapping("/generate")
    @Operation(summary = "Generate a new electricity bill",
            description = "Generates a new electricity bill for a specific meter and billing period. The system calculates charges based on consumption data and applicable tariff plan.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Bill generated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Resource created successfully\",\"data\":{\"id\":1,\"billNumber\":\"BILL-2024-001\",\"meterId\":1,\"billingMonth\":1,\"billingYear\":2024,\"totalAmount\":1250.50,\"status\":\"PENDING\"},\"statusCode\":201,\"timestamp\":\"2024-01-15T10:30:00\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Meter or tariff plan not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Bill already exists for the specified period")
    })
    public ResponseEntity<ApiResponse<BillResponse>> generateBill(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Bill generation request containing meter ID and billing period", required = true,
                    content = @Content(schema = @Schema(implementation = GenerateBillRequest.class)))
            @RequestBody GenerateBillRequest request) {
        log.info("REST request to generate bill: meterId={}, period={}/{}",
                request.getMeterId(), request.getBillingMonth(), request.getBillingYear());
        BillResponse response = billService.generateBill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    /**
     * POST /api/v1/bills/{id}/regenerate - Regenerate a bill with updated data.
     */
    @PostMapping("/{id}/regenerate")
    @Operation(summary = "Regenerate an existing bill",
            description = "Regenerates a bill with updated consumption data and recalculates charges. The previous bill is archived and a new version is created.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill regenerated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bill, meter, or tariff plan not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cannot regenerate bill in current status")
    })
    public ResponseEntity<ApiResponse<BillResponse>> regenerateBill(
            @Parameter(description = "Bill ID", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated bill generation request", required = true)
            @RequestBody GenerateBillRequest request) {
        log.info("REST request to regenerate bill: id={}", id);
        BillResponse response = billService.regenerateBill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bill regenerated successfully", response));
    }

    /**
     * GET /api/v1/bills - Get all bills with pagination.
     */
    @GetMapping
    @Operation(summary = "Get all bills with pagination",
            description = "Retrieves a paginated list of all bills. Use page and size parameters to control the result set.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bills retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<BillResponse>>> getAllBills(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.debug("REST request to get all bills: page={}, size={}", page, size);
        PageResponse<BillResponse> response = billService.getAllBills(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/bills/{id} - Get a bill by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get bill by ID",
            description = "Retrieves detailed bill information including line items and calculated charges by bill ID.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bill not found")
    })
    public ResponseEntity<ApiResponse<BillResponse>> getBill(
            @Parameter(description = "Bill ID", example = "1", required = true) @PathVariable Long id) {
        log.debug("REST request to get bill: id={}", id);
        BillResponse response = billService.getBill(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/bills/number/{billNumber} - Get a bill by its bill number.
     */
    @GetMapping("/number/{billNumber}")
    @Operation(summary = "Get bill by bill number",
            description = "Retrieves bill details using the unique bill number (e.g., BILL-2024-001).",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bill not found")
    })
    public ResponseEntity<ApiResponse<BillResponse>> getBillByNumber(
            @Parameter(description = "Bill number", example = "BILL-2024-001", required = true) @PathVariable String billNumber) {
        log.debug("REST request to get bill by number: {}", billNumber);
        BillResponse response = billService.getBillByNumber(billNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PUT /api/v1/bills/{id} - Update an existing bill.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing bill",
            description = "Updates bill details such as amounts, discounts, or notes. Only bills in PENDING status can be modified.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bill not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cannot update bill in current status")
    })
    public ResponseEntity<ApiResponse<BillResponse>> updateBill(
            @Parameter(description = "Bill ID", example = "1", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated bill details", required = true)
            @RequestBody UpdateBillRequest request) {
        log.info("REST request to update bill: id={}", id);
        BillResponse response = billService.updateBill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bill updated successfully", response));
    }

    /**
     * DELETE /api/v1/bills/{id} - Delete a bill.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bill (soft delete)",
            description = "Performs a soft delete on a bill, marking it as deleted without removing it from the database.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bill not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cannot delete bill in current status")
    })
    public ResponseEntity<ApiResponse<Void>> deleteBill(
            @Parameter(description = "Bill ID", example = "1", required = true) @PathVariable Long id) {
        log.info("REST request to delete bill: id={}", id);
        billService.deleteBill(id);
        return ResponseEntity.ok(ApiResponse.success("Bill deleted successfully", null));
    }

    /**
     * POST /api/v1/bills/{id}/cancel - Cancel a bill with a reason.
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a bill",
            description = "Cancels a bill with an optional reason. The bill status is updated to CANCELLED and a cancellation record is created.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bill not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cannot cancel bill in current status")
    })
    public ResponseEntity<ApiResponse<BillResponse>> cancelBill(
            @Parameter(description = "Bill ID", example = "1", required = true) @PathVariable Long id,
            @Parameter(description = "Reason for cancellation", example = "Duplicate bill generated") @RequestParam(required = false) String reason) {
        log.info("REST request to cancel bill: id={}", id);
        BillResponse response = billService.cancelBill(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Bill cancelled successfully", response));
    }

    /**
     * GET /api/v1/bills/search - Search bills with filters and pagination.
     */
    @GetMapping("/search")
    @Operation(summary = "Search bills with filters",
            description = "Advanced search for bills using multiple filters: meter ID, organization ID, status, billing period, and keyword search. Results are paginated and sortable.",
            tags = {"Billing"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<BillResponse>>> searchBills(
            @Parameter(description = "Filter by meter ID", example = "1") @RequestParam(required = false) Long meterId,
            @Parameter(description = "Filter by organization ID", example = "550e8400-e29b-41d4-a716-446655440000") @RequestParam(required = false) Long organizationId,
            @Parameter(description = "Filter by bill status (PENDING, PAID, OVERDUE, CANCELLED)", example = "PENDING") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by billing month (1-12)", example = "1") @RequestParam(required = false) Integer billingMonth,
            @Parameter(description = "Filter by billing year", example = "2024") @RequestParam(required = false) Integer billingYear,
            @Parameter(description = "Search keyword in bill number or notes", example = "BILL-2024") @RequestParam(required = false) String query,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field (generatedDate, totalAmount, billingMonth, billingYear, status)", example = "generatedDate") @RequestParam(defaultValue = "generatedDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC") @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.debug("REST request to search bills");
        SearchBillRequest searchRequest = new SearchBillRequest();
        searchRequest.setMeterId(meterId);
        searchRequest.setOrganizationId(organizationId);
        if (status != null) {
            searchRequest.setStatus(com.powersphere.billing.enums.BillStatus.valueOf(status));
        }
        searchRequest.setBillingMonth(billingMonth);
        searchRequest.setBillingYear(billingYear);
        searchRequest.setQuery(query);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<BillResponse> response = billService.searchBills(searchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
