package com.crypto;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Argon2PasswordEncoder extends AbstractPasswordEncoder {

    final static Logger logger = LoggerFactory.getLogger(Argon2PasswordEncoder.class);

    private static final int ARGON2_HASH_ALGORITHM = 2;
    private static final int ARGON2_SALT_LENGTH = 16;
    private static final int ARGON2_HASH_LENGTH = 32;
    private static final int ARGON2_PARALLELISM = 1;
    private static final int ARGON2_MEMORY = 65536;
    private static final int ARGON2_ITERATIONS = 2;

    private static final class Argon2PasswordEncoderHolder {
        static final Argon2PasswordEncoder INSTANCE = new Argon2PasswordEncoder();
    }

    public static Argon2PasswordEncoder getInstance() {
        return Argon2PasswordEncoderHolder.INSTANCE;
    }


    /**
     * Generate an encoded password hash value for storage in a user's account.
     *
     * @param password the password to hash
     * @return a hash of the password in the form of HASH:SALT:ALGORITHM
     */
    String createHash(char[] password) {
        logger.debug("create hash");

        // Generate a random salt
        byte[] salt = SaltGenerator.init(ARGON2_SALT_LENGTH).generateSalt();

        // Hash the password
        byte[] hash = encodeWithSalt(password, salt);

        // Format hash:salt:algorithm
        return EncodingUtils.toHex(hash) + ":" + EncodingUtils.toHex(salt) + ":" + "2";
    }

    private static byte[] encodeWithSalt(char[] password, byte[] salt) {
        byte[] hash = new byte[ARGON2_HASH_LENGTH];
        Argon2Parameters params = new Argon2Parameters.Builder(ARGON2_HASH_ALGORITHM)
                .withSalt(salt)
                .withParallelism(ARGON2_PARALLELISM)
                .withMemoryAsKB(ARGON2_MEMORY)
                .withIterations(ARGON2_ITERATIONS)
                .build();
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);
        generator.generateBytes(password, hash);
        return hash;
    }


    boolean matches(char[] password, byte[] hash, byte[] salt) {

        // Compute the hash of the provided password, using the same salt
        byte[] testHash = encodeWithSalt(password, salt);

        // Compare the hashes in constant time. The password is correct if the two hashes match.
        return EncodingUtils.slowEquals(hash, testHash);

    }

}
