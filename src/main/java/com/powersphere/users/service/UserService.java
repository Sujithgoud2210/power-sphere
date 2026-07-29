package com.powersphere.users.service;

import com.powersphere.users.dto.request.AssignDepartmentRequest;
import com.powersphere.users.dto.request.AssignRoleRequest;
import com.powersphere.users.dto.request.AssignTeamRequest;
import com.powersphere.users.dto.request.UserProfileRequest;
import com.powersphere.users.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserProfileResponse getUserProfile(UUID userId);

    UserProfileResponse updateUserProfile(UUID userId, UserProfileRequest request);

    void assignRoles(UUID userId, AssignRoleRequest request);

    void assignDepartment(UUID userId, AssignDepartmentRequest request);

    void assignTeam(UUID userId, AssignTeamRequest request);

    void activateUser(UUID userId);

    void deactivateUser(UUID userId);

    void lockUser(UUID userId);

    void unlockUser(UUID userId);

    List<UserProfileResponse> searchUsers(String searchTerm);

    Page<UserProfileResponse> searchUsersPaginated(String searchTerm, Pageable pageable);

    List<UserProfileResponse> getUsersByOrganization(UUID organizationId);

    List<UserProfileResponse> getUsersByDepartment(UUID departmentId);

    List<UserProfileResponse> getUsersByTeam(UUID teamId);
}
