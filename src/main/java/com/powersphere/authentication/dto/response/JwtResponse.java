package com.powersphere.authentication.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT token response containing access and refresh tokens")
public class JwtResponse {

    @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Refresh token for obtaining a new access token when the current one expires", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String refreshToken;

    @Schema(description = "Token type (always Bearer)", example = "Bearer")
    private String tokenType;

    @Schema(description = "Access token expiration time in seconds", example = "3600")
    private long expiresIn;
}
