package com.ratelimiter.models;

public class RealExternalService implements ExternalService {
    
    public RealExternalService() {
        // Simulating an expensive initialization
        System.out.println("🚀 [Service] Initializing RealExternalService (Expensive Resource)...");
    }

    @Override
    public String callApi(String request) {
        return "200 OK - Data for: " + request;
    }
}
