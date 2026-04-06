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
    public boolean canProceed(String key) {
        // Concatenate resourceKey with the caller key
        String combinedKey = this.key + ":" + key;

        // Atomic INCR in Redis
        int currentCount = redis.incr(combinedKey);

        // If it's the first request in the window, set expiry
        if (currentCount == 1) {
            redis.expire(combinedKey, windowSizeInMillis);
        }

        if (currentCount <= limit) {
            System.out.println("[Distributed] Request allowed for " + combinedKey + ". Current count: " + currentCount);
            return true;
        }

        System.out.println("[Distributed] 429 Too Many Requests for " + combinedKey + ". Current count: " + currentCount);
        return false;
    }
}
