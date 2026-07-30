package com.powersphere.meter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to assign a meter to a user")
public class AssignMeterRequest {

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user to assign the meter to", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID userId;
}
