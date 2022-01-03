package com.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPasswordEncoder extends AbstractPasswordEncoder {

    final static Logger logger = LoggerFactory.getLogger(HashPasswordEncoder.class);

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 32; // bytes

    private static final class HashPasswordEncoderHolder {
        static final HashPasswordEncoder INSTANCE = new HashPasswordEncoder();
    }

    public static HashPasswordEncoder getInstance() {
        return HashPasswordEncoderHolder.INSTANCE;
    }


    /**
     * Returns a salted hash of the password.
     *
     * @param password the password to hash
     * @return a salted hash of the password in the form of HASH:SALT:ALGORITHM
     * @throws NoSuchAlgorithmException hash algorithm does not exist
     */
    String createHash(char[] password) throws NoSuchAlgorithmException {
        logger.debug("create hash");

        // Generate a random salt
        byte[] salt = SaltGenerator.init(SALT_LENGTH).generateSalt();

        // Hash the password
        byte[] hash = encodeWithSalt(password, salt);

        // Format hash:salt:algorithm
        return EncodingUtils.toHex(hash) + ":" + EncodingUtils.toHex(salt) + ":" + "1";
    }


    private static byte[] encodeWithSalt(char[] password, byte[] salt) throws NoSuchAlgorithmException {
        byte[] passwordBytes = new String(password).getBytes();
        //byte[] saltPassword = new byte[salt.length + passwordBytes.length];
        //System.arraycopy(salt, 0, saltPassword, 0, salt.length);
        //System.arraycopy(passwordBytes, 0, saltPassword, salt.length, passwordBytes.length);

        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.update(salt);
        return md.digest(passwordBytes);

    }


    /**
     * Validates a password using a hash.
     *
     * @param password the password to check
     * @param hash     Previously hashed password
     * @param salt     The salt used for the derivation
     * @return true if the password is correct, false if not
     */
    boolean matches(char[] password, byte[] hash, byte[] salt) throws Exception{

        // Compute the hash of the provided password, using the same salt
        byte[] testHash = encodeWithSalt(password, salt);

        // Compare the hashes in constant time. The password is correct if the two hashes match.
        return EncodingUtils.slowEquals(hash, testHash);

    }

}
