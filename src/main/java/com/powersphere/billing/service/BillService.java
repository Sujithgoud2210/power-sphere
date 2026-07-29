package com.powersphere.billing.service;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.dto.request.SearchBillRequest;
import com.powersphere.billing.dto.request.UpdateBillRequest;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.dto.response.PageResponse;

/**
 * Service interface for managing electricity bills through their lifecycle
 * from generation through payment and cancellation.
 */
public interface BillService {

    /**
     * Generates a new bill from meter readings and applicable tariff plan.
     * Performs all charge calculations, applies taxes, and creates line items.
     */
    BillResponse generateBill(GenerateBillRequest request);

    /**
     * Regenerates an existing bill with updated readings or tariff.
     * Cancels the existing bill and creates a new one.
     */
    BillResponse regenerateBill(Long id, GenerateBillRequest request);

    /**
     * Retrieves a bill by its ID.
     */
    BillResponse getBill(Long id);

    /**
     * Retrieves a bill by its unique bill number.
     */
    BillResponse getBillByNumber(String billNumber);

    /**
     * Updates modifiable fields of an existing bill (remarks, discount, late fee).
     */
    BillResponse updateBill(Long id, UpdateBillRequest request);

    /**
     * Cancels a bill with a reason. Performs status transition validation.
     */
    BillResponse cancelBill(Long id, String reason);

    /**
     * Deletes a bill by its ID.
     */
    void deleteBill(Long id);

    /**
     * Searches for bills with filters, pagination, and sorting.
     */
    PageResponse<BillResponse> searchBills(SearchBillRequest request);

    /**
     * Retrieves a paginated list of all bills.
     */
    PageResponse<BillResponse> getAllBills(int page, int size);
}
