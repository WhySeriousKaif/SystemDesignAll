package com.ratelimiter.strategies;


/**
 * Token Bucket Algorithm:
 * - Tokens are added to a bucket at a fixed rate.
 * - The bucket has a maximum capacity.
 * - Each request consumes one token.
 * - If the bucket is empty, the request is rejected.
 * - Best for: Allowing bursts of traffic up to the capacity.
 */
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketStrategy implements RateLimitStrategy {
    private final long capacity;
    private final double refillRatePerMs;
    private final ConcurrentHashMap<String, BucketData> keyBuckets = new ConcurrentHashMap<>();

    private static class BucketData {
        double currentTokens;
        long lastRefillTime;

        BucketData(double capacity) {
            this.currentTokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    public TokenBucketStrategy(long capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = (double) refillRatePerSecond / 1000.0;
    }

    @Override
    public boolean canProceed(String key) {
        BucketData data = keyBuckets.computeIfAbsent(key, k -> new BucketData(capacity));

        synchronized (data) {
            refill(data);
            if (data.currentTokens >= 1.0) {
                data.currentTokens -= 1.0;
                return true;
            }
            return false;
        }
    }

    private void refill(BucketData data) {
        long now = System.currentTimeMillis();
        long timeElapsed = now - data.lastRefillTime;
        double tokensToAdd = timeElapsed * refillRatePerMs;
        
        data.currentTokens = Math.min(capacity, data.currentTokens + tokensToAdd);
        data.lastRefillTime = now;
    }
}
