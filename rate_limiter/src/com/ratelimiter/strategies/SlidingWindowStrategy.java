package com.ratelimiter.strategies;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.Deque;

public class SlidingWindowStrategy implements RateLimitingStrategy {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final Deque<Long> requestTimestamps;

    public SlidingWindowStrategy(int maxRequests, int windowSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSeconds * 1000L;
        this.requestTimestamps = new ConcurrentLinkedDeque<>();
    }

    @Override
    public synchronized boolean isAllowed() {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowSizeMillis;

        // Remove timestamps outside the current window
        while (!requestTimestamps.isEmpty() && requestTimestamps.peekFirst() < windowStart) {
            requestTimestamps.pollFirst();
        }

        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.addLast(currentTime);
            return true;
        }

        return false;
    }
}
