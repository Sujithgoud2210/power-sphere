package com.powersphere.authentication.service.impl;

import com.powersphere.authentication.dto.request.ChangePasswordRequest;
import com.powersphere.authentication.dto.request.ForgotPasswordRequest;
import com.powersphere.authentication.dto.request.LoginRequest;
import com.powersphere.authentication.dto.request.RefreshTokenRequest;
import com.powersphere.authentication.dto.request.RegisterRequest;
import com.powersphere.authentication.dto.request.ResetPasswordRequest;
import com.powersphere.authentication.dto.response.JwtResponse;
import com.powersphere.authentication.dto.response.LoginResponse;
import com.powersphere.authentication.dto.response.RegisterResponse;
import com.powersphere.authentication.entity.RefreshToken;
import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.exception.InvalidCredentialsException;
import com.powersphere.authentication.exception.RefreshTokenExpiredException;
import com.powersphere.authentication.exception.RoleNotFoundException;
import com.powersphere.authentication.exception.UserAlreadyExistsException;
import com.powersphere.authentication.exception.UserNotFoundException;
import com.powersphere.authentication.jwt.JwtTokenProvider;
import com.powersphere.authentication.mapper.UserMapper;
import com.powersphere.authentication.repository.RefreshTokenRepository;
import com.powersphere.authentication.repository.RoleRepository;
import com.powersphere.authentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationServiceImpl Unit Tests")
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthenticationServiceImpl authService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<RefreshToken> refreshTokenCaptor;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Test@123";
    private static final String TEST_FIRST_NAME = "Test";
    private static final String TEST_LAST_NAME = "User";
    private static final String ACCESS_TOKEN = "access-token-123";
    private static final String REFRESH_TOKEN = "refresh-token-456";

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;
    private Role viewerRole;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authService = new AuthenticationServiceImpl(
                userRepository, roleRepository, refreshTokenRepository,
                userMapper, passwordEncoder, authenticationManager, jwtTokenProvider);

        registerRequest = RegisterRequest.builder()
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .email(TEST_EMAIL)
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        loginRequest = LoginRequest.builder()
                .usernameOrEmail(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        viewerRole = Role.builder()
                .id(UUID.randomUUID())
                .name("VIEWER")
                .description("Viewer role")
                .isActive(true)
                .build();

        testUser = User.builder()
                .id(UUID.randomUUID())
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .email(TEST_EMAIL)
                .username(TEST_USERNAME)
                .password("encoded-password")
                .enabled(true)
                .accountLocked(false)
                .emailVerified(false)
                .status("ACTIVE")
                .failedLoginAttempts(0)
                .isActive(true)
                .roles(Set.of(viewerRole))
                .build();

        authentication = new UsernamePasswordAuthenticationToken(
                TEST_USERNAME, TEST_PASSWORD, List.of());
    }

    // ==================== REGISTRATION ====================

    @Nested
    @DisplayName("Registration")
    class Registration {

        @Test
        @DisplayName("Should register user successfully")
        void shouldRegisterUserSuccessfully() {
            // Arrange
            when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(userMapper.registerRequestToUser(registerRequest)).thenReturn(testUser);
            when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encoded-password");
            when(roleRepository.findByName("VIEWER")).thenReturn(Optional.of(viewerRole));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.userToRegisterResponse(any(User.class)))
                    .thenReturn(RegisterResponse.builder()
                            .id(testUser.getId())
                            .firstName(TEST_FIRST_NAME)
                            .lastName(TEST_LAST_NAME)
                            .email(TEST_EMAIL)
                            .username(TEST_USERNAME)
                            .build());

            // Act
            RegisterResponse response = authService.register(registerRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getUsername()).isEqualTo(TEST_USERNAME);
            assertThat(response.getEmail()).isEqualTo(TEST_EMAIL);
            assertThat(response.getMessage()).contains("Registration successful");

            verify(passwordEncoder).encode(TEST_PASSWORD);
            verify(roleRepository).findByName("VIEWER");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        void shouldThrowExceptionWhenUsernameExists() {
            // Arrange
            when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining(TEST_USERNAME);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Arrange
            when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining(TEST_EMAIL);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when default VIEWER role not found")
        void shouldThrowExceptionWhenViewerRoleNotFound() {
            // Arrange
            when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(userMapper.registerRequestToUser(registerRequest)).thenReturn(testUser);
            when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encoded-password");
            when(roleRepository.findByName("VIEWER")).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(RoleNotFoundException.class)
                    .hasMessageContaining("VIEWER");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should assign VIEWER role to new user")
        void shouldAssignViewerRoleToNewUser() {
            // Arrange
            when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(userMapper.registerRequestToUser(registerRequest)).thenReturn(testUser);
            when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encoded-password");
            when(roleRepository.findByName("VIEWER")).thenReturn(Optional.of(viewerRole));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.userToRegisterResponse(any(User.class)))
                    .thenReturn(RegisterResponse.builder()
                            .id(testUser.getId())
                            .firstName(TEST_FIRST_NAME)
                            .lastName(TEST_LAST_NAME)
                            .email(TEST_EMAIL)
                            .username(TEST_USERNAME)
                            .build());

            // Act
            authService.register(registerRequest);

            // Assert
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getRoles()).contains(viewerRole);
        }
    }

    // ==================== LOGIN ====================

    @Nested
    @DisplayName("Login")
    class Login {

        @Test
        @DisplayName("Should login successfully")
        void shouldLoginSuccessfully() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
            when(jwtTokenProvider.generateAccessToken(anyString(), anyList())).thenReturn(ACCESS_TOKEN);
            when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn(REFRESH_TOKEN);
            when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(userMapper.userToLoginResponse(any(User.class)))
                    .thenReturn(LoginResponse.builder()
                            .id(testUser.getId())
                            .firstName(TEST_FIRST_NAME)
                            .lastName(TEST_LAST_NAME)
                            .email(TEST_EMAIL)
                            .username(TEST_USERNAME)
                            .roles(List.of("VIEWER"))
                            .tokenType("Bearer")
                            .build());
            when(refreshTokenRepository.save(any(RefreshToken.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            LoginResponse response = authService.login(loginRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getUsername()).isEqualTo(TEST_USERNAME);
            assertThat(response.getAccessToken()).isEqualTo(ACCESS_TOKEN);
            assertThat(response.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
            assertThat(response.getTokenType()).isEqualTo("Bearer");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtTokenProvider).generateAccessToken(TEST_USERNAME, List.of("VIEWER"));
            verify(jwtTokenProvider).generateRefreshToken(TEST_USERNAME);
        }

        @Test
        @DisplayName("Should reset failed login attempts on successful login")
        void shouldResetFailedLoginAttemptsOnSuccess() {
            // Arrange
            testUser.setFailedLoginAttempts(3);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
            when(jwtTokenProvider.generateAccessToken(anyString(), anyList())).thenReturn(ACCESS_TOKEN);
            when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn(REFRESH_TOKEN);
            when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);
            when(userMapper.userToLoginResponse(any(User.class)))
                    .thenReturn(LoginResponse.builder()
                            .id(testUser.getId())
                            .firstName(TEST_FIRST_NAME)
                            .lastName(TEST_LAST_NAME)
                            .email(TEST_EMAIL)
                            .username(TEST_USERNAME)
                            .build());
            when(refreshTokenRepository.save(any(RefreshToken.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            authService.login(loginRequest);

            // Assert
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getFailedLoginAttempts()).isZero();
            assertThat(savedUser.getLastLogin()).isNotNull();
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException for bad credentials")
        void shouldThrowExceptionForBadCredentials() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessageContaining("Invalid username or password");
        }

        @Test
        @DisplayName("Should increment failed login attempts on bad credentials")
        void shouldIncrementFailedLoginAttempts() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));
            when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class);

            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getFailedLoginAttempts()).isOne();
        }

        @Test
        @DisplayName("Should lock account after max failed attempts")
        void shouldLockAccountAfterMaxFailedAttempts() {
            // Arrange
            testUser.setFailedLoginAttempts(4);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));
            when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class);

            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getFailedLoginAttempts()).isEqualTo(5);
            assertThat(savedUser.isAccountLocked()).isTrue();
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException for disabled account")
        void shouldThrowExceptionForDisabledAccount() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new DisabledException("Account disabled"));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessageContaining("disabled");
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException for locked account")
        void shouldThrowExceptionForLockedAccount() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new LockedException("Account locked"));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessageContaining("locked");
        }
    }

    // ==================== LOGOUT ====================

    @Nested
    @DisplayName("Logout")
    class Logout {

        @Test
        @DisplayName("Should revoke refresh token on logout")
        void shouldRevokeRefreshTokenOnLogout() {
            // Arrange
            RefreshToken storedToken = RefreshToken.builder()
                    .id(UUID.randomUUID())
                    .token(REFRESH_TOKEN)
                    .revoked(false)
                    .build();
            when(refreshTokenRepository.findByToken(REFRESH_TOKEN))
                    .thenReturn(Optional.of(storedToken));
            when(refreshTokenRepository.save(any(RefreshToken.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            authService.logout(REFRESH_TOKEN);

            // Assert
            assertThat(storedToken.isRevoked()).isTrue();
            verify(refreshTokenRepository).save(storedToken);
        }

        @Test
        @DisplayName("Should do nothing when refresh token is null")
        void shouldDoNothingWhenTokenIsNull() {
            // Act
            authService.logout(null);

            // Assert
            verify(refreshTokenRepository, never()).findByToken(anyString());
            verify(refreshTokenRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should do nothing when refresh token not found")
        void shouldDoNothingWhenTokenNotFound() {
            // Arrange
            when(refreshTokenRepository.findByToken(REFRESH_TOKEN))
                    .thenReturn(Optional.empty());

            // Act
            authService.logout(REFRESH_TOKEN);

            // Assert
            verify(refreshTokenRepository, never()).save(any());
        }
    }

    // ==================== REFRESH TOKEN ====================

    @Nested
    @DisplayName("Refresh Token")
    class RefreshTokenOperation {

        @Test
        @DisplayName("Should refresh token successfully")
        void shouldRefreshTokenSuccessfully() {
            // Arrange
            RefreshToken storedToken = RefreshToken.builder()
                    .id(UUID.randomUUID())
                    .token(REFRESH_TOKEN)
                    .revoked(false)
                    .expiryDate(LocalDateTime.now().plusDays(1))
                    .user(testUser)
                    .build();

            RefreshTokenRequest request = RefreshTokenRequest.builder()
                    .refreshToken(REFRESH_TOKEN)
                    .build();

            String newAccessToken = "new-access-token";
            String newRefreshToken = "new-refresh-token";

            when(refreshTokenRepository.findByToken(REFRESH_TOKEN))
                    .thenReturn(Optional.of(storedToken));
            when(jwtTokenProvider.generateAccessToken(anyString(), anyList()))
                    .thenReturn(newAccessToken);
            when(jwtTokenProvider.generateRefreshToken(anyString()))
                    .thenReturn(newRefreshToken);
            when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);
            when(refreshTokenRepository.save(any(RefreshToken.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            JwtResponse response = authService.refreshToken(request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getAccessToken()).isEqualTo(newAccessToken);
            assertThat(response.getRefreshToken()).isEqualTo(newRefreshToken);
            assertThat(response.getTokenType()).isEqualTo("Bearer");

            // Verify old token revoked
            assertThat(storedToken.isRevoked()).isTrue();

            // Verify new token saved
            verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("Should throw exception when refresh token not found")
        void shouldThrowExceptionWhenTokenNotFound() {
            // Arrange
            RefreshTokenRequest request = RefreshTokenRequest.builder()
                    .refreshToken("nonexistent-token")
                    .build();
            when(refreshTokenRepository.findByToken("nonexistent-token"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(RefreshTokenExpiredException.class)
                    .hasMessage("Refresh token not found");
        }

        @Test
        @DisplayName("Should throw exception when refresh token is revoked")
        void shouldThrowExceptionWhenTokenRevoked() {
            // Arrange
            RefreshToken storedToken = RefreshToken.builder()
                    .id(UUID.randomUUID())
                    .token(REFRESH_TOKEN)
                    .revoked(true)
                    .expiryDate(LocalDateTime.now().plusDays(1))
                    .user(testUser)
                    .build();

            RefreshTokenRequest request = RefreshTokenRequest.builder()
                    .refreshToken(REFRESH_TOKEN)
                    .build();

            when(refreshTokenRepository.findByToken(REFRESH_TOKEN))
                    .thenReturn(Optional.of(storedToken));

            // Act & Assert
            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(RefreshTokenExpiredException.class)
                    .hasMessage("Refresh token has been revoked");
        }

        @Test
        @DisplayName("Should throw exception when refresh token is expired")
        void shouldThrowExceptionWhenTokenExpired() {
            // Arrange
            RefreshToken storedToken = RefreshToken.builder()
                    .id(UUID.randomUUID())
                    .token(REFRESH_TOKEN)
                    .revoked(false)
                    .expiryDate(LocalDateTime.now().minusDays(1))
                    .user(testUser)
                    .build();

            RefreshTokenRequest request = RefreshTokenRequest.builder()
                    .refreshToken(REFRESH_TOKEN)
                    .build();

            when(refreshTokenRepository.findByToken(REFRESH_TOKEN))
                    .thenReturn(Optional.of(storedToken));
            when(refreshTokenRepository.save(any(RefreshToken.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act & Assert
            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(RefreshTokenExpiredException.class)
                    .hasMessageContaining("expired");
        }
    }

    // ==================== CHANGE PASSWORD ====================

    @Nested
    @DisplayName("Change Password")
    class ChangePassword {

        @Test
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() {
            // Arrange
            ChangePasswordRequest request = ChangePasswordRequest.builder()
                    .currentPassword("OldPass@123")
                    .newPassword("NewPass@456")
                    .build();

            when(userRepository.findByUsername(TEST_USERNAME))
                    .thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches("OldPass@123", testUser.getPassword()))
                    .thenReturn(true);
            when(passwordEncoder.encode("NewPass@456"))
                    .thenReturn("new-encoded-password");
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            authService.changePassword(TEST_USERNAME, request);

            // Assert
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getPassword()).isEqualTo("new-encoded-password");
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            ChangePasswordRequest request = ChangePasswordRequest.builder()
                    .currentPassword("OldPass@123")
                    .newPassword("NewPass@456")
                    .build();

            when(userRepository.findByUsername(TEST_USERNAME))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> authService.changePassword(TEST_USERNAME, request))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(TEST_USERNAME);
        }

        @Test
        @DisplayName("Should throw exception when current password is incorrect")
        void shouldThrowExceptionWhenCurrentPasswordIncorrect() {
            // Arrange
            ChangePasswordRequest request = ChangePasswordRequest.builder()
                    .currentPassword("WrongPass@123")
                    .newPassword("NewPass@456")
                    .build();

            when(userRepository.findByUsername(TEST_USERNAME))
                    .thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches("WrongPass@123", testUser.getPassword()))
                    .thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> authService.changePassword(TEST_USERNAME, request))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Current password is incorrect");
        }
    }

    // ==================== FORGOT PASSWORD ====================

    @Nested
    @DisplayName("Forgot Password")
    class ForgotPassword {

        @Test
        @DisplayName("Should process forgot password request when email exists")
        void shouldProcessForgotPasswordWhenEmailExists() {
            // Arrange
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .email(TEST_EMAIL)
                    .build();
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

            // Act
            authService.forgotPassword(request);

            // Assert
            verify(userRepository).findByEmail(TEST_EMAIL);
        }

        @Test
        @DisplayName("Should silently handle forgot password when email not found")
        void shouldHandleForgotPasswordWhenEmailNotFound() {
            // Arrange
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .email("nonexistent@example.com")
                    .build();
            when(userRepository.findByEmail("nonexistent@example.com"))
                    .thenReturn(Optional.empty());

            // Act
            authService.forgotPassword(request);

            // Assert
            verify(userRepository).findByEmail("nonexistent@example.com");
        }
    }

    // ==================== RESET PASSWORD ====================

    @Nested
    @DisplayName("Reset Password")
    class ResetPassword {

        @Test
        @DisplayName("Should throw exception for null token")
        void shouldThrowExceptionForNullToken() {
            // Arrange
            ResetPasswordRequest request = ResetPasswordRequest.builder()
                    .token(null)
                    .newPassword("NewPass@123")
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> authService.resetPassword(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid reset token");
        }

        @Test
        @DisplayName("Should throw exception for blank token")
        void shouldThrowExceptionForBlankToken() {
            // Arrange
            ResetPasswordRequest request = ResetPasswordRequest.builder()
                    .token("   ")
                    .newPassword("NewPass@123")
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> authService.resetPassword(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid reset token");
        }
    }

    // ==================== VERIFY EMAIL ====================

    @Nested
    @DisplayName("Email Verification")
    class EmailVerification {

        @Test
        @DisplayName("Should return false for verify email (placeholder)")
        void shouldReturnFalseForVerifyEmail() {
            // Act
            boolean result = authService.verifyEmail("some-token");

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should return false for resend verification email (placeholder)")
        void shouldReturnFalseForResendVerificationEmail() {
            // Act
            boolean result = authService.resendVerificationEmail(TEST_EMAIL);

            // Assert
            assertThat(result).isFalse();
        }
    }
}
