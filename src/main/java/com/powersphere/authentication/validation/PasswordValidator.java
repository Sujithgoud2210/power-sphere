package com.powersphere.authentication.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String UPPERCASE_PATTERN = ".*[A-Z].*";
    private static final String LOWERCASE_PATTERN = ".*[a-z].*";
    private static final String DIGIT_PATTERN = ".*\\d.*";
    private static final String SPECIAL_CHAR_PATTERN = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        boolean hasUppercase = password.matches(UPPERCASE_PATTERN);
        boolean hasLowercase = password.matches(LOWERCASE_PATTERN);
        boolean hasDigit = password.matches(DIGIT_PATTERN);
        boolean hasSpecialChar = password.matches(SPECIAL_CHAR_PATTERN);

        context.disableDefaultConstraintViolation();

        if (!hasUppercase) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one uppercase letter").addConstraintViolation();
            return false;
        }
        if (!hasLowercase) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one lowercase letter").addConstraintViolation();
            return false;
        }
        if (!hasDigit) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one number").addConstraintViolation();
            return false;
        }
        if (!hasSpecialChar) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one special character").addConstraintViolation();
            return false;
        }

        return true;
    }
}
