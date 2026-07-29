package com.powersphere.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
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
    private String organizationCode;

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    private String registrationNumber;
    private String taxNumber;
    private String industry;
    private String website;
    private String email;
    private String phone;
    private String status;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String timezone;
    private String currency;
    private String logoUrl;
}
