package com.powersphere.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request payload for creating or updating a department")
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between {min} and {max} characters")
    @Schema(description = "Department name", example = "Engineering", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String name;

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 20, message = "Department code must be between {min} and {max} characters")
    @Schema(description = "Unique department code", example = "ENG", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 20)
    private String code;

    @Size(max = 255, message = "Description must not exceed {max} characters")
    @Schema(description = "Department description", example = "Engineering and Technology Division", maxLength = 255)
    private String description;

    @Size(max = 100, message = "Manager name must not exceed {max} characters")
    @Schema(description = "Department manager name", example = "John Smith", maxLength = 100)
    private String manager;

    @Schema(description = "ID of the parent organization", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID organizationId;
}
