package com.ratelimiter.strategies;

public interface RateLimitStrategy {
    /**
     * Checks if the request is allowed based on the implemented algorithm.
     * @return true if allowed, false if limit reached.
     */
    boolean canProceed(String key);
}
