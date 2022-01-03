package com.util.text;


import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtils {

    /**
     * An array of pattern defining rules that turn plural into singular (the corresponding replacement string is in the same position in SINGULAR).
     * More specific rules are at the end of the array (and should be tested first).
     */
    private static final Pattern[] PLURAL = {
            Pattern.compile("s$"),
            Pattern.compile("(s|si|u)s$"),
            Pattern.compile("(n)ews$"),
            Pattern.compile("([ti])a$"),
            Pattern.compile("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$"),
            Pattern.compile("(^analy)ses$"),
            Pattern.compile("(^analy)sis$"),
            Pattern.compile("([^f])ves$"),
            Pattern.compile("(hive)s$"),
            Pattern.compile("(tive)s$"),
            Pattern.compile("([lr])ves$"),
            Pattern.compile("([^aeiouy]|qu)ies$"),
            Pattern.compile("(s)eries$"),
            Pattern.compile("(m)ovies$"),
            Pattern.compile("(x|ch|ss|sh)es$"),
            Pattern.compile("([m|l])ice$"),
            Pattern.compile("(bus)es$"),
            Pattern.compile("(o)es$"),
            Pattern.compile("(shoe)s$"),
            Pattern.compile("(cris|ax|test)is$"),
            Pattern.compile("(cris|ax|test)es$"),
            Pattern.compile("(octop|vir)i$"),
            Pattern.compile("(octop|vir)us$"),
            Pattern.compile("(alias|status)es$"),
            Pattern.compile("(alias|status)$"),
            Pattern.compile("^(ox)en"),
            Pattern.compile("(vert|ind)ices$"),
            Pattern.compile("(matr)ices$"),
            Pattern.compile("(quiz)zes$"),
            Pattern.compile("^people$"),
            Pattern.compile("^men$"),
            Pattern.compile("^women$"),
            Pattern.compile("^children$"),
            Pattern.compile("^sexes$"),
            Pattern.compile("^moves$"),
            Pattern.compile("^stadiums$")
    };

    /**
     * Replacement strings for PLURAL's patterns.
     */
    private static final String[] SINGULAR = {
            "",
            "$1s",
            "$1ews",
            "$1um",
            "$1$2sis",
            "$1sis",
            "$1sis",
            "$1fe",
            "$1",
            "$1",
            "$1f",
            "$1y",
            "$1eries",
            "$1ovie",
            "$1",
            "$1ouse",
            "$1",
            "$1",
            "$1",
            "$1is",
            "$1is",
            "$1us",
            "$1us",
            "$1",
            "$1",
            "$1",
            "$1ex",
            "$1ix",
            "$1",
            "person",
            "man",
            "woman",
            "child",
            "sex",
            "move",
            "stadium"
    };

    private StringUtils() {
        // private constructor to hide default public one
    }

    /**
     * @param str String object to examine.
     * @return Returns false if the given string is null or empty. Otherwise true.
     */
    public static boolean isEmpty(final String str) {
        return null == str || 0 == str.length();
    }

    /**
     * @param str String object to examine.
     * @return Returns true if the given string is null or empty. Otherwise false.
     */
    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static boolean hasText(CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the CharSequence contains any character in the given set of characters.
     *
     * @param charSequence the CharSequence to check
     * @param searchChars  the chars to search for
     * @return the {@code true} if any of the chars are found, {@code false} if no match or null input
     */
    public static boolean containsAny(final CharSequence charSequence, final CharSequence searchChars) {
        if (charSequence == null || charSequence.length() == 0 || searchChars == null || searchChars.length() == 0) {
            return false;
        }
        boolean containsAny = false;
        if (charSequence.chars().anyMatch(character ->
                searchChars.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet()).contains((char) character))) {
            containsAny = true;
        }
        return containsAny;
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf);
    }

    public static String encodeText(String text) {
        int ascii = checkAscii(text);
        if (ascii == 1) {
            return text;
        } else {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    static int checkAscii(String s) {
        int ascii = 0;
        int nonAscii = 0;
        int length = s.length();

        for (int i = 0; i < length; ++i) {
            if (nonAscii(s.charAt(i))) {
                ++nonAscii;
            } else {
                ++ascii;
            }
        }

        if (nonAscii == 0) {
            return 1;
        } else if (ascii > nonAscii) {
            return 2;
        } else {
            return 3;
        }
    }

    public static boolean nonAscii(int b) {
        return b >= 127 || b < 32 && b != 13 && b != 10 && b != 9;
    }

    /**
     * Returns the singular form of the given word.
     **/
    public static String singularize(final String word) {
        for (int i = PLURAL.length; i-- != 0; ) {
            final Matcher matcher = PLURAL[i].matcher(word);
            if (matcher.find()) return matcher.replaceFirst(SINGULAR[i]);
        }
        return word;
    }

}
