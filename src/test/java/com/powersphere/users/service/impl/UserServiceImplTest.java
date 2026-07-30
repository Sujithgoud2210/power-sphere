package com.powersphere.users.service.impl;

import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.exception.UserNotFoundException;
import com.powersphere.authentication.repository.RoleRepository;
import com.powersphere.authentication.repository.UserRepository;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.entity.Team;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserProfile> profileCaptor;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final UUID ORG_ID = UUID.randomUUID();
    private static final UUID DEPT_ID = UUID.randomUUID();
    private static final UUID TEAM_ID = UUID.randomUUID();

    private User user;
    private Role role;
    private Organization organization;
    private Department department;
    private Team team;
    private UserProfile userProfile;
    private UserProfileResponse profileResponse;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository, userProfileRepository, roleRepository,
                organizationRepository, departmentRepository, teamRepository,
                userProfileMapper);

        user = User.builder()
                .id(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .username("johndoe")
                .password("encoded")
                .enabled(true)
                .accountLocked(false)
                .status("ACTIVE")
                .failedLoginAttempts(0)
                .isActive(true)
                .build();

        role = Role.builder()
                .id(ROLE_ID)
                .name("VIEWER")
                .isActive(true)
                .build();

        organization = Organization.builder()
                .id(ORG_ID)
                .organizationName("Test Org")
                .build();

        department = Department.builder()
                .id(DEPT_ID)
                .name("Engineering")
                .organization(organization)
                .build();

        team = Team.builder()
                .id(TEAM_ID)
                .name("Alpha Team")
                .department(department)
                .build();

        userProfile = UserProfile.builder()
                .id(UUID.randomUUID())
                .user(user)
                .employeeId("EMP001")
                .designation("Engineer")
                .organization(organization)
                .department(department)
                .team(team)
                .build();

        profileResponse = UserProfileResponse.builder()
                .userId(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .username("johndoe")
                .employeeId("EMP001")
                .designation("Engineer")
                .build();
    }

    @Nested
    @DisplayName("Get User Profile")
    class GetUserProfile {

        @Test
        @DisplayName("Should get existing profile")
        void shouldGetProfile() {
            when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(userProfile));
            when(userProfileMapper.toResponse(userProfile)).thenReturn(profileResponse);

            var result = userService.getUserProfile(USER_ID);

            assertThat(result).isNotNull();
            assertThat(result.getEmployeeId()).isEqualTo("EMP001");
        }

        @Test
        @DisplayName("Should throw when no profile")
        void shouldThrowWhenNoProfile() {
            when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserProfile(USER_ID))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Update User Profile")
    class UpdateUserProfile {

        @Test
        @DisplayName("Should update existing profile")
        void shouldUpdateProfile() {
            UserProfileRequest request = UserProfileRequest.builder()
                    .designation("Senior Engineer")
                    .build();

            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(userProfile));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
            when(userProfileMapper.toResponse(any(UserProfile.class))).thenReturn(profileResponse);

            var result = userService.updateUserProfile(USER_ID, request);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should create profile if not exists")
        void shouldCreateProfileIfNotExists() {
            UserProfileRequest request = UserProfileRequest.builder()
                    .designation("Engineer")
                    .employeeId("EMP002")
                    .build();

            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
            when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
            when(userProfileMapper.toResponse(any(UserProfile.class))).thenReturn(profileResponse);

            var result = userService.updateUserProfile(USER_ID, request);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Assign Roles")
    class AssignRoles {

        @Test
        @DisplayName("Should assign roles to user")
        void shouldAssignRoles() {
            AssignRoleRequest request = AssignRoleRequest.builder()
                    .roleIds(Set.of(ROLE_ID))
                    .build();

            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.of(role));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.assignRoles(USER_ID, request);

            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().getRoles()).contains(role);
        }

        @Test
        @DisplayName("Should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            AssignRoleRequest request = AssignRoleRequest.builder()
                    .roleIds(Set.of(ROLE_ID))
                    .build();

            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.assignRoles(USER_ID, request))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Assign Department")
    class AssignDepartment {

        @Test
        @DisplayName("Should assign department to user")
        void shouldAssignDepartment() {
            AssignDepartmentRequest request = AssignDepartmentRequest.builder()
                    .departmentId(DEPT_ID)
                    .build();

            when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(userProfile));
            when(departmentRepository.findById(DEPT_ID)).thenReturn(Optional.of(department));
            when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

            userService.assignDepartment(USER_ID, request);

            verify(userProfileRepository).save(profileCaptor.capture());
            assertThat(profileCaptor.getValue().getDepartment()).isEqualTo(department);
        }
    }

    @Nested
    @DisplayName("Assign Team")
    class AssignTeam {

        @Test
        @DisplayName("Should assign team to user")
        void shouldAssignTeam() {
            AssignTeamRequest request = AssignTeamRequest.builder()
                    .teamId(TEAM_ID)
                    .build();

            when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(userProfile));
            when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
            when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

            userService.assignTeam(USER_ID, request);

            verify(userProfileRepository).save(profileCaptor.capture());
            assertThat(profileCaptor.getValue().getTeam()).isEqualTo(team);
        }
    }

    @Nested
    @DisplayName("Activate/Deactivate/Lock/Unlock")
    class UserStatus {

        @Test
        @DisplayName("Should activate user")
        void shouldActivateUser() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.activateUser(USER_ID);

            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().isEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should deactivate user")
        void shouldDeactivateUser() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.deactivateUser(USER_ID);

            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().isEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should lock user")
        void shouldLockUser() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.lockUser(USER_ID);

            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().isAccountLocked()).isTrue();
        }

        @Test
        @DisplayName("Should unlock user and reset attempts")
        void shouldUnlockUser() {
            user.setAccountLocked(true);
            user.setFailedLoginAttempts(5);

            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.unlockUser(USER_ID);

            verify(userRepository).save(userCaptor.capture());
            User saved = userCaptor.getValue();
            assertThat(saved.isAccountLocked()).isFalse();
            assertThat(saved.getFailedLoginAttempts()).isZero();
        }
    }
}
