package com.util.random;

/**
 * An implementation of Austin Appleby's MurmurHash 3 algorithm.
 *
 * An algorithm designed to generate well-distributed non-cryptographic
 * hashes. It is designed to hash data in 32 bit chunks (ints).
 *
 * The mix method needs to be called at each step to update the intermediate
 * hash value. For the last chunk to incorporate into the hash mixLast may
 * be used instead, which is slightly faster. Finally finalizeHash needs to
 * be called to compute the final hash value.
 *
 * Good collision resistance (passes Bob Jenkin's frog.c torture-test).
 *
 * Great performance on Intel/AMD hardware, good tradeoff between hash quality and CPU consumption.
 *
 * This is public domain code with no copyrights.
 *
 * @see "http://code.google.com/p/smhasher"
 */
public class MurmurHash3 {

    /**
     * Generates 32-bit hash from a byte array and a given seed.
     */
    public static long hash32(byte[] data, long seed) {
        long len = data.length;
        long h = seed;

        // Body
        int i = 0;
        while (len >= 4) {
            long k = data[i] & 0xFF;
            k |= (data[i + 1] & 0xFF) << 8;
            k |= (data[i + 2] & 0xFF) << 16;
            k |= (data[i + 3] & 0xFF) << 24;

            h = mix(h, k);

            i += 4;
            len -= 4;
        }

        // Tail
        int k = 0;
        if (len == 3) k ^= (data[i + 2] & 0xFF) << 16;
        if (len >= 2) k ^= (data[i + 1] & 0xFF) << 8;
        if (len >= 1) {
            k ^= (data[i] & 0xFF);
            h = mixLast(h, k);
        }

        // Finalization
        return finalizeHash(h, data.length);
    }

    /**
     * Finalize a hash to incorporate the length and make sure all bits avalanche.
     */
    private static long finalizeHash(long hash, int length) {
        return avalanche(hash ^ length);
    }

    /**
     * Force all bits of the hash to avalanche. Used for finalizing the hash.
     */
    private static long avalanche(long hash) {
        long x = hash;

        x ^= x >>> 33;
        x *= 0xff51afd7ed558ccdL;
        x ^= x >>> 33;
        x *= 0xc4ceb9fe1a85ec53L;
        x ^= x >>> 33;

        return x;
    }

    /**
     * Mix in a block of data into an intermediate hash value.
     */
    private static long mix(long hash, long data) {
        long h = mixLast(hash, data);
        h = Long.rotateLeft(h, 13);
        return h * 5 + 0xe6546b64;
    }

    /**
     * May optionally be used as the last mixing step. Is a little bit faster than mix,
     * as it does no further mixing of the resulting hash. For the last element this is not
     * necessary as the hash is thoroughly mixed during finalization anyway.
     */
    private static long mixLast(long hash, long data) {
        long k = data;

        k *= 0xcc9e2d51;
        k = Long.rotateLeft(k, 15);
        k *= 0x1b873593;

        return hash ^ k;
    }
}
