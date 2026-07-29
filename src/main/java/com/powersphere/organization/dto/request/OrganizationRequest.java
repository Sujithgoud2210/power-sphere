package com.powersphere.organization.dto.request;

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
public class OrganizationRequest {

    @NotBlank(message = "Organization code is required")
    @Size(min = 2, max = 20, message = "Organization code must be between {min} and {max} characters")
    private String organizationCode;

    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 150, message = "Organization name must be between {min} and {max} characters")
    private String organizationName;

    @Size(max = 50, message = "Registration number must not exceed {max} characters")
    private String registrationNumber;

    @Size(max = 50, message = "Tax number must not exceed {max} characters")
    private String taxNumber;

    @Size(max = 100, message = "Industry must not exceed {max} characters")
    private String industry;

    @Size(max = 255, message = "Website URL must not exceed {max} characters")
    private String website;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed {max} characters")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone must not exceed {max} characters")
    private String phone;

    @Size(max = 20, message = "Status must not exceed {max} characters")
    private String status;

    @Size(max = 255, message = "Address line 1 must not exceed {max} characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed {max} characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed {max} characters")
    private String city;

    @Size(max = 100, message = "State must not exceed {max} characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed {max} characters")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed {max} characters")
    private String postalCode;

    @Size(max = 50, message = "Timezone must not exceed {max} characters")
    private String timezone;

    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    @Size(max = 500, message = "Logo URL must not exceed {max} characters")
    private String logoUrl;
}
