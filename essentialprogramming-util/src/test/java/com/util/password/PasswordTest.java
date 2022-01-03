package com.util.password;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

public class PasswordTest {

    @Test
    void verify_7_unique_chars_alphanumeric_only() {
        PasswordOptions passwordOptions = PasswordOptions.builder()
                .minimumLength(7)
                .minimumUniqueChars(7)
                .requiresNonAlphanumeric(false)
                .requiresLowercase(false)
                .requiresUppercase(false)
                .requiresDigit(false)
                .requiresAlphanumericOnly(true)
                .excludeAmbiguousCharacters(true)
                .must(containDigits())
                .must(containLetters())
                .build();
        String validPasswordAlphanumericOnly = "Awk357c";
        String invalidPasswordAmbiguousCharacters = "Awk123c";
        String invalidPasswordNotAlphanumericOnly = "Aa1.bCd";
        String invalidPasswordNoDigits = "Aab.bcd";
        String invalidPasswordNoLetters = "2345679";
        String invalidPasswordNoNonUniqueChars = "aa12bCC";

        assert PasswordUtil.isValidPassword(validPasswordAlphanumericOnly, passwordOptions);

        Assertions.assertFalse(PasswordUtil.isValidPassword(invalidPasswordNotAlphanumericOnly, passwordOptions));
        Assertions.assertFalse(PasswordUtil.isValidPassword(invalidPasswordAmbiguousCharacters, passwordOptions));
        Assertions.assertFalse(PasswordUtil.isValidPassword(invalidPasswordNoDigits, passwordOptions));
        Assertions.assertFalse(PasswordUtil.isValidPassword(invalidPasswordNoLetters, passwordOptions));
        Assertions.assertFalse(PasswordUtil.isValidPassword(invalidPasswordNoNonUniqueChars, passwordOptions));
    }

    @Test
    void password_strength() {
        String passwordStrong = "Aa1.bCAwk";
        String passwordMedium = "Aa1.bc";
        String passwordWeak = "Aa1dbc";
        String passwordVeryWeak = "abc";
        String passwordBlank = "";

        Assertions.assertTrue(PasswordUtil.isStrongPassword(passwordStrong));
        Assertions.assertFalse(PasswordUtil.isStrongPassword(passwordMedium));
        Assertions.assertEquals(PasswordStrength.Strong, PasswordUtil.getPasswordStrength(passwordStrong));
        Assertions.assertEquals(PasswordStrength.Medium, PasswordUtil.getPasswordStrength(passwordMedium));
        Assertions.assertEquals(PasswordStrength.Weak, PasswordUtil.getPasswordStrength(passwordWeak));
        Assertions.assertEquals(PasswordStrength.VeryWeak, PasswordUtil.getPasswordStrength(passwordVeryWeak));
        Assertions.assertEquals(PasswordStrength.None, PasswordUtil.getPasswordStrength(passwordBlank));
    }

    @Test
    public void generate_1000_random_passwords() {
        PasswordOptions passwordOptions = buildPasswordOptions(16, false, false);
        for (int i = 0; i < 1000; i++) {
            String password = PasswordUtil.generateRandomPassword(passwordOptions);
            System.out.println("Random password: " + password);
            Assertions.assertEquals(passwordOptions.getRequiredLength(), password.length());
            Assertions.assertTrue(PasswordUtil.isValidPassword(password, passwordOptions));
        }
    }

    @Test
    public void generate_1000_random_passwords_with_default_options() {
        for (int i = 0; i < 1000; i++) {
            String password = PasswordUtil.generateRandomPassword();
            System.out.println("Random password: " + password);
            Assertions.assertTrue(PasswordUtil.isValidPassword(password, PasswordUtil.DEFAULT_PASSWORD_OPTIONS));
        }
    }

    @Test
    public void generate_1000_random_passwords_with_mandatory_non_alphanumeric() {
        PasswordOptions passwordOptions = buildPasswordOptions(16, true, false);
        for (int i = 0; i < 1000; i++) {
            String password = PasswordUtil.generateRandomPassword(passwordOptions);
            System.out.println("Random password: " + password);
            String nonAlphanumerics = "@%+\\/'!#$^?:,(){}[]~-_.";

            boolean containsNonAlphanumeric = StringUtils.containsAny(password, nonAlphanumerics);

            Assertions.assertTrue(containsNonAlphanumeric);
            Assertions.assertEquals(passwordOptions.getRequiredLength(), password.length());
            Assertions.assertTrue(PasswordUtil.isValidPassword(password, passwordOptions));
        }
    }

    @Test
    public void generate_1000_random_passwords_alphanumeric_only() {
        PasswordOptions passwordOptions = buildPasswordOptions(6, false, true);
        for (int i = 0; i < 1000; i++) {
            String password = PasswordUtil.generateRandomPassword(passwordOptions);
            System.out.println("Random password: " + password);
            String nonAlphanumerics = "@%+\\/'!#$^?:,(){}[]~-_.";

            boolean containsNonAlphanumeric = StringUtils.containsAny(password, nonAlphanumerics);

            Assertions.assertFalse(containsNonAlphanumeric);
            Assertions.assertEquals(passwordOptions.getRequiredLength(), password.length());
            Assertions.assertTrue(PasswordUtil.isValidPassword(password, passwordOptions));
        }
    }

    private PasswordOptions buildPasswordOptions(int length, boolean requiresNonAlphanumeric, boolean alphaNumericOnly) {
        return PasswordOptions.builder()
                .minimumLength(length)
                .minimumUniqueChars(length)
                .requiresNonAlphanumeric(requiresNonAlphanumeric)
                .requiresLowercase(true)
                .requiresUppercase(true)
                .requiresDigit(true)
                .requiresAlphanumericOnly(alphaNumericOnly)
                .excludeAmbiguousCharacters(false)
                .build();

    }

    private Predicate<String> containDigits() {
        return s -> s.matches(".*\\d.*");
    }

    private Predicate<String> containLetters() {
        return s -> s.matches(".*[a-zA-Z]+.*");
    }
}
