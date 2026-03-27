package com.ratelimiter.strategies;

public interface RateLimitingStrategy {
    boolean isAllowed();
}
