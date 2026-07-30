package com.powersphere.users.service;

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

    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String emergencyContact;
    private String profileImageUrl;
}
