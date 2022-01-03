package com.crypto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Pbkdf2PasswordEncoder extends AbstractPasswordEncoder {

    final static Logger logger = LoggerFactory.getLogger(Pbkdf2PasswordEncoder.class);

    /**
     * Length of salt for PBKDF2 hashed password
     */
    private static final int PBKDF2_SALT_LENGTH = 16; // bytes
    private static final int PBKDF2_ITERATIONS = 200000;
    private static final int PBKDF2_HASH_BYTE_SIZE = 64;
    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1"; // can also be PBKDF2WithHmacSHA256, PBKDF2WithHmacMD5 etc.

    private static class Pbkdf2PasswordEncoderHolder {
        static final Pbkdf2PasswordEncoder INSTANCE = new Pbkdf2PasswordEncoder();
    }

    public static Pbkdf2PasswordEncoder getInstance() {
        return Pbkdf2PasswordEncoderHolder.INSTANCE;
    }


    /**
     * Returns a salted hash of the password.
     *
     * @param password the password to hash
     * @return a salted hash of the password in the form of SALT:HASH
     * @throws InvalidKeySpecException, NoSuchAlgorithmException
     */
    String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        logger.debug("create hash");

        // Generate a random salt
        byte[] salt = SaltGenerator.init(PBKDF2_SALT_LENGTH).generateSalt();

        // Hash the password
        byte[] hash = encodeWithSalt(password, salt);

        // Format hash:salt:algorithm
        return EncodingUtils.toBase64(hash) + ":" + EncodingUtils.toBase64(salt)+ ":"+ "3";
    }

    private static byte[] encodeWithSalt(char[] password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        logger.debug("Encode with salt");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, PBKDF2_HASH_BYTE_SIZE * 8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HASH_ALGORITHM);

        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }


    /**
     * Verify that the encoded password obtained from storage matches the submitted raw
     * password after the raw password is also encoded. Returns true if the passwords match, false if
     * they do not. The stored password itself is never decoded.
     *
     * @param passwordSequence               the password to check
     * @param encodedPassword the stored hashed password with salt
     * @return true if the password is correct, false if not
     */
    @Override
    public boolean matches(CharSequence passwordSequence, String encodedPassword) {
        String password = passwordSequence.toString();
        String[] parts = encodedPassword.split(":");
        if (parts.length < 2) {
            return false;
        }

        byte[] hash = EncodingUtils.fromBase64(parts[0]);
        byte[] salt = EncodingUtils.fromBase64(parts[1]);


        return matches(password, hash, salt);
    }



    boolean matches(char[] password, byte[] hash, byte[] salt) throws Exception {

        // Compute the hash of the provided password, using the same salt
        byte[] testHash = encodeWithSalt(password, salt);

        // Compare the hashes in constant time. The password is correct if the two hashes match.
        return EncodingUtils.slowEquals(hash, testHash);

    }
}
