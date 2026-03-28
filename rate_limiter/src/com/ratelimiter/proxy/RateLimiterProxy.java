package com.ratelimiter.proxy;

import com.ratelimiter.strategies.RateLimitStrategy;

public class RateLimiterProxy implements ApiService {
    private final ApiService realApiService;
    private final RateLimitStrategy strategy;

    public RateLimiterProxy(ApiService realService, RateLimitStrategy strategy) {
        this.realApiService = realService;
        this.strategy = strategy;
    }

    @Override
    public String callApi(String endpoint) {
        if (strategy.allowRequest()) {
            return realApiService.callApi(endpoint);
        } else {
            System.err.println("[Proxy] 429 Too Many Requests (Rate limit reached)");
            return "Error 429: Too Many Requests";
        }
    }
}
