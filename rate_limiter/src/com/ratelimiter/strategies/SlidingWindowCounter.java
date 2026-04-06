package com.ratelimiter.strategies;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowCounter implements RateLimitStrategy {
    private final int limit;
    private final long windowSizeInMillis;
    private final ConcurrentHashMap<String, Queue<Long>> keyTimestamps = new ConcurrentHashMap<>();

    public SlidingWindowCounter(int limit, long windowSizeInMillis) {
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public boolean canProceed(String key) {
        long currentTime = System.currentTimeMillis();
        Queue<Long> timestamps = keyTimestamps.computeIfAbsent(key, k -> new LinkedList<>());

        synchronized (timestamps) {
            // Remove timestamps outside the sliding window
            while (!timestamps.isEmpty() && currentTime - timestamps.peek() >= windowSizeInMillis) {
                timestamps.poll();
            }

            if (timestamps.size() < limit) {
                timestamps.offer(currentTime);
                return true;
            }
        }
        return false;
    }
}
