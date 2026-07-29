package com.powersphere.users.dto.request;

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
public class UserProfileRequest {

    @Size(max = 50, message = "Employee ID must not exceed {max} characters")
    private String employeeId;

    @Size(max = 100, message = "Designation must not exceed {max} characters")
    private String designation;

    @Past(message = "Joining date must be in the past")
    private LocalDate joiningDate;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 20, message = "Gender must not exceed {max} characters")
    private String gender;

    @Size(max = 500, message = "Address must not exceed {max} characters")
    private String address;

    @Size(max = 100, message = "Emergency contact must not exceed {max} characters")
    private String emergencyContact;

    @Size(max = 500, message = "Profile image URL must not exceed {max} characters")
    private String profileImageUrl;

    private String firstName;
    private String lastName;
    private String phone;
}
