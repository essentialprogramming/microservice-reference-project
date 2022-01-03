package com.crypto.util;


import com.util.random.XORShiftRandom;
import java.util.Random;

public class IvGenerator {

    private final Random random;
    private final int keyLength;
    private static final int DEFAULT_KEY_LENGTH = 12;

    IvGenerator() {
        this(DEFAULT_KEY_LENGTH);
    }

    IvGenerator(int keyLength) {
        this.random = new XORShiftRandom();
        this.keyLength = keyLength;
    }

    public int getKeyLength() {
        return this.keyLength;
    }

    public byte[] generateKey() {
        byte[] bytes = new byte[this.keyLength];
        random.nextBytes(bytes);
        return bytes;
    }

    public static IvGenerator init(int keyLength) {
        return new IvGenerator(keyLength);
    }
}
