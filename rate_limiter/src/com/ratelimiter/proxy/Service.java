package com.ratelimiter.proxy;

public class Service {

    private final IRemoteResource resource;

    public Service(IRemoteResource resource) {
        this.resource = resource;
    }

    public void process(String key) {
        System.out.println("Processing business logic inside Service for client request...");
        
        // Simulating business logic that decides if an external call is needed
        boolean externalCallNeeded = true; 
        
        if (externalCallNeeded) {
            System.out.println("External resource call required. Delegating to Proxy...");
            resource.callAPI(key);
        } else {
            System.out.println("No external call needed. Request complete.");
        }
    }
}
