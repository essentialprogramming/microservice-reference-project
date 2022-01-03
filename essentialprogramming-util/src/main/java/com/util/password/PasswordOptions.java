package com.util.password;


import lombok.*;

import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
public class PasswordOptions {

    //     Minimum required length. Defaults to 6.
    public int requiredLength;

    //     Minimum number of unique chars. Defaults to 1.
    public int requiredUniqueChars;

    //     Flag indicating if passwords must contain a non-alphanumeric character. Defaults to false.
    public boolean requireNonAlphanumeric;

    //     Flag indicating if passwords must contain a lower case ASCII character. Defaults to true.
    public boolean requiredLowercase;

    //     Flag indicating if passwords must contain a upper case ASCII character. Defaults to true.
    public boolean requiredUppercase;

    //     Flag indicating if passwords must contain a digit. Defaults to true.
    public boolean requiredDigit;

    //     Flag indicating if passwords shall be alphanumeric only. Defaults to false.
    public boolean alphanumericOnly;

    public boolean excludeAmbiguousCharacters;

    public Predicate<String> customCondition;

    public static <T> PasswordOptions.Builder<T> builder() {
        return new PasswordOptions.Builder<>();
    }


    public boolean excludeAmbiguousCharacters(){
        return excludeAmbiguousCharacters;
    }

    public static class Builder<T> {
        private int requiredLength;
        private int requiredUniqueChars;
        private boolean requiresNonAlphanumeric;
        private boolean requiresLowercase;
        private boolean requiresUppercase;
        private boolean requiresDigit;
        private boolean requiresAlphanumericOnly;
        private boolean excludeAmbiguousCharacters;
        private Predicate<String> customCondition;

        public Builder() {
            this.requiredLength = 6;
            this.requiredUniqueChars = 6;
            this.requiresNonAlphanumeric = false;
            this.requiresAlphanumericOnly = false;
            this.requiresLowercase = true;
            this.requiresUppercase = true;
            this.requiresDigit = true;
            this.excludeAmbiguousCharacters = false;

        }

        public PasswordOptions.Builder<T> minimumLength(int requiredLength) {
            this.requiredLength = requiredLength;
            return this;
        }

        public PasswordOptions.Builder<T> minimumUniqueChars(int requiredUniqueChars) {
            this.requiredUniqueChars = requiredUniqueChars;
            return this;
        }

        public PasswordOptions.Builder<T> requiresNonAlphanumeric(boolean requireNonAlphanumeric) {
            this.requiresNonAlphanumeric = requireNonAlphanumeric;
            return this;
        }

        public PasswordOptions.Builder<T> requiresAlphanumericOnly(boolean alphanumericOnly) {
            this.requiresAlphanumericOnly = alphanumericOnly;
            return this;
        }

        public PasswordOptions.Builder<T> requiresLowercase(boolean requireLowercase) {
            this.requiresLowercase = requireLowercase;
            return this;
        }

        public PasswordOptions.Builder<T> requiresUppercase(boolean requireUppercase) {
            this.requiresUppercase = requireUppercase;
            return this;
        }

        public PasswordOptions.Builder<T> requiresDigit(boolean requireDigit) {
            this.requiresDigit = requireDigit;
            return this;
        }

        public PasswordOptions.Builder<T> excludeAmbiguousCharacters(boolean excludeAmbiguousCharacters) {
            this.excludeAmbiguousCharacters = excludeAmbiguousCharacters;
            return this;
        }


        public PasswordOptions.Builder<T> must(Predicate<String> condition) {
            this.customCondition = this.customCondition == null ? condition : this.customCondition.and(condition);
            return this;
        }

        public PasswordOptions build() {
            return new PasswordOptions(requiredLength, requiredUniqueChars, requiresNonAlphanumeric, requiresLowercase, requiresUppercase, requiresDigit, requiresAlphanumericOnly, excludeAmbiguousCharacters, customCondition);
        }
    }

}
