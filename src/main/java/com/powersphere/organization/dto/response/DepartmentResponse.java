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
@Schema(description = "Department response containing full department details")
public class DepartmentResponse {

    @Schema(description = "Unique department identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Department name", example = "Engineering")
    private String name;

    @Schema(description = "Department code", example = "ENG")
    private String code;

    @Schema(description = "Department description", example = "Engineering and Technology Division")
    private String description;

    @Schema(description = "Department manager name", example = "John Smith")
    private String manager;

    @Schema(description = "Parent organization ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID organizationId;

    @Schema(description = "Parent organization name", example = "Acme Corporation")
    private String organizationName;

    @Schema(description = "Whether the department is active", example = "true")
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
