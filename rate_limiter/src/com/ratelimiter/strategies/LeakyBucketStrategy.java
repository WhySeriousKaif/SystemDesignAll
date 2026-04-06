package com.ratelimiter.strategies;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Leaky Bucket Algorithm:
 * - Requests enter a bucket.
 * - The bucket "leaks" at a constant rate.
 * - If the bucket overflows, requests are rejected.
 * - Best for: Smoothing out traffic and ensuring a constant output rate.
 */
public class LeakyBucketStrategy implements RateLimitStrategy {
    private final int capacity;
    private final double leakRatePerMs;
    private final ConcurrentHashMap<String, BucketData> keyBuckets = new ConcurrentHashMap<>();

    private static class BucketData {
        double currentWater;
        long lastLeakTime;

        BucketData() {
            this.currentWater = 0;
            this.lastLeakTime = System.currentTimeMillis();
        }
    }

    public LeakyBucketStrategy(int capacity, int leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRatePerMs = (double) leakRatePerSecond / 1000.0;
    }

    @Override
    public boolean canProceed(String key) {
        BucketData data = keyBuckets.computeIfAbsent(key, k -> new BucketData());

        synchronized (data) {
            leak(data);
            if (data.currentWater < capacity) {
                data.currentWater += 1.0;
                return true;
            }
            return false;
        }
    }

    private void leak(BucketData data) {
        long now = System.currentTimeMillis();
        long timeElapsed = now - data.lastLeakTime;
        double leakedAmount = timeElapsed * leakRatePerMs;
        
        data.currentWater = Math.max(0, data.currentWater - leakedAmount);
        data.lastLeakTime = now;
    }
}
