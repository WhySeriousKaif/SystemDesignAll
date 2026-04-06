package com.ratelimiter.strategies;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Token Bucket Algorithm:
 * - Tokens are added to a bucket at a fixed rate.
 * - The bucket has a maximum capacity.
 * - Each request consumes one token.
 * - If the bucket is empty, the request is rejected.
 * - Best for: Allowing bursts of traffic up to the capacity.
 */
public class TokenBucketStrategy implements RateLimitStrategy {
    private final long capacity;
    private final double refillRatePerMs;
    private double currentTokens;
    private long lastRefillTime;
    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucketStrategy(long capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = (double) refillRatePerSecond / 1000.0;
        this.currentTokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public boolean canProceed() {
        lock.lock();
        try {
            refill();
            if (currentTokens >= 1.0) {
                currentTokens -= 1.0;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long timeElapsed = now - lastRefillTime;
        double tokensToAdd = timeElapsed * refillRatePerMs;
        
        currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
        lastRefillTime = now;
    }
}
