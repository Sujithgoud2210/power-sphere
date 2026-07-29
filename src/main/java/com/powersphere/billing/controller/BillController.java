package com.powersphere.billing.controller;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.dto.request.SearchBillRequest;
import com.powersphere.billing.dto.request.UpdateBillRequest;
import com.powersphere.billing.dto.response.ApiResponse;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.dto.response.PageResponse;
import com.powersphere.billing.service.BillService;
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
    public ResponseEntity<ApiResponse<BillResponse>> generateBill(
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
    public ResponseEntity<ApiResponse<BillResponse>> regenerateBill(
            @PathVariable Long id,
            @RequestBody GenerateBillRequest request) {
        log.info("REST request to regenerate bill: id={}", id);
        BillResponse response = billService.regenerateBill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bill regenerated successfully", response));
    }

    /**
     * GET /api/v1/bills - Get all bills with pagination.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BillResponse>>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("REST request to get all bills: page={}, size={}", page, size);
        PageResponse<BillResponse> response = billService.getAllBills(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/bills/{id} - Get a bill by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponse>> getBill(@PathVariable Long id) {
        log.debug("REST request to get bill: id={}", id);
        BillResponse response = billService.getBill(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/bills/number/{billNumber} - Get a bill by its bill number.
     */
    @GetMapping("/number/{billNumber}")
    public ResponseEntity<ApiResponse<BillResponse>> getBillByNumber(@PathVariable String billNumber) {
        log.debug("REST request to get bill by number: {}", billNumber);
        BillResponse response = billService.getBillByNumber(billNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PUT /api/v1/bills/{id} - Update an existing bill.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponse>> updateBill(
            @PathVariable Long id,
            @RequestBody UpdateBillRequest request) {
        log.info("REST request to update bill: id={}", id);
        BillResponse response = billService.updateBill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bill updated successfully", response));
    }

    /**
     * DELETE /api/v1/bills/{id} - Delete a bill.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBill(@PathVariable Long id) {
        log.info("REST request to delete bill: id={}", id);
        billService.deleteBill(id);
        return ResponseEntity.ok(ApiResponse.success("Bill deleted successfully", null));
    }

    /**
     * POST /api/v1/bills/{id}/cancel - Cancel a bill with a reason.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BillResponse>> cancelBill(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        log.info("REST request to cancel bill: id={}", id);
        BillResponse response = billService.cancelBill(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Bill cancelled successfully", response));
    }

    /**
     * GET /api/v1/bills/search - Search bills with filters and pagination.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<BillResponse>>> searchBills(
            @RequestParam(required = false) Long meterId,
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer billingMonth,
            @RequestParam(required = false) Integer billingYear,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "generatedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
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
