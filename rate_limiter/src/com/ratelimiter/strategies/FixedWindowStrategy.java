package com.ratelimiter.strategies;

public class FixedWindowStrategy implements RateLimitStrategy {
    private final int maxRequests;
    private final long windowSizeMillis;
    private int requestCount;
    private long windowStartTime;

    public FixedWindowStrategy(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.requestCount = 0;
        this.windowStartTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        
        // Check if window has expired
        if (currentTime - windowStartTime >= windowSizeMillis) {
            windowStartTime = currentTime;
            requestCount = 0;
            System.out.println("[Strategy] Fixed window reset.");
        }

        if (requestCount < maxRequests) {
            requestCount++;
            return true;
        }
        
        return false;
    }
}
