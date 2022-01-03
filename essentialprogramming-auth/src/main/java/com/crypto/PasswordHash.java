package com.crypto;


import com.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Generate and verify password hashes. Note that it is impossible to discover the password
 * from the hash and salt in any reasonable time frame.
 * <p/>
 * See https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 */
public final class PasswordHash {

    private static final Logger logger = LoggerFactory.getLogger(PasswordHash.class);

    private final static Map<Integer, PasswordEncoder> passwordEncoder = new HashMap<>();
    static {
        passwordEncoder.put(1, HashPasswordEncoder.getInstance());
        passwordEncoder.put(2, Argon2PasswordEncoder.getInstance());
        passwordEncoder.put(3, Pbkdf2PasswordEncoder.getInstance());
        passwordEncoder.put(4, HashHmacPasswordEncoder.getInstance());
    }

    private PasswordHash() {
    }

    /**
     * Verify that the encoded password obtained from storage matches the submitted raw
     * password after the raw password is also encoded. Returns true if the passwords match, false if
     * they do not. The stored password itself is never decoded.
     *
     * @param password                       the password to check
     * @param encodedPassword the stored hashed password with salt
     * @return true if the password is correct, false if not
     */
    public static boolean matches(String password, String encodedPassword)  {
        if (StringUtils.isEmpty(encodedPassword)) {
            logger.warn("password hash is null");
            return false;
        }
        final String[] params = encodedPassword.split(":");
        final Integer alg = Integer.valueOf(params[2]);

        return passwordEncoder.getOrDefault(alg, Pbkdf2PasswordEncoder.getInstance()).matches(password, encodedPassword);
    }

    /**
     * Generate an encoded password hash.
     *
     * @param password the password to hash
     * @return a salted hash of the password
     */
    public static String encode(String password) {
        logger.debug("Encoding password");
        return Argon2PasswordEncoder.getInstance().encode(password);
    }

    public static String encode(String password, int algorithm) {
        logger.debug("Encoding password");
        return passwordEncoder.getOrDefault(algorithm, Argon2PasswordEncoder.getInstance()).encode(password);
    }

}
