package com.util.password;

import com.util.random.XORShiftRandom;
import com.util.text.StringUtils;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PasswordUtil {


    public static final PasswordOptions DEFAULT_PASSWORD_OPTIONS = new PasswordOptions(6, 6, false, true, true, true, false, false, null);

    public static String generateRandomPassword() {
        PasswordGenerator passwordGenerator = PasswordGenerator.builder()
                .withOptions(DEFAULT_PASSWORD_OPTIONS)
                .usingRandom(new XORShiftRandom())
                .build();
        return passwordGenerator.generatePassword();
    }

    public static String generateRandomPassword(final PasswordOptions passwordOptions) {
        PasswordGenerator passwordGenerator = PasswordGenerator.builder()
                .withOptions(passwordOptions)
                .usingRandom(new XORShiftRandom())
                .build();
        return passwordGenerator.generatePassword();
    }

    public static PasswordStrength getPasswordStrength(String password) {
        int score = 0;
        if (password == null || password.trim().isEmpty())
            return PasswordStrength.None;
        if (!hasMinimumLength(password, 5))
            return PasswordStrength.VeryWeak;
        if (hasMinimumLength(password, 8))
            score++;
        if (containsUpperCaseLetter(password) && containsLowerCaseLetter(password))
            score++;
        if (containsDigit(password))
            score++;
        if (hasSpecialChar(password))
            score++;
        return PasswordStrength.get(score);
    }


    /**
     * Requirements for a strong password:
     * - minimum 8 characters
     * - at lease one UC letter
     * - at least one LC letter
     * - at least one non-letter char (digit OR special char)
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        return hasMinimumLength(password, 8) && containsUpperCaseLetter(password) && containsLowerCaseLetter(password)
                && (containsDigit(password) || hasSpecialChar(password));
    }


    public static boolean isValidPassword(final String password, final PasswordOptions options) {
        return isValidPassword(password,
                options.getRequiredLength(),
                options.getRequiredUniqueChars(),
                options.isAlphanumericOnly(),
                options.isRequireNonAlphanumeric(),
                options.isRequiredLowercase(),
                options.isRequiredUppercase(),
                options.isRequiredDigit(),
                options.excludeAmbiguousCharacters(),
                options.getCustomCondition());
    }

    private static boolean isValidPassword(final String password, int requiredLength, int requiredUniqueChars, boolean alphanumericOnly, boolean requireNonAlphanumeric,
                                           boolean requireLowercase, boolean requireUppercase, boolean requireDigit, boolean excludeAmbiguousCharacters, Predicate<String> predicate) {
        if (password == null || password.isEmpty())
            return false;
        if (!hasMinimumLength(password, requiredLength))
            return false;
        if (!hasMinimumUniqueChars(password, requiredUniqueChars))
            return false;
        if (requireNonAlphanumeric && !hasSpecialChar(password))
            return false;
        if (alphanumericOnly && hasSpecialChar(password))
            return false;
        if (requireLowercase && !containsLowerCaseLetter(password))
            return false;
        if (requireUppercase && !containsUpperCaseLetter(password))
            return false;
        if (requireDigit && !containsDigit(password))
            return false;
        if (excludeAmbiguousCharacters && containsAmbiguousCharacters(password))
            return false;
        return predicate == null || predicate.test(password);
    }

    private static boolean hasMinimumLength(String password, int minLength) {
        return password.length() >= minLength;
    }


    private static boolean hasMinimumUniqueChars(String password, int minUniqueChars) {
        return password.chars().distinct().count() >= minUniqueChars;
    }


    /**
     * Returns TRUE if the password contains at least one uppercase letter
     */
    private static boolean containsUpperCaseLetter(final String password) {
        return password.chars().filter(Character::isUpperCase).findAny().isPresent();
    }

    /**
     * Returns TRUE if the password contains at least one lowercase letter
     */
    private static boolean containsLowerCaseLetter(final String password) {
        return password.chars().filter(Character::isLowerCase).findAny().isPresent();
    }

    /**
     * Returns TRUE if the password has at least one digit
     */
    private static boolean containsDigit(String password) {
        return password.chars().filter(Character::isDigit).findAny().isPresent();
    }

    /**
     * Returns TRUE if the password has at least one special character
     */
    private static boolean hasSpecialChar(String password) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        return m.find();
    }

    /**
     * Returns TRUE if the password contains ambiguous characters
     */
    private static boolean containsAmbiguousCharacters(String password) {
        return StringUtils.containsAny(password, PasswordGenerator.AMBIGUOUS_CHARS);
    }


}
