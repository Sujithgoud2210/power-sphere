package com.powersphere.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating or updating an organization")
public class OrganizationRequest {

    @NotBlank(message = "Organization code is required")
    @Size(min = 2, max = 20, message = "Organization code must be between {min} and {max} characters")
    @Schema(description = "Unique organization code identifier", example = "ACME-CORP", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 20)
    private String organizationCode;

    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 150, message = "Organization name must be between {min} and {max} characters")
    @Schema(description = "Full organization name", example = "Acme Corporation", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 150)
    private String organizationName;

    @Size(max = 50, message = "Registration number must not exceed {max} characters")
    @Schema(description = "Business registration number", example = "REG-2024-001", maxLength = 50)
    private String registrationNumber;

    @Size(max = 50, message = "Tax number must not exceed {max} characters")
    @Schema(description = "Tax identification number", example = "TAX-12345", maxLength = 50)
    private String taxNumber;

    @Size(max = 100, message = "Industry must not exceed {max} characters")
    @Schema(description = "Industry sector", example = "Energy & Utilities", maxLength = 100)
    private String industry;

    @Size(max = 255, message = "Website URL must not exceed {max} characters")
    @Schema(description = "Organization website URL", example = "https://www.acmecorp.com", maxLength = 255)
    private String website;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed {max} characters")
    @Schema(description = "Organization email address", example = "contact@acmecorp.com", maxLength = 100)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone must not exceed {max} characters")
    @Schema(description = "Phone number in E.164 format", example = "+1234567890", maxLength = 20)
    private String phone;

    @Size(max = 20, message = "Status must not exceed {max} characters")
    @Schema(description = "Organization status", example = "ACTIVE", maxLength = 20)
    private String status;

    @Size(max = 255, message = "Address line 1 must not exceed {max} characters")
    @Schema(description = "Primary address line", example = "123 Business Park Drive", maxLength = 255)
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed {max} characters")
    @Schema(description = "Secondary address line", example = "Suite 400", maxLength = 255)
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed {max} characters")
    @Schema(description = "City", example = "New York", maxLength = 100)
    private String city;

    @Size(max = 100, message = "State must not exceed {max} characters")
    @Schema(description = "State or province", example = "NY", maxLength = 100)
    private String state;

    @Size(max = 100, message = "Country must not exceed {max} characters")
    @Schema(description = "Country", example = "USA", maxLength = 100)
    private String country;

    @Size(max = 20, message = "Postal code must not exceed {max} characters")
    @Schema(description = "Postal or ZIP code", example = "10001", maxLength = 20)
    private String postalCode;

    @Size(max = 50, message = "Timezone must not exceed {max} characters")
    @Schema(description = "Timezone", example = "America/New_York", maxLength = 50)
    private String timezone;

    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    @Schema(description = "Three-letter ISO currency code", example = "USD", minLength = 3, maxLength = 3)
    private String currency;

    @Size(max = 500, message = "Logo URL must not exceed {max} characters")
    @Schema(description = "URL to organization logo image", example = "https://cdn.acmecorp.com/logo.png", maxLength = 500)
    private String logoUrl;
}
