package com.powersphere.authentication.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request payload for user authentication")
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    @Schema(description = "Registered username or email address", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Schema(description = "Account password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    private String password;
}
