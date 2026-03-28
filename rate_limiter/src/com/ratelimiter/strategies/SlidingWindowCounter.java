package com.ratelimiter.strategies;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowCounter implements RateLimitStrategy {
    private final Queue<Long> requestTimestamps = new LinkedList<>();
    private final int limit;
    private final long windowSizeInMillis;

    public SlidingWindowCounter(int limit, long windowSizeInMillis) {
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public synchronized boolean canProceed() {
        long currentTime = System.currentTimeMillis();
        // Remove timestamps outside the sliding window
        while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peek() >= windowSizeInMillis) {
            requestTimestamps.poll();
        }

        if (requestTimestamps.size() < limit) {
            requestTimestamps.offer(currentTime);
            return true;
        }
        return false;
    }
}
