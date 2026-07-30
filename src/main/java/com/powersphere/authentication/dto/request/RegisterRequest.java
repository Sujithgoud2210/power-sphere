package com.powersphere.authentication.dto.request;

import com.powersphere.authentication.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Registration request payload for creating a new user account")
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between {min} and {max} characters")
    @Schema(description = "User's first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between {min} and {max} characters")
    @Schema(description = "User's last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "User's email address (must be unique)", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED, format = "email")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between {min} and {max} characters")
    @Schema(description = "Unique username for the account", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 3, maxLength = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between {min} and {max} characters")
    @Password
    @Schema(description = "Account password. Must be at least 8 characters with uppercase, lowercase, digit, and special character", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 8, maxLength = 128, format = "password")
    private String password;

    @Schema(description = "Optional phone number", example = "+1234567890", nullable = true)
    private String phone;
}
