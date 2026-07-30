package com.powersphere.authentication.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registration response containing the created user's basic details")
public class RegisterResponse {

    @Schema(description = "Unique identifier of the created user", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's username", example = "john.doe")
    private String username;

    @Schema(description = "Registration result message", example = "Registration successful")
    private String message;
}
