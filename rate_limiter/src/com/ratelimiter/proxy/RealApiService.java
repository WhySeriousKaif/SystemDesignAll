package com.ratelimiter.proxy;

public class RealApiService implements ApiService {
    @Override
    public String callApi(String endpoint) {
        // Simulating an actual expensive API call
        System.out.println("[RemoteResource] Calling external API: " + endpoint);
        return "Success: Data from " + endpoint;
    }
}
