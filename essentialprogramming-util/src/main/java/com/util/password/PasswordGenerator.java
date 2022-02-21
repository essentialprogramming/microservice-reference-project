package com.util.password;

import com.util.collection.CollectionUtils;
import com.util.random.XORShiftRandom;

import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Creates passwords that meet required options.
 */
public class PasswordGenerator {

    public static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String DIGITS = "1234567890";
    public static final String SYMBOLS = "@%+\\/'!#$^?:,(){}[]~-_.";
    public static final String AMBIGUOUS_CHARS = "0oOiIl1";

    public static final List<Character> AMBIGUOUS_CHARS_LIST = Collections
            .unmodifiableList(Arrays.asList('0', 'o', 'O', 'i', 'I', 'l', '1'));
    private static final String ALL_CHARS = UPPERCASE_LETTERS + LOWERCASE_LETTERS + DIGITS;
    private static final String[] CHARACTER_SETS = new String[]{UPPERCASE_LETTERS, LOWERCASE_LETTERS, DIGITS, SYMBOLS};

    /**
     * Source of random data.
     */
    private final Random random;
    /**
     * Options which passwords shall meet.
     */
    private final PasswordOptions options;

    private PasswordGenerator(final PasswordOptions options, final Random random) {
        this.random = random;
        this.options = options;
        validatePasswordOptions(options);
    }

    private static void validatePasswordOptions(final PasswordOptions options) {
        final int count = Boolean.compare(options.isRequiredUppercase(), Boolean.FALSE) + Boolean.compare(options.isRequiredLowercase(), Boolean.FALSE) + Boolean.compare(options.isRequiredDigit(), Boolean.FALSE) + Boolean.compare(options.isRequireNonAlphanumeric(), Boolean.FALSE);
        if (count > options.getRequiredLength())
            throw new IllegalArgumentException("Current length cannot satisfy all required conditions");
    }

    /**
     * Generate a password randomly with help from a random number generator.
     * Steps:
     * 1. Determine which character sets to include based on given options
     * 2. Randomly choose a character set to select a character from
     * 3. Randomly choose a character from that randomly chosen character set
     * 4. Repeat steps 2 and 3 (in that order) for as many times as the password's length
     */
    public String generatePassword() {

        final CharBuffer buffer = CharBuffer.allocate(options.getRequiredLength());
        if (options.isRequiredUppercase()) {
            buffer.append(randomChar(random, UPPERCASE_LETTERS, options.excludeAmbiguousCharacters()));
        }
        if (options.isRequiredLowercase()) {
            buffer.append(randomChar(random, LOWERCASE_LETTERS, options.excludeAmbiguousCharacters()));
        }
        if (options.isRequiredDigit()) {
            buffer.append(randomChar(random, DIGITS, options.excludeAmbiguousCharacters()));
        }
        if (options.isRequireNonAlphanumeric()) {
            buffer.append(randomChar(random, SYMBOLS, options.excludeAmbiguousCharacters()));
        }
        for (int i = buffer.position(); i < buffer.limit(); i++) {
            buffer.append(generateRandomChar(random, options.isAlphanumericOnly(), options.excludeAmbiguousCharacters()));
        }

        //Extract password from buffer
        ((Buffer) buffer).flip();
        final String generatedPassword = buffer.toString();


        //Handle duplicate characters
        final List<Character> chars = new ArrayList<>(generatedPassword.length());
        final Set<Character> charsSet = new HashSet<>(generatedPassword.length());
        IntStream.range(0, generatedPassword.length())
                .forEach(index -> {
                    final Character character = generatedPassword.charAt(index);
                    chars.add(character);
                    charsSet.add(character);
                });

        final int duplicateChars = chars.size() - charsSet.size();
        final Map<Character, Long> frequencyMap = chars.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        if (chars.size() - duplicateChars < options.getRequiredUniqueChars()) {
            IntStream.range(0, chars.size()).forEach(index -> {
                final Character character = chars.get(index);
                if (frequencyMap.get(character) > 1) {
                    Character generated = generateRandomChar(random, options.isAlphanumericOnly(), options.excludeAmbiguousCharacters(), chars);

                    //frequencyMap.merge(character, 1L, (oldValue, value) -> oldValue - 1);
                    frequencyMap.computeIfPresent(character, (k, v) -> v - 1);
                    chars.set(index, generated);
                }
            });
        }
        Collections.shuffle(chars);

        /*
        final StringBuilder builder = new StringBuilder(chars.size());
        chars.forEach(builder::append);
        return builder.toString();
        */

        //return chars.stream().map(String::valueOf).collect(Collectors.joining());
        return chars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    private static Character generateRandomChar(Random random, boolean alphanumericOnly, boolean excludeAmbiguousCharacters) {
        return generateRandomChar(random, alphanumericOnly, excludeAmbiguousCharacters, Collections.emptyList());
    }

    private static Character generateRandomChar(Random random, boolean alphanumericOnly, boolean excludeAmbiguousCharacters, List<Character> charactersToExclude) {
        final int length = CHARACTER_SETS.length - 1;
        int randomCharSetMaxIndex = alphanumericOnly ? length - 1 : length;

        if (excludeAmbiguousCharacters) {
            charactersToExclude = CollectionUtils.concat(charactersToExclude, AMBIGUOUS_CHARS_LIST);
        }
        String characters = CHARACTER_SETS[random.nextInt(randomCharSetMaxIndex)];
        characters = excludeCharacters(characters, charactersToExclude);


        if (characters.length() == 0) {
            characters = excludeCharacters(ALL_CHARS, charactersToExclude);
        }
        return randomChar(random, characters, false); //excludeAmbiguousCharacters is always false because ambiguous characters have already been removed in this method
    }


    private static Character randomChar(Random random, String alphabet, boolean excludeAmbiguousCharacters) {
        final String characters = excludeAmbiguousCharacters ? alphabet.replaceAll("[" + AMBIGUOUS_CHARS + "]", "") : alphabet;

        int randomIndex = random.nextInt(characters.length());
        return characters.charAt(randomIndex);
    }

    private static String excludeCharacters(final String input, final List<Character> charactersToExclude) {
        if (charactersToExclude.size() == 0) {
            return input;
        }
        final List<Character> inputList = input.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        inputList.removeAll(charactersToExclude);

        /*
        StringBuilder inputCharsListToString = new StringBuilder();
        inputList.forEach(inputCharsListToString::append);
        return inputCharsListToString.toString();
         */
        return inputList.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();

    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }


    public static class Builder<T> {
        private Random random;
        private PasswordOptions passwordOptions;

        public Builder() {
            random = new XORShiftRandom();
        }

        public Builder<T> usingRandom(final Random random) {
            this.random = random;
            return this;
        }

        public Builder<T> withOptions(final PasswordOptions passwordOptions) {
            this.passwordOptions = passwordOptions;
            return this;
        }

        public PasswordGenerator build() {
            return new PasswordGenerator(passwordOptions, random);
        }
    }
}
