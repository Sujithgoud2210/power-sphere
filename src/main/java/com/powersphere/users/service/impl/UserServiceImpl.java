package com.powersphere.users.service.impl;

import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.exception.UserNotFoundException;
import com.powersphere.authentication.repository.RoleRepository;
import com.powersphere.authentication.repository.UserRepository;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.TeamRepository;
import com.powersphere.users.dto.response.UserProfileResponse;
import com.powersphere.users.entity.UserProfile;
import com.powersphere.users.exception.UserProfileNotFoundException;
import com.powersphere.users.mapper.UserProfileMapper;
import com.powersphere.users.repository.UserProfileRepository;
import com.powersphere.users.service.UserProfileRequest;
import com.powersphere.users.service.UserRoleAssignmentRequest;
import com.powersphere.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(UUID userId) {
        log.debug("Fetching profile for user: {}", userId);

        var userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        return userProfileMapper.toResponse(userProfile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(UUID userId, UserProfileRequest request) {
        log.debug("Updating profile for user: {}", userId);

        var userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        if (request.getEmployeeId() != null) userProfile.setEmployeeId(request.getEmployeeId());
        if (request.getDesignation() != null) userProfile.setDesignation(request.getDesignation());
        if (request.getJoiningDate() != null) userProfile.setJoiningDate(request.getJoiningDate());
        if (request.getDateOfBirth() != null) userProfile.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) userProfile.setGender(request.getGender());
        if (request.getAddress() != null) userProfile.setAddress(request.getAddress());
        if (request.getEmergencyContact() != null) userProfile.setEmergencyContact(request.getEmergencyContact());
        if (request.getProfileImageUrl() != null) userProfile.setProfileImageUrl(request.getProfileImageUrl());

        userProfile = userProfileRepository.save(userProfile);
        log.info("Profile updated for user: {}", userId);
        return userProfileMapper.toResponse(userProfile);
    }

    @Override
    @Transactional
    public void assignRoles(UUID userId, UserRoleAssignmentRequest request) {
        log.debug("Assigning roles to user: {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        var roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        user.setRoles(roles);
        userRepository.save(user);
        log.info("Roles assigned to user: {}", userId);
    }

    @Override
    @Transactional
    public void assignDepartment(UUID userId, UUID departmentId) {
        log.debug("Assigning department {} to user: {}", departmentId, userId);

        var userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new com.powersphere.organization.exception.DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        userProfile.setDepartment(department);
        userProfileRepository.save(userProfile);
        log.info("Department assigned to user: {}", userId);
    }

    @Override
    @Transactional
    public void assignTeam(UUID userId, UUID teamId) {
        log.debug("Assigning team {} to user: {}", teamId, userId);

        var userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new com.powersphere.organization.exception.TeamNotFoundException(
                        "Team not found with id: " + teamId));

        userProfile.setTeam(team);
        userProfileRepository.save(userProfile);
        log.info("Team assigned to user: {}", userId);
    }

    @Override
    @Transactional
    public void activateUser(UUID userId) {
        log.debug("Activating user: {}", userId);
        updateUserStatus(userId, true, false);
    }

    @Override
    @Transactional
    public void deactivateUser(UUID userId) {
        log.debug("Deactivating user: {}", userId);
        updateUserStatus(userId, false, false);
    }

    @Override
    @Transactional
    public void lockUser(UUID userId) {
        log.debug("Locking user: {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setAccountLocked(true);
        userRepository.save(user);
        log.info("User locked: {}", userId);
    }

    @Override
    @Transactional
    public void unlockUser(UUID userId) {
        log.debug("Unlocking user: {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setAccountLocked(false);
        userRepository.save(user);
        log.info("User unlocked: {}", userId);
    }

    private void updateUserStatus(UUID userId, boolean active, boolean locked) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setEnabled(active);
        user.setIsActive(active);
        userRepository.save(user);
        log.info("User status updated: {} active={}", userId, active);
    }
}
