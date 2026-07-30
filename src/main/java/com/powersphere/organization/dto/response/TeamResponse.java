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
@Schema(description = "Team response containing full team details")
public class TeamResponse {

    @Schema(description = "Unique team identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Team name", example = "Platform Engineering")
    private String name;

    @Schema(description = "Team code", example = "PE")
    private String code;

    @Schema(description = "Team description", example = "Platform Engineering Team")
    private String description;

    @Schema(description = "Team lead name", example = "Jane Doe")
    private String teamLead;

    @Schema(description = "Parent department ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID departmentId;

    @Schema(description = "Parent department name", example = "Engineering")
    private String departmentName;

    @Schema(description = "Whether the team is active", example = "true")
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
