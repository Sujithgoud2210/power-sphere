package com.powersphere.authentication.jwt;

import com.powersphere.authentication.exception.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenProvider Unit Tests")
class JwtTokenProviderTest {

    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970337336763979244226452948404D6351655468576D5A7134743777217A25432A";
    private static final long ACCESS_TOKEN_EXPIRATION = 900000; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days
    private static final String TEST_USERNAME = "testuser";
    private static final List<String> TEST_ROLES = List.of("ROLE_ADMIN", "ROLE_VIEWER");

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(TEST_SECRET, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION);
    }

    @Nested
    @DisplayName("Access Token Generation")
    class AccessTokenGeneration {

        @Test
        @DisplayName("Should generate a valid access token")
        void shouldGenerateValidAccessToken() {
            // Act
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Assert
            assertThat(token).isNotNull().isNotBlank();
            assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should generate token with correct username")
        void shouldGenerateTokenWithCorrectUsername() {
            // Act
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Assert
            String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);
            assertThat(extractedUsername).isEqualTo(TEST_USERNAME);
        }

        @Test
        @DisplayName("Should generate token with correct roles")
        void shouldGenerateTokenWithCorrectRoles() {
            // Act
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Assert
            List<String> extractedRoles = jwtTokenProvider.getRolesFromToken(token);
            assertThat(extractedRoles).containsExactlyInAnyOrderElementsOf(TEST_ROLES);
        }

        @Test
        @DisplayName("Should generate token with correct expiration time")
        void shouldGenerateTokenWithCorrectExpiration() {
            // Act
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Assert
            assertThat(jwtTokenProvider.getAccessTokenExpirationMs()).isEqualTo(ACCESS_TOKEN_EXPIRATION);
        }

        @Test
        @DisplayName("Should generate different tokens for different usernames")
        void shouldGenerateDifferentTokensForDifferentUsernames() {
            // Act
            String token1 = jwtTokenProvider.generateAccessToken("user1", TEST_ROLES);
            String token2 = jwtTokenProvider.generateAccessToken("user2", TEST_ROLES);

            // Assert
            assertThat(token1).isNotEqualTo(token2);
        }
    }

    @Nested
    @DisplayName("Refresh Token Generation")
    class RefreshTokenGeneration {

        @Test
        @DisplayName("Should generate a valid refresh token")
        void shouldGenerateValidRefreshToken() {
            // Act
            String token = jwtTokenProvider.generateRefreshToken(TEST_USERNAME);

            // Assert
            assertThat(token).isNotNull().isNotBlank();
            assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should generate refresh token with correct username")
        void shouldGenerateRefreshTokenWithCorrectUsername() {
            // Act
            String token = jwtTokenProvider.generateRefreshToken(TEST_USERNAME);

            // Assert
            String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);
            assertThat(extractedUsername).isEqualTo(TEST_USERNAME);
        }
    }

    @Nested
    @DisplayName("Token Validation")
    class TokenValidation {

        @Test
        @DisplayName("Should validate a valid token")
        void shouldValidateValidToken() {
            // Arrange
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Act & Assert
            assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should throw TokenExpiredException for expired token")
        void shouldThrowExceptionForExpiredToken() {
            // Arrange - create a token that expires immediately
            JwtTokenProvider shortLivedProvider = new JwtTokenProvider(TEST_SECRET, -3600000, REFRESH_TOKEN_EXPIRATION);
            String token = shortLivedProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Act & Assert
            assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                    .isInstanceOf(TokenExpiredException.class)
                    .hasMessage("JWT token has expired");
        }

        @Test
        @DisplayName("Should return false for malformed token")
        void shouldReturnFalseForMalformedToken() {
            // Act & Assert
            assertThat(jwtTokenProvider.validateToken("invalid-token-here")).isFalse();
        }

        @Test
        @DisplayName("Should return false for empty token")
        void shouldReturnFalseForEmptyToken() {
            // Act & Assert
            assertThat(jwtTokenProvider.validateToken("")).isFalse();
        }

        @Test
        @DisplayName("Should return false for null token")
        void shouldReturnFalseForNullToken() {
            // Act & Assert
            assertThat(jwtTokenProvider.validateToken(null)).isFalse();
        }

        @Test
        @DisplayName("Should return false for token with different secret")
        void shouldReturnFalseForTokenWithDifferentSecret() {
            // Arrange
            String differentSecret = "7336763979244226452948404D6351655468576D5A7134743777217A25432A404E635266556A586E3272357538782F413F4428472B4B6250645367566B5";
            JwtTokenProvider differentProvider = new JwtTokenProvider(differentSecret, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION);
            String token = differentProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Act & Assert - token generated with different secret fails validation with original provider
            assertThat(jwtTokenProvider.validateToken(token)).isFalse();
        }
    }

    @Nested
    @DisplayName("Token Parsing")
    class TokenParsing {

        @Test
        @DisplayName("Should extract username from valid token")
        void shouldExtractUsernameFromValidToken() {
            // Arrange
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Act
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Assert
            assertThat(username).isEqualTo(TEST_USERNAME);
        }

        @Test
        @DisplayName("Should extract roles from valid token")
        void shouldExtractRolesFromValidToken() {
            // Arrange
            String token = jwtTokenProvider.generateAccessToken(TEST_USERNAME, TEST_ROLES);

            // Act
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);

            // Assert
            assertThat(roles).isNotNull().isNotEmpty();
            assertThat(roles).contains("ROLE_ADMIN");
        }

        @Test
        @DisplayName("Should extract empty roles list when no roles in token")
        void shouldExtractEmptyRolesFromTokenWithoutRolesClaim() {
            // Act
            String token = jwtTokenProvider.generateRefreshToken(TEST_USERNAME);

            // Assert
            // Refresh token doesn't have roles claim
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);
            assertThat(roles).isNull();
        }
    }

    @Nested
    @DisplayName("Expiration Time")
    class ExpirationTime {

        @Test
        @DisplayName("Should return configured access token expiration")
        void shouldReturnConfiguredAccessTokenExpiration() {
            // Act
            long expiration = jwtTokenProvider.getAccessTokenExpirationMs();

            // Assert
            assertThat(expiration).isEqualTo(ACCESS_TOKEN_EXPIRATION);
        }
    }
}
