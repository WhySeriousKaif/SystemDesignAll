package com.ratelimiter.proxy;

import com.ratelimiter.models.RateLimitExceededException;

public class Service {
    private final RemoteResourceProxy remoteResourceProxy;

    public Service(RemoteResourceProxy remoteResourceProxy) {
        this.remoteResourceProxy = remoteResourceProxy;
    }

    public void someMethod(boolean someCondition) {
        System.out.println("[Service] Executing someMethod logic...");
        // Some code...
        if (someCondition) {
            System.out.println("[Service] Condition met. Calling remote proxy...");
            try {
                remoteResourceProxy.generate();
            } catch (RateLimitExceededException e) {
                System.err.println("[Service] Rate limit exceeded: " + e.getMessage());
            }
        }
        // More code...
    }
}
