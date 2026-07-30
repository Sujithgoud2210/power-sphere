package com.powersphere.users.service.impl;

import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.exception.RoleNotFoundException;
import com.powersphere.authentication.exception.UserNotFoundException;
import com.powersphere.authentication.repository.RoleRepository;
import com.powersphere.authentication.repository.UserRepository;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.entity.Team;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.exception.TeamNotFoundException;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.OrganizationRepository;
import com.powersphere.organization.repository.TeamRepository;
import com.powersphere.users.dto.request.AssignDepartmentRequest;
import com.powersphere.users.dto.request.AssignRoleRequest;
import com.powersphere.users.dto.request.AssignTeamRequest;
import com.powersphere.users.dto.request.UserProfileRequest;
import com.powersphere.users.dto.response.UserProfileResponse;
import com.powersphere.users.entity.UserProfile;
import com.powersphere.users.mapper.UserProfileMapper;
import com.powersphere.users.repository.UserProfileRepository;
import com.powersphere.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserProfileRepository userProfileRepository,
                           RoleRepository roleRepository,
                           OrganizationRepository organizationRepository,
                           DepartmentRepository departmentRepository,
                           TeamRepository teamRepository,
                           UserProfileMapper userProfileMapper) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.roleRepository = roleRepository;
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.teamRepository = teamRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(UUID userId) {
        log.debug("Fetching profile for user: {}", userId);

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return userProfileMapper.toResponse(userProfile);
    }

    @Override
    public UserProfileResponse updateUserProfile(UUID userId, UserProfileRequest request) {
        log.info("Updating profile for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.debug("Creating new profile for user: {}", userId);
                    UserProfile newProfile = UserProfile.builder()
                            .user(user)
                            .build();
                    return userProfileRepository.save(newProfile);
                });

        if (request.getEmployeeId() != null) {
            if (!request.getEmployeeId().equals(userProfile.getEmployeeId())
                    && userProfileRepository.existsByEmployeeId(request.getEmployeeId())) {
                throw new IllegalArgumentException(
                        "Employee ID '" + request.getEmployeeId() + "' is already in use");
            }
            userProfile.setEmployeeId(request.getEmployeeId());
        }
        if (request.getDesignation() != null) {
            userProfile.setDesignation(request.getDesignation());
        }
        if (request.getJoiningDate() != null) {
            userProfile.setJoiningDate(request.getJoiningDate());
        }
        if (request.getDateOfBirth() != null) {
            userProfile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            userProfile.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            userProfile.setAddress(request.getAddress());
        }
        if (request.getEmergencyContact() != null) {
            userProfile.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getProfileImageUrl() != null) {
            userProfile.setProfileImageUrl(request.getProfileImageUrl());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        log.info("Profile updated for user: {}", userId);

        return userProfileMapper.toResponse(savedProfile);
    }

    @Override
    public void assignRoles(UUID userId, AssignRoleRequest request) {
        log.info("Assigning roles to user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        var roles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);
        log.info("Roles assigned to user: {} - {} roles", userId, roles.size());
    }

    @Override
    public void assignDepartment(UUID userId, AssignDepartmentRequest request) {
        log.info("Assigning department to user: {}", userId);

        UserProfile userProfile = getOrCreateUserProfile(userId);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        userProfile.setDepartment(department);
        userProfile.setOrganization(department.getOrganization());
        userProfileRepository.save(userProfile);
        log.info("Department assigned to user: {}", userId);
    }

    @Override
    public void assignTeam(UUID userId, AssignTeamRequest request) {
        log.info("Assigning team to user: {}", userId);

        UserProfile userProfile = getOrCreateUserProfile(userId);

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new TeamNotFoundException(
                        "Team not found with id: " + request.getTeamId()));

        userProfile.setTeam(team);
        if (userProfile.getDepartment() == null) {
            userProfile.setDepartment(team.getDepartment());
        }
        if (userProfile.getOrganization() == null) {
            userProfile.setOrganization(team.getDepartment().getOrganization());
        }
        userProfileRepository.save(userProfile);
        log.info("Team assigned to user: {}", userId);
    }

    @Override
    public void activateUser(UUID userId) {
        log.info("Activating user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setEnabled(true);
        user.setStatus("ACTIVE");
        userRepository.save(user);
        log.info("User activated: {}", userId);
    }

    @Override
    public void deactivateUser(UUID userId) {
        log.info("Deactivating user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setEnabled(false);
        user.setStatus("INACTIVE");
        userRepository.save(user);
        log.info("User deactivated: {}", userId);
    }

    @Override
    public void lockUser(UUID userId) {
        log.info("Locking user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setAccountLocked(true);
        user.setStatus("LOCKED");
        userRepository.save(user);
        log.info("User locked: {}", userId);
    }

    @Override
    public void unlockUser(UUID userId) {
        log.info("Unlocking user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setStatus("ACTIVE");
        userRepository.save(user);
        log.info("User unlocked: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> searchUsers(String searchTerm) {
        log.debug("Searching users with term: {}", searchTerm);

        return userProfileRepository.searchUsers(searchTerm).stream()
                .map(userProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileResponse> searchUsersPaginated(String searchTerm, Pageable pageable) {
        log.debug("Paginated search users with term: {} - page: {}, size: {}",
                searchTerm, pageable.getPageNumber(), pageable.getPageSize());

        return userProfileRepository.searchUsersPaginated(searchTerm, pageable)
                .map(userProfileMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getUsersByOrganization(UUID organizationId) {
        log.debug("Fetching users for organization: {}", organizationId);

        return userProfileRepository.findByOrganizationId(organizationId).stream()
                .map(userProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getUsersByDepartment(UUID departmentId) {
        log.debug("Fetching users for department: {}", departmentId);

        return userProfileRepository.findByDepartmentId(departmentId).stream()
                .map(userProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getUsersByTeam(UUID teamId) {
        log.debug("Fetching users for team: {}", teamId);

        return userProfileRepository.findByTeamId(teamId).stream()
                .map(userProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    private UserProfile getOrCreateUserProfile(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException(
                                    "User not found with id: " + userId));
                    return userProfileRepository.save(
                            UserProfile.builder().user(user).build());
                });
    }
}
