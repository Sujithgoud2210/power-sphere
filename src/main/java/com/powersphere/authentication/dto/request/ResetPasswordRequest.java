package com.powersphere.authentication.dto.request;

import com.powersphere.authentication.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Reset password request payload for setting a new password using a reset token")
public class ResetPasswordRequest {

    @NotBlank(message = "Token is required")
    @Schema(description = "Password reset token received via email", example = "abc123-def456-ghi789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 128, message = "Password must be between {min} and {max} characters")
    @Password
    @Schema(description = "New password. Must be at least 8 characters with uppercase, lowercase, digit, and special character", example = "NewP@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 8, maxLength = 128, format = "password")
    private String newPassword;
}
