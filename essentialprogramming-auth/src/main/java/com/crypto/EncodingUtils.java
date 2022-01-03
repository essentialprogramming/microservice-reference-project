package com.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class EncodingUtils {

    private static final Logger logger = LoggerFactory.getLogger(EncodingUtils.class);

    private static final byte[] HEX = "0123456789abcdef".getBytes(StandardCharsets.UTF_8);

    /**
     * Converts a byte array into a base64 string.
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    static String toBase64(byte[] array) {
        logger.debug("Convert to base64");
        return Base64.getEncoder().encodeToString(array);
    }

    /**
     * Converts a string of base64 characters into a byte array.
     *
     * @param base64 the hex string
     * @return the hex string decoded into a byte array
     */
    static byte[] fromBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }


    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param bytes the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    static String toHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int octet = bytes[i] & 0xFF; //“& 0xFF” effectively masks the variable so it leaves only the value in the last 8 bits, and ignores all the rest of the bits.
            hexChars[i * 2] = HEX[octet >>> 4];
            hexChars[i * 2 + 1] = HEX[octet & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }


    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    public static byte[] fromHex(String hex) {
        int numberOfChars = hex.length();
        if (numberOfChars % 2 != 0) {
            throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
        }
        byte[] binary = new byte[numberOfChars / 2];
        for (int i = 0; i < binary.length; i++) {
            int firstDigit  = Character.digit(hex.charAt(2 * i), 16);
            int secondDigit  = Character.digit(hex.charAt(2 * i + 1), 16);
            if (firstDigit  < 0 || secondDigit  < 0) {
                throw new IllegalArgumentException("Detected a Non-hex character at " + (2 * i) + " or " + (2 * i + 1) + " position");
            }

            binary[i] = (byte)((firstDigit  << 4) | secondDigit );
        }
        return binary;
    }


    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    static boolean slowEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;

        int result = a.length ^ b.length;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }





}
