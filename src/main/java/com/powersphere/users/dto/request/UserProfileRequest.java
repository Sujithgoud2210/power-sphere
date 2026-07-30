package com.powersphere.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating a user's profile information")
public class UserProfileRequest {

    @Size(max = 50, message = "Employee ID must not exceed {max} characters")
    @Schema(description = "Employee identification number", example = "EMP-001", maxLength = 50)
    private String employeeId;

    @Size(max = 100, message = "Designation must not exceed {max} characters")
    @Schema(description = "Job title or designation", example = "Senior Engineer", maxLength = 100)
    private String designation;

    @Past(message = "Joining date must be in the past")
    @Schema(description = "Date of joining the organization", example = "2024-01-15")
    private LocalDate joiningDate;

    @Past(message = "Date of birth must be in the past")
    @Schema(description = "User's date of birth", example = "1990-05-20")
    private LocalDate dateOfBirth;

    @Size(max = 20, message = "Gender must not exceed {max} characters")
    @Schema(description = "Gender", example = "Male", maxLength = 20)
    private String gender;

    @Size(max = 500, message = "Address must not exceed {max} characters")
    @Schema(description = "Residential address", example = "456 Park Avenue, Apt 12", maxLength = 500)
    private String address;

    @Size(max = 100, message = "Emergency contact must not exceed {max} characters")
    @Schema(description = "Emergency contact name and number", example = "Jane Doe: +1234567890", maxLength = 100)
    private String emergencyContact;

    @Size(max = 500, message = "Profile image URL must not exceed {max} characters")
    @Schema(description = "URL to profile image", example = "https://cdn.powersphere.com/profiles/avatar.png", maxLength = 500)
    private String profileImageUrl;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;
}
