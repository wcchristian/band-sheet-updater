package com.anderc.riptiderenamer.service;

import java.util.concurrent.ThreadLocalRandom;

public class RandomSleepService {
    private static final int MIN_SLEEP_SECONDS = 5;
    private static final int MAX_SLEEP_SECONDS = 10;
    private static final int SEC_TO_MILLIS_MULTIPLIER = 1000;


    public static void sleep() throws InterruptedException {
        int seconds = ThreadLocalRandom.current().nextInt(MIN_SLEEP_SECONDS, MAX_SLEEP_SECONDS + 1);
        long millis = seconds * SEC_TO_MILLIS_MULTIPLIER;

        Thread.sleep(millis);
    }

}
