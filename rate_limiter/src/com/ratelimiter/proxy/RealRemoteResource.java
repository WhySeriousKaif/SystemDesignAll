package com.ratelimiter.proxy;

public class RealRemoteResource implements IRemoteResource {
    @Override
    public void callAPI() {
        System.out.println("[RealResource] Calling real external API...");
    }
}
