package com.ratelimiter.proxy;

import com.ratelimiter.strategies.RateLimitStrategy;
import com.ratelimiter.models.RateLimitExceededException;

public class RemoteResourceProxy {
    private final RateLimitStrategy rateLimitStrategy;

    public RemoteResourceProxy(RateLimitStrategy rateLimitStrategy) {
        this.rateLimitStrategy = rateLimitStrategy;
    }

    /*
     * Implementation follows the instructor's 'generate' method.
     */
    public void generate() {
        if (rateLimitStrategy.canProceed()) {
            // Make the actual API call
            actualRemoteApiCall();
        } else {
            // Throw exception as per instructor points
            throw new RateLimitExceededException("Rate limit reached for RemoteResourceProxy");
        }
    }

    private void actualRemoteApiCall() {
        System.out.println("[RemoteResourceProxy] Making the actual remote API call...");
    }
}
