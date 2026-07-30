package com.powersphere.users.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.users.dto.request.AssignDepartmentRequest;
import com.powersphere.users.dto.request.AssignRoleRequest;
import com.powersphere.users.dto.request.AssignTeamRequest;
import com.powersphere.users.dto.request.UserProfileRequest;
import com.powersphere.users.dto.response.UserProfileResponse;
import com.powersphere.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = ApplicationConstants.API_BASE_PATH + "/users",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get user profile",
            description = "Retrieves the complete profile of a specific user by their unique user ID including personal details, organizational assignments, and account status.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable UUID userId) {
        log.debug("GET /api/v1/users/{}/profile", userId);
        UserProfileResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update user profile",
            description = "Updates the profile information for a specific user including personal details, contact information, and organizational data.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequest request) {
        log.debug("PUT /api/v1/users/{}/profile", userId);
        UserProfileResponse response = userService.updateUserProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @PostMapping("/{userId}/assign-roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign roles to user",
            description = "Assigns one or more roles to a user. Available roles: ADMIN, MANAGER, OPERATOR, VIEWER.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid role IDs"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or role not found")
    })
    public ResponseEntity<ApiResponse<Void>> assignRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignRoleRequest request) {
        log.debug("POST /api/v1/users/{}/assign-roles", userId);
        userService.assignRoles(userId, request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Roles assigned successfully"));
    }

    @PutMapping("/{userId}/assign-department")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Assign department to user",
            description = "Assigns a department to a user, updating their organizational hierarchy.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or department not found")
    })
    public ResponseEntity<ApiResponse<Void>> assignDepartment(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignDepartmentRequest request) {
        log.debug("PUT /api/v1/users/{}/assign-department", userId);
        userService.assignDepartment(userId, request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Department assigned successfully"));
    }

    @PutMapping("/{userId}/assign-team")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Assign team to user",
            description = "Assigns a team to a user, updating their team membership within the department.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or team not found")
    })
    public ResponseEntity<ApiResponse<Void>> assignTeam(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignTeamRequest request) {
        log.debug("PUT /api/v1/users/{}/assign-team", userId);
        userService.assignTeam(userId, request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Team assigned successfully"));
    }

    @PostMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate user",
            description = "Activates a previously deactivated user account, restoring access to the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable UUID userId) {
        log.debug("POST /api/v1/users/{}/activate", userId);
        userService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.<Void>success("User activated successfully"));
    }

    @PostMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user",
            description = "Deactivates a user account, revoking access without deleting the account data.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID userId) {
        log.debug("POST /api/v1/users/{}/deactivate", userId);
        userService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.<Void>success("User deactivated successfully"));
    }

    @PostMapping("/{userId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lock user account",
            description = "Locks a user account, preventing login until the account is manually unlocked.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User locked successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<Void>> lockUser(@PathVariable UUID userId) {
        log.debug("POST /api/v1/users/{}/lock", userId);
        userService.lockUser(userId);
        return ResponseEntity.ok(ApiResponse.<Void>success("User locked successfully"));
    }

    @PostMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Unlock user account",
            description = "Unlocks a previously locked user account, restoring login capability.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User unlocked successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<Void>> unlockUser(@PathVariable UUID userId) {
        log.debug("POST /api/v1/users/{}/unlock", userId);
        userService.unlockUser(userId);
        return ResponseEntity.ok(ApiResponse.<Void>success("User unlocked successfully"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search users with pagination",
            description = "Search users by keyword matching against name, email, username, employee ID, or designation. Results are paginated and sorted by first name ascending by default.")
    @Parameter(name = "q", description = "Search keyword (matches against name, email, username, employee ID, or designation)", required = true, example = "john")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<UserProfileResponse>>> searchUsers(
            @RequestParam String q,
            @PageableDefault(size = 20, sort = "user.firstName", direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("GET /api/v1/users/search?q={}&page={}&size={}", q, pageable.getPageNumber(), pageable.getPageSize());
        Page<UserProfileResponse> users = userService.searchUsersPaginated(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PostMapping("/{userId}/profile-image")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Upload profile image (placeholder)",
            description = "Placeholder endpoint for uploading a profile image. Currently returns a mock CDN URL. File upload functionality will be implemented in a future release.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile image URL generated (placeholder)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(@PathVariable UUID userId) {
        log.debug("POST /api/v1/users/{}/profile-image - placeholder", userId);
        String mockUrl = "https://cdn.powersphere.com/profiles/" + userId + "/avatar.png";
        return ResponseEntity.ok(ApiResponse.success("Profile image upload placeholder - " +
                "URL would be: " + mockUrl, mockUrl));
    }

    @GetMapping("/by-organization/{organizationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get users by organization",
            description = "Retrieves all users belonging to an organization")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getUsersByOrganization(
            @PathVariable UUID organizationId) {
        log.debug("GET /api/v1/users/by-organization/{}", organizationId);
        List<UserProfileResponse> users = userService.getUsersByOrganization(organizationId);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get users by department",
            description = "Retrieves all users belonging to a department")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getUsersByDepartment(
            @PathVariable UUID departmentId) {
        log.debug("GET /api/v1/users/by-department/{}", departmentId);
        List<UserProfileResponse> users = userService.getUsersByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/by-team/{teamId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get users by team",
            description = "Retrieves all users belonging to a team")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getUsersByTeam(
            @PathVariable UUID teamId) {
        log.debug("GET /api/v1/users/by-team/{}", teamId);
        List<UserProfileResponse> users = userService.getUsersByTeam(teamId);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
