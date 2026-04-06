package com.ratelimiter.proxy;

public class Service {

    private final IRemoteResource resource;

    public Service(IRemoteResource resource) {
        this.resource = resource;
    }

    public void process() {
        System.out.println("Processing business logic inside Service...");
        resource.callAPI();
    }
}
