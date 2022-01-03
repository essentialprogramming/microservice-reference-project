package com.crypto;

import com.util.cloud.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class HashHmacPasswordEncoder extends AbstractPasswordEncoder {

    final static Logger logger = LoggerFactory.getLogger(HashHmacPasswordEncoder.class);

    public static final String SECRET_KEY = "hashHMACSecretKey";

    private static final String HASH_ALGORITHM = "HMACSHA256";
    private static final int SALT_LENGTH = 32; // bytes


    private static final class HashHmacPasswordEncoderHolder {
        static final HashHmacPasswordEncoder INSTANCE = new HashHmacPasswordEncoder();
    }

    public static HashHmacPasswordEncoder getInstance() {
        return HashHmacPasswordEncoderHolder.INSTANCE;
    }


    /**
     * Returns a salted hash of the password.
     *
     * @param password the password to hash
     * @return a salted hash of the password in the form of HASH:SALT:ALGORITHM
     * @throws InvalidKeyException, NoSuchAlgorithmException
     */
    String createHash(char[] password) throws InvalidKeyException, NoSuchAlgorithmException {
        logger.debug("create hash");

        // Generate a random salt
        byte[] salt = SaltGenerator.init(SALT_LENGTH).generateSalt();

        // Hash the password
        byte[] hash = encodeWithSalt(password, salt);

        // Format hash:salt:algorithm
        return EncodingUtils.toHex(hash) + ":" + EncodingUtils.toHex(salt) + ":" + "4";
    }

    private static byte[] encodeWithSalt(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeyException {
        Key sk = new SecretKeySpec(Environment.getProperty(SECRET_KEY, "secret").getBytes(), HASH_ALGORITHM);
        Mac mac = Mac.getInstance(sk.getAlgorithm());
        mac.init(sk);
        byte[] passwordBytes = new String(password).getBytes();
        byte[] saltPassword = new byte[salt.length + passwordBytes.length];
        System.arraycopy(salt, 0, saltPassword, 0, salt.length);
        System.arraycopy(passwordBytes, 0, saltPassword, salt.length, passwordBytes.length);
        return mac.doFinal(saltPassword);
    }


    boolean matches(char[] password, byte[] hash, byte[] salt) throws Exception {

        // Compute the hash of the provided password, using the same salt
        byte[] testHash = encodeWithSalt(password, salt);

        // Compare the hashes in constant time. The password is correct if the two hashes match.
        return EncodingUtils.slowEquals(hash, testHash);

    }

}
