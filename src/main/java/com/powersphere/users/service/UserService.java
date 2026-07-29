package com.powersphere.users.service;

import com.powersphere.users.dto.response.UserProfileResponse;

import java.util.UUID;

public interface UserService {

    UserProfileResponse getUserProfile(UUID userId);

    UserProfileResponse updateUserProfile(UUID userId, UserProfileRequest request);

    void assignRoles(UUID userId, UserRoleAssignmentRequest request);

    void assignDepartment(UUID userId, UUID departmentId);

    void assignTeam(UUID userId, UUID teamId);

    void activateUser(UUID userId);

    void deactivateUser(UUID userId);

    void lockUser(UUID userId);

    void unlockUser(UUID userId);
}
