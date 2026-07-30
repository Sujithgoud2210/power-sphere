package com.powersphere.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for assigning roles to a user")
public class AssignRoleRequest {

    @NotEmpty(message = "At least one role ID must be provided")
    @Schema(description = "Set of role IDs to assign to the user", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<UUID> roleIds;
}
