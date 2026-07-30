package com.powersphere.users.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Complete user profile response with personal details, organizational assignments, and account status")
public class UserProfileResponse {

    @Schema(description = "Profile record identifier")
    private UUID id;

    @Schema(description = "User account identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID userId;

    @Schema(description = "Username", example = "john.doe")
    private String username;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Employee identification number", example = "EMP-001")
    private String employeeId;

    @Schema(description = "Job designation", example = "Senior Engineer")
    private String designation;

    @Schema(description = "Date of joining")
    private LocalDate joiningDate;

    @Schema(description = "Date of birth")
    private LocalDate dateOfBirth;

    @Schema(description = "Gender", example = "Male")
    private String gender;

    @Schema(description = "Residential address")
    private String address;

    @Schema(description = "Emergency contact information")
    private String emergencyContact;

    @Schema(description = "Profile image URL")
    private String profileImageUrl;

    @Schema(description = "Account status", example = "ACTIVE")
    private String status;

    @Schema(description = "Whether the account is enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Whether the account is locked", example = "false")
    private boolean accountLocked;

    @Schema(description = "Whether the email is verified", example = "true")
    private boolean emailVerified;

    @Schema(description = "Assigned roles", example = "[\"VIEWER\"]")
    private List<String> roles;

    @Schema(description = "Organization ID")
    private UUID organizationId;

    @Schema(description = "Organization name", example = "Acme Corporation")
    private String organizationName;

    @Schema(description = "Department ID")
    private UUID departmentId;

    @Schema(description = "Department name", example = "Engineering")
    private String departmentName;

    @Schema(description = "Team ID")
    private UUID teamId;

    @Schema(description = "Team name", example = "Platform Engineering")
    private String teamName;

    @Schema(description = "Last login timestamp")
    private LocalDateTime lastLogin;

    @Schema(description = "Profile creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Profile last update timestamp")
    private LocalDateTime updatedAt;
}
