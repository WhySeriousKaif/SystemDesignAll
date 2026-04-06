package com.ratelimiter.strategies;

import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowCounter implements RateLimitStrategy {
    private final int limit;
    private final long windowSizeInMillis;
    private final ConcurrentHashMap<String, WindowData> keyData = new ConcurrentHashMap<>();

    private static class WindowData {
        int counter;
        long windowStartTime;

        WindowData(long startTime) {
            this.windowStartTime = startTime;
            this.counter = 0;
        }
    }

    public FixedWindowCounter(int limit, long windowSizeInMillis) {
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public boolean canProceed(String key) {
        long currentTime = System.currentTimeMillis();
        
        WindowData data = keyData.compute(key, (k, v) -> {
            if (v == null || currentTime - v.windowStartTime >= windowSizeInMillis) {
                return new WindowData(currentTime);
            }
            return v;
        });

        synchronized (data) {
            if (data.counter < limit) {
                data.counter++;
                return true;
            }
        }
        return false;
    }
}
