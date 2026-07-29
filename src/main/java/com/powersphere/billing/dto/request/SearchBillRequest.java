package com.powersphere.billing.dto.request;

import com.powersphere.billing.enums.BillStatus;

/**
 * Request DTO for searching/filtering bills with pagination and sorting support.
 */
public class SearchBillRequest {

    private Long meterId;
    private Long organizationId;
    private BillStatus status;
    private Integer billingMonth;
    private Integer billingYear;
    private String query;
    private int page = 0;
    private int size = 20;
    private String sortBy = "generatedDate";
    private String sortDirection = "DESC";

    // --- Getters and Setters ---

    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }

    public Integer getBillingMonth() { return billingMonth; }
    public void setBillingMonth(Integer billingMonth) { this.billingMonth = billingMonth; }

    public Integer getBillingYear() { return billingYear; }
    public void setBillingYear(Integer billingYear) { this.billingYear = billingYear; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}
