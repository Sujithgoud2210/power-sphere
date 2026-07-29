package com.powersphere.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
}
