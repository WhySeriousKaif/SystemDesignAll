package com.ratelimiter;

import com.ratelimiter.proxy.*;
import com.ratelimiter.strategies.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Rate Limiter Simulation (Final Refinement)");
        System.out.println("==================================================\n");

        // 1. Setup Simulation with FixedWindowCounter (5 req per 2 sec)
        System.out.println("--- Testing Fixed Window (5 req/2sec) via Service ---");
        RateLimitStrategy fixedCounter = new FixedWindowCounter(5, 2000);
        RemoteResourceProxy proxy = new RemoteResourceProxy(fixedCounter);
        Service service = new Service(proxy);

        for (int i = 1; i <= 8; i++) {
            System.out.print("Iteration " + i + ": ");
            service.someMethod(true); // Trigger remote call
            Thread.sleep(200);
        }

        System.out.println("\nWaiting for window reset...");
        Thread.sleep(2000);

        // 2. Setup Simulation with SlidingWindowCounter (3 req per 1 sec)
        System.out.println("\n--- Testing Sliding Window (3 req/1sec) via Service ---");
        RateLimitStrategy slidingCounter = new SlidingWindowCounter(3, 1000);
        RemoteResourceProxy slidingProxy = new RemoteResourceProxy(slidingCounter);
        Service slidingService = new Service(slidingProxy);

        for (int i = 1; i <= 5; i++) {
            System.out.print("Iteration " + i + ": ");
            slidingService.someMethod(true);
            Thread.sleep(200);
        }

        System.out.println("\nSimulation completed.");
    }
}
