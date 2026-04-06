package com.ratelimiter.strategies;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite Rate Limiter:
 * - Allows multiple strategies to be applied (e.g., 5/min AND 100/day).
 * - All strategies must allow the request for it to proceed.
 * - Best for: Tiered rate limiting.
 */
public class CompositeRateLimitStrategy implements RateLimitStrategy {
    private final List<RateLimitStrategy> strategies;

    public CompositeRateLimitStrategy() {
        this.strategies = new ArrayList<>();
    }

    public void addStrategy(RateLimitStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public boolean canProceed(String key) {
        for (RateLimitStrategy strategy : strategies) {
            if (!strategy.canProceed(key)) {
                return false;
            }
        }
        return true;
    }
}
