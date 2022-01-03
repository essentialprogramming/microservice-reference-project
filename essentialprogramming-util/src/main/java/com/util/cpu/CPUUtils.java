package com.util.cpu;

public class CPUUtils {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    /**
     * Returns true if less then 5% of the available memory is free.
     */
    public static boolean memoryIsLow() {
        return availableMemory() * 100 < RUNTIME.totalMemory() * 5;
    }

    /**
     * Returns the amount of available memory (free memory plus never allocated memory).
     */
    public static long availableMemory() {
        return RUNTIME.freeMemory() + (RUNTIME.maxMemory() - RUNTIME.totalMemory());
    }

    /**
     * Returns the percentage of available memory (free memory plus never allocated memory).

     */
    public static int percentageAvailableMemory() {
        return (int) ((CPUUtils.availableMemory() * 100) / Runtime.getRuntime().maxMemory());
    }

    /**
     * Tries to compact memory as much as possible by forcing garbage collection.
     */
    public static void compactMemory() {
        try {
            final byte[][] unused = new byte[128][];
            for (int i = unused.length; i-- != 0; ) unused[i] = new byte[2000000000];
        } catch (final OutOfMemoryError itsWhatWeWanted) {
            //IGNORE
        }
        System.gc();
    }
}
