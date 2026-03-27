package com.ratelimiter.proxy;

import com.ratelimiter.models.ExternalService;
import com.ratelimiter.models.RealExternalService;
import com.ratelimiter.strategies.RateLimitingStrategy;

public class RateLimiterProxy implements ExternalService {
    private volatile RealExternalService realService;
    private final RateLimitingStrategy strategy;

    public RateLimiterProxy(RateLimitingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String callApi(String request) {
        // 1. Rate Limiting Check (Strategy Pattern)
        if (strategy.isAllowed()) {
            // 2. Lazy Initialization (Double-Checked Locking)
            if (realService == null) {
                synchronized (this) {
                    if (realService == null) {
                        realService = new RealExternalService();
                    }
                }
            }
            // 3. Delegation
            return realService.callApi(request);
        } else {
            // 4. Return 429 Too Many Requests
            return "429 Too Many Requests - Limit Exceeded for '" + request + "'";
        }
    }
}
