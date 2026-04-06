package com.ratelimiter.proxy;

import com.ratelimiter.strategies.RateLimitStrategy;

public class RemoteResourceProxy implements IRemoteResource {

    private final IRemoteResource realResource;
    private final RateLimitStrategy strategy;

    public RemoteResourceProxy(IRemoteResource realResource,
                               RateLimitStrategy strategy) {
        this.realResource = realResource;
        this.strategy = strategy;
    }

    @Override
    public void callAPI(String key) {
        if (strategy.canProceed(key)) {
            realResource.callAPI(key);
        } else {
            System.err.println("429 Too Many Requests for key: " + key + " - Rejected by Proxy");
        }
    }
}
