package com.crypto;


import java.security.SecureRandom;

@SuppressWarnings("unused")
public class SaltGenerator {

    private final SecureRandom random;
    private final int keyLength;
    private static final int DEFAULT_KEY_LENGTH = 8;

    SaltGenerator() {
        this(DEFAULT_KEY_LENGTH);
    }

    SaltGenerator(int keyLength) {
        this.random = new SecureRandom();
        this.keyLength = keyLength;
    }

    public int getKeyLength() {
        return this.keyLength;
    }

    public byte[] generateSalt() {
        byte[] bytes = new byte[this.keyLength];
        random.nextBytes(bytes);
        return bytes;
    }

    public static SaltGenerator init(int keyLength) {
        return new SaltGenerator(keyLength);
    }

}
