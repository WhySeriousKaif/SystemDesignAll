package com.ratelimiter.proxy;

public class RealRemoteResource implements RemoteResource {
    @Override
    public String callApi(String input) {
        System.out.println("[RealResource] Calling remote API with input: " + input);
        return "Success: Processed " + input;
    }
}
