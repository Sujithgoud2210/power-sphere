package com.powersphere.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private UUID id;
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String status;
    private boolean enabled;
    private boolean accountLocked;
    private boolean emailVerified;
    private LocalDateTime lastLogin;
    private Set<String> roles;
    private UUID organizationId;
    private String organizationName;
    private UUID departmentId;
    private String departmentName;
    private UUID teamId;
    private String teamName;
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String emergencyContact;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
