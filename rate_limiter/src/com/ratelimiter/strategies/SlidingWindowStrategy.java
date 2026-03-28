package com.ratelimiter.strategies;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindowStrategy implements RateLimitStrategy {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final Queue<Long> requestTimestamps;

    public SlidingWindowStrategy(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.requestTimestamps = new ConcurrentLinkedQueue<>();
    }

    @Override
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        
        // Remove timestamps outside the sliding window
        while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peek() > windowSizeMillis) {
            requestTimestamps.poll();
        }

        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.add(currentTime);
            return true;
        }

        return false;
    }
}
