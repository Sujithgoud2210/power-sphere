package com.powersphere.organization.dto.response;

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
@Schema(description = "Organization response containing full organization details")
public class OrganizationResponse {

    @Schema(description = "Unique organization identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Unique organization code", example = "ACME-CORP")
    private String organizationCode;

    @Schema(description = "Full organization name", example = "Acme Corporation")
    private String organizationName;

    @Schema(description = "Business registration number", example = "REG-2024-001")
    private String registrationNumber;

    @Schema(description = "Tax identification number", example = "TAX-12345")
    private String taxNumber;

    @Schema(description = "Industry sector", example = "Energy & Utilities")
    private String industry;

    @Schema(description = "Organization website URL", example = "https://www.acmecorp.com")
    private String website;

    @Schema(description = "Organization email address", example = "contact@acmecorp.com")
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Organization status", example = "ACTIVE")
    private String status;

    @Schema(description = "Primary address line", example = "123 Business Park Drive")
    private String addressLine1;

    @Schema(description = "Secondary address line", example = "Suite 400")
    private String addressLine2;

    @Schema(description = "City", example = "New York")
    private String city;

    @Schema(description = "State or province", example = "NY")
    private String state;

    @Schema(description = "Country", example = "USA")
    private String country;

    @Schema(description = "Postal or ZIP code", example = "10001")
    private String postalCode;

    @Schema(description = "Timezone", example = "America/New_York")
    private String timezone;

    @Schema(description = "Three-letter ISO currency code", example = "USD")
    private String currency;

    @Schema(description = "URL to organization logo", example = "https://cdn.acmecorp.com/logo.png")
    private String logoUrl;

    @Schema(description = "Whether the organization is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Created by user", example = "admin")
    private String createdBy;

    @Schema(description = "Last updated by user", example = "admin")
    private String updatedBy;
}
