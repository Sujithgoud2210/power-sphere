package com.powersphere.authentication.controller;

import com.powersphere.authentication.dto.request.ChangePasswordRequest;
import com.powersphere.authentication.dto.request.ForgotPasswordRequest;
import com.powersphere.authentication.dto.request.LoginRequest;
import com.powersphere.authentication.dto.request.RefreshTokenRequest;
import com.powersphere.authentication.dto.request.RegisterRequest;
import com.powersphere.authentication.dto.request.ResetPasswordRequest;
import com.powersphere.authentication.dto.response.JwtResponse;
import com.powersphere.authentication.dto.response.LoginResponse;
import com.powersphere.authentication.dto.response.RegisterResponse;
import com.powersphere.authentication.service.AuthenticationService;
import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = ApplicationConstants.API_BASE_PATH + "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Creates a new user account with VIEWER role. The user will receive a confirmation email upon successful registration.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        log.debug("POST /api/v1/auth/register - username: {}", request.getUsername());
        RegisterResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user",
            description = "Authenticates user with username/email and password. Returns JWT access token and refresh token upon successful authentication.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful, JWT tokens returned",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials or account disabled/locked")
    })
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        log.debug("POST /api/v1/auth/login - username: {}", request.getUsernameOrEmail());
        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user",
            description = "Revokes the refresh token, making it invalid for future token refresh requests.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody(required = false) RefreshTokenRequest request) {
        log.debug("POST /api/v1/auth/logout");
        String refreshToken = (request != null) ? request.getRefreshToken() : null;
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.<Void>success("Logout successful"));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token",
            description = "Refreshes the access token using a valid, non-expired refresh token. Returns a new access token and a new refresh token.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.debug("POST /api/v1/auth/refresh-token");
        JwtResponse response = authenticationService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset",
            description = "Requests a password reset link. If the email exists in the system, a password reset token is sent to the registered email address.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset email sent if account exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email format")
    })
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        log.debug("POST /api/v1/auth/forgot-password - email: {}", request.getEmail());
        authenticationService.forgotPassword(request);
        return ResponseEntity.ok(
                ApiResponse.<Void>success("If an account exists with this email, " +
                        "a password reset link has been sent"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password",
            description = "Resets the user's password using a valid reset token received via email. The token expires after use or after the configured expiration time.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired reset token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Password does not meet security requirements")
    })
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        log.debug("POST /api/v1/auth/reset-password");
        authenticationService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Password reset successful"));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password",
            description = "Changes the password for the currently authenticated user. Requires the current password for verification.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Current password is incorrect or new password is invalid"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        log.debug("POST /api/v1/auth/change-password");
        String username = authentication.getName();
        authenticationService.changePassword(username, request);
        return ResponseEntity.ok(ApiResponse.<Void>success("Password changed successfully"));
    }
}
