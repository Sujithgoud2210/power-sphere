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
@Schema(description = "Request payload for creating or updating a team")
public class TeamRequest {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 100, message = "Team name must be between {min} and {max} characters")
    @Schema(description = "Team name", example = "Platform Engineering", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String name;

    @NotBlank(message = "Team code is required")
    @Size(min = 2, max = 20, message = "Team code must be between {min} and {max} characters")
    @Schema(description = "Unique team code", example = "PE", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 20)
    private String code;

    @Size(max = 255, message = "Description must not exceed {max} characters")
    @Schema(description = "Team description", example = "Platform Engineering Team", maxLength = 255)
    private String description;

    @Size(max = 100, message = "Team lead must not exceed {max} characters")
    @Schema(description = "Team lead name", example = "Jane Doe", maxLength = 100)
    private String teamLead;

    @Schema(description = "ID of the parent department", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID departmentId;
}
