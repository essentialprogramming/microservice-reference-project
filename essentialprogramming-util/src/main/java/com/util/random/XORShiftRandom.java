package com.util.random;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;


/**
 * A fast, still solid, {@linkplain Random pseudorandom number generator}.
 * <p>Note that this is not a {@linkplain SecureRandom secure generator}.
 *
 */
public class XORShiftRandom extends Random {
    public static final long serialVersionUID = 1;


    private long seed;

    public XORShiftRandom() {
        long nanoTime = System.nanoTime();
        this.seed = hashSeed(nanoTime);
    }


    public long nextLong() {
        long nextSeed = Long.rotateLeft(seed, 17);
        nextSeed ^= (nextSeed << 21);
        nextSeed ^= (nextSeed >>> 7);
        nextSeed ^= (nextSeed << 3);

        seed = nextSeed;
        return nextSeed;
    }

    /**
     * Generates the next pseudorandom number.
     * Uses XORShift technique for generating high-quality random numbers.
     *
     * @param bits random bits
     * @return the next pseudorandom value
     */
    @Override
    protected int next(int bits) {
        return (int) (nextLong() & ((1L << bits) - 1));
    }


    @Override
    public boolean nextBoolean() {
        return nextLong() < 0;
    }

    @Override
    public void nextBytes(final byte[] bytes) {
        int i = bytes.length;
        int n;
        while (i != 0) {
            n = Math.min(i, 8);
            for (long bits = nextLong(); n-- != 0; bits >>= 8) bytes[--i] = (byte) bits;
        }
    }

    private static long hashSeed(long seed) {
        try {
            byte[] bytes = ByteBuffer.allocate(java.lang.Long.BYTES).putLong(seed).array();
            long lowBits = MurmurHash3.hash32(bytes, seed);
            long highBits = MurmurHash3.hash32(bytes, lowBits);
            return (highBits << 32) | (lowBits & 0xFFFFFFFFL);
        } catch (Exception exception) {
            return seed;
        }
    }

    public static XORShiftRandom instance(){
        return new XORShiftRandom();
    }
}