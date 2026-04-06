package com.ratelimiter.proxy;

public class RealRemoteResource implements IRemoteResource {
    @Override
    public void callAPI(String key) {
        System.out.println("[RealResource] Calling real external API for key: " + key);
    }
}
