package com.powersphere.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 20, message = "Department code must be between {min} and {max} characters")
    private String code;

    @Size(max = 255, message = "Description must not exceed {max} characters")
    private String description;

    @Size(max = 100, message = "Manager name must not exceed {max} characters")
    private String manager;

    private UUID organizationId;
}
