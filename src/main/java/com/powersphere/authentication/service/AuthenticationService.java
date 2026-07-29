package com.powersphere.authentication.service;

import com.powersphere.authentication.dto.request.ChangePasswordRequest;
import com.powersphere.authentication.dto.request.ForgotPasswordRequest;
import com.powersphere.authentication.dto.request.LoginRequest;
import com.powersphere.authentication.dto.request.RefreshTokenRequest;
import com.powersphere.authentication.dto.request.RegisterRequest;
import com.powersphere.authentication.dto.request.ResetPasswordRequest;
import com.powersphere.authentication.dto.response.JwtResponse;
import com.powersphere.authentication.dto.response.LoginResponse;
import com.powersphere.authentication.dto.response.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void logout(String refreshToken);

    JwtResponse refreshToken(RefreshTokenRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void changePassword(String username, ChangePasswordRequest request);

    boolean verifyEmail(String token);

    boolean resendVerificationEmail(String email);
}
