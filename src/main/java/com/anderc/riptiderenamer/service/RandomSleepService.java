package com.anderc.riptiderenamer.service;

import java.util.concurrent.ThreadLocalRandom;

public class RandomSleepService {
    private static final int MIN_SLEEP_SECONDS = 5;
    private static final int MAX_SLEEP_SECONDS = 10;


    public static void sleep() throws InterruptedException {
        int seconds = ThreadLocalRandom.current().nextInt(MIN_SLEEP_SECONDS, MAX_SLEEP_SECONDS + 1);
        long millis = seconds * 1000;

        Thread.sleep(millis);

    }

}
