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
@Schema(description = "Request to transfer a meter to another user")
public class TransferMeterRequest {

    @NotNull(message = "Current user ID is required")
    @Schema(description = "ID of the current assigned user", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID currentUserId;

    @NotNull(message = "New user ID is required")
    @Schema(description = "ID of the new user to transfer the meter to", example = "660e8400-e29b-41d4-a716-446655440001")
    private UUID newUserId;
}
