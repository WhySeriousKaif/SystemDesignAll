package com.ratelimiter.strategies;

public class FixedWindowCounter implements RateLimitStrategy {
    private int counter;
    private long windowStartTime;
    private final int limit;
    private final long windowSizeInMillis;

    public FixedWindowCounter(int limit, long windowSizeInMillis) {
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
        this.windowStartTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean canProceed() {
        long currentTime = System.currentTimeMillis();
        // Check if window has expired
        if (currentTime - windowStartTime >= windowSizeInMillis) {
            counter = 0;
            windowStartTime = currentTime;
            System.out.println("[Strategy] Fixed window reset.");
        }

        if (counter < limit) {
            counter++;
            return true;
        }
        return false;
    }
}
