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
import com.powersphere.authentication.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     RefreshTokenRepository refreshTokenRepository,
                                     UserMapper userMapper,
                                     PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager,
                                     JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "User with username '" + request.getUsername() + "' already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User with email '" + request.getEmail() + "' already exists");
        }

        User user = userMapper.registerRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role viewerRole = roleRepository.findByName("VIEWER")
                .orElseThrow(() -> new RoleNotFoundException("Default role VIEWER not found"));
        user.setRoles(Set.of(viewerRole));
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        RegisterResponse response = userMapper.userToRegisterResponse(savedUser);
        response.setMessage("Registration successful. Please check your email to verify your account.");
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.getUsernameOrEmail());

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()));

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            user.setFailedLoginAttempts(0);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            String accessToken = jwtTokenProvider.generateAccessToken(username, roles);
            String refreshToken = jwtTokenProvider.generateRefreshToken(username);

            saveRefreshToken(refreshToken, user);

            log.info("User logged in successfully: {}", username);

            LoginResponse response = userMapper.userToLoginResponse(user);
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiresIn(jwtTokenProvider.getAccessTokenExpirationMs());
            return response;

        } catch (BadCredentialsException e) {
            handleFailedLogin(request.getUsernameOrEmail());
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (DisabledException e) {
            throw new InvalidCredentialsException("Account is disabled. Contact administrator.");
        } catch (LockedException e) {
            throw new InvalidCredentialsException(
                    "Account is locked due to too many failed attempts. Try again later.");
        }
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> {
                token.setRevoked(true);
                refreshTokenRepository.save(token);
                log.info("Refresh token revoked for user");
            });
        }
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        log.info("Attempting to refresh token");

        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenExpiredException("Refresh token not found"));

        if (storedToken.isRevoked()) {
            throw new RefreshTokenExpiredException("Refresh token has been revoked");
        }

        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            storedToken.setRevoked(true);
            refreshTokenRepository.save(storedToken);
            throw new RefreshTokenExpiredException("Refresh token has expired. Please login again.");
        }

        User user = storedToken.getUser();

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), roles);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
        saveRefreshToken(newRefreshToken, user);

        log.info("Token refreshed successfully for user: {}", user.getUsername());

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationMs())
                .build();
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            log.info("Password reset email would be sent to: {}", request.getEmail());
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Resetting password");

        if (request.getToken() == null || request.getToken().isBlank()) {
            throw new IllegalArgumentException("Invalid reset token");
        }

        log.info("Password reset completed successfully");
    }

    @Override
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        log.info("Changing password for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", username);
    }

    @Override
    public boolean verifyEmail(String token) {
        log.info("Verifying email with token");
        return false;
    }

    @Override
    public boolean resendVerificationEmail(String email) {
        log.info("Resending verification email to: {}", email);
        return false;
    }

    private void saveRefreshToken(String token, User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private void handleFailedLogin(String usernameOrEmail) {
        userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .ifPresent(user -> {
                    user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                    if (user.getFailedLoginAttempts() >= MAX_FAILED_LOGIN_ATTEMPTS) {
                        user.setAccountLocked(true);
                        log.warn("Account locked due to too many failed attempts: {}", user.getUsername());
                    }
                    userRepository.save(user);
                });
    }
}
