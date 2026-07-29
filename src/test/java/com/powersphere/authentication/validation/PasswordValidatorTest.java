package com.powersphere.authentication.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PasswordValidator Unit Tests")
class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        context = mock(ConstraintValidatorContext.class);
        violationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
    }

    @Nested
    @DisplayName("Valid Passwords")
    class ValidPasswords {

        @Test
        @DisplayName("Should accept password with all requirements")
        void shouldAcceptPasswordWithAllRequirements() {
            assertThat(passwordValidator.isValid("Test@123", context)).isTrue();
        }

        @Test
        @DisplayName("Should accept complex password with multiple special characters")
        void shouldAcceptComplexPassword() {
            assertThat(passwordValidator.isValid("P@ssw0rd!Complex#123", context)).isTrue();
        }

        @Test
        @DisplayName("Should accept password with minimum length meeting all criteria")
        void shouldAcceptMinLengthValidPassword() {
            assertThat(passwordValidator.isValid("Abc@1234", context)).isTrue();
        }

        @Test
        @DisplayName("Should accept password with underscore as special character")
        void shouldAcceptPasswordWithUnderscore() {
            assertThat(passwordValidator.isValid("Secure_Pass1", context)).isTrue();
        }

        @Test
        @DisplayName("Should accept password with all special character types")
        void shouldAcceptPasswordWithAllSpecialChars() {
            assertThat(passwordValidator.isValid("P@ss!word#123$%^&", context)).isTrue();
        }
    }

    @Nested
    @DisplayName("Invalid Passwords")
    class InvalidPasswords {

        @Test
        @DisplayName("Should reject null password")
        void shouldRejectNullPassword() {
            assertThat(passwordValidator.isValid(null, context)).isFalse();
        }

        @Test
        @DisplayName("Should reject empty password")
        void shouldRejectEmptyPassword() {
            assertThat(passwordValidator.isValid("", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password without uppercase letter")
        void shouldRejectPasswordWithoutUppercase() {
            assertThat(passwordValidator.isValid("password@123", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password without lowercase letter")
        void shouldRejectPasswordWithoutLowercase() {
            assertThat(passwordValidator.isValid("PASSWORD@123", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password without number")
        void shouldRejectPasswordWithoutNumber() {
            assertThat(passwordValidator.isValid("Password@test", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password without special character")
        void shouldRejectPasswordWithoutSpecialChar() {
            assertThat(passwordValidator.isValid("Password123", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password with only letters")
        void shouldRejectPasswordWithOnlyLetters() {
            assertThat(passwordValidator.isValid("PasswordTest", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password with only numbers and letters")
        void shouldRejectPasswordWithOnlyNumbersAndLetters() {
            assertThat(passwordValidator.isValid("Password123", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject password with only special characters")
        void shouldRejectPasswordWithOnlySpecialChars() {
            assertThat(passwordValidator.isValid("@#$%^&*()", context)).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should reject single character password")
        void shouldRejectSingleCharacter() {
            assertThat(passwordValidator.isValid("a", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject whitespace only password")
        void shouldRejectWhitespaceOnly() {
            assertThat(passwordValidator.isValid("       ", context)).isFalse();
        }

        @Test
        @DisplayName("Should accept password with numbers at start")
        void shouldAcceptPasswordWithNumbersAtStart() {
            assertThat(passwordValidator.isValid("1234Test@", context)).isTrue();
        }

        @Test
        @DisplayName("Should accept password with special char at start")
        void shouldAcceptPasswordWithSpecialCharAtStart() {
            assertThat(passwordValidator.isValid("@Test1234", context)).isTrue();
        }

        @Test
        @DisplayName("Should reject all lowercase with number and special char (missing uppercase)")
        void shouldRejectAllLowercaseWithNumberAndSpecialChar() {
            assertThat(passwordValidator.isValid("password123!", context)).isFalse();
        }

        @Test
        @DisplayName("Should reject all uppercase with number and special char (missing lowercase)")
        void shouldRejectAllUppercaseWithNumberAndSpecialChar() {
            assertThat(passwordValidator.isValid("PASSWORD123!", context)).isFalse();
        }
    }
}
