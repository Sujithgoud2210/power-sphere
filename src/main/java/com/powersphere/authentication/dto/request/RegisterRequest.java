package com.powersphere.authentication.dto.request;

import com.powersphere.authentication.validation.Password;
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
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between {min} and {max} characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between {min} and {max} characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between {min} and {max} characters")
    @Password
    private String password;

    private String phone;
}
