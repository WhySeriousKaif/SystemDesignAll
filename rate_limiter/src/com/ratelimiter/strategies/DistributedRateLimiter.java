package com.ratelimiter.strategies;

import com.ratelimiter.infrastructure.RedisClientMock;

/**
 * Distributed Rate Limiter:
 * - Uses a central Redis store to track requests.
 * - Simulates a multi-node environment where multiple services share the same rate limit.
 * - Provides visibility into "global" request count for a given resource.
 */
public class DistributedRateLimiter implements RateLimitStrategy {
    private final RedisClientMock redis;
    private final String key;
    private final int limit;
    private final long windowSizeInMillis;

    public DistributedRateLimiter(String key, int limit, long windowSizeInMillis) {
        this.redis = RedisClientMock.getInstance();
        this.key = key;
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public boolean canProceed() {
        // Atomic INCR in Redis
        int currentCount = redis.incr(key);

        // If it's the first request in the window, set expiry
        if (currentCount == 1) {
            redis.expire(key, windowSizeInMillis);
        }

        if (currentCount <= limit) {
            System.out.println("[Distributed] Request allowed. Current count: " + currentCount);
            return true;
        }

        System.out.println("[Distributed] 429 Too Many Requests. Current count: " + currentCount);
        return false;
    }
}
