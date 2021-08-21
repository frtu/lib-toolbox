package com.github.frtu.test.resilience;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Randomly generate issues.
 *
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/log-platform/blob/master/sample-microservices/service-b/src/main/java/com/github/frtu/logs/example/demo/biz/ChaosGenerator.java">Moved from Github project frtu/log-platform</a>
 * @since 1.1.3
 */
@Slf4j
public class ChaosGenerator {
    private List<String> memoryLeak = new ArrayList<>();

    /**
     * Randomly generate IllegalStateException
     *
     * @param errorMsg What the message should return
     * @return random number between 0 and 100
     */
    public String raiseException(String errorMsg) {
        Random rand = new Random();
        int n = rand.nextInt(100);
        if ((n % 2) == 0) {
            throw new IllegalStateException(errorMsg);
        }
        return Integer.toString(n);
    }

    /**
     * Introduce random delay for the current thread
     *
     * @param maxDelayInMilli Range of max millisecond it can wait
     */
    public void randomSleep(int maxDelayInMilli) {
        Random rand = new Random();
        int n = rand.nextInt(maxDelayInMilli);
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public String memoryleak() {
        StringBuilder longPayload = new StringBuilder();
        for (int i = 0; i < 100000000; i++) {
            longPayload.append('0');
        }
        String big = longPayload.toString();
        for (int j = 0; j < 1000000; j++) {
            for (int i = 0; i < 1000000; i++) {
                increaseMemory(big);
            }
            LOGGER.debug("Counter {}", j);
        }
        return "No issue";
    }

    protected void increaseMemory(String paylaod) {
        memoryLeak.add(paylaod);
    }
}
