package com.util.random;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.util.logging.ProgressLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class XORShiftRandomTest {

    @Test
    public void all_10000000_random_generated_IDs_are_unique() {
        final ProgressLogger progressLogger = new ProgressLogger("nounces")
                .displayFreeMemory(true)
                .displayLocalSpeed(true)
                .logFrequency(3, TimeUnit.SECONDS)
                .expectedUpdates(10000000);
        progressLogger.start("Generating nounces...");

        final Set<String> randomSet = new HashSet<>();
        for (int i = 10000000; i-- != 0; ) {
            final Random random = new XORShiftRandom();
            final String nonce = NanoIdUtils.randomNanoId(random,
                    NanoIdUtils.DEFAULT_ALPHABET,
                    NanoIdUtils.DEFAULT_SIZE);
            randomSet.add(nonce);

            progressLogger.update();
        }
        progressLogger.done();
        Assertions.assertEquals(10000000, randomSet.size());

    }
}
