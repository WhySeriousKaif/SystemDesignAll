package com.ratelimiter;

import com.ratelimiter.models.ExternalService;
import com.ratelimiter.proxy.RateLimiterProxy;
import com.ratelimiter.strategies.SlidingWindowStrategy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🛡️ Starting Rate Limiter System Simulation");
        System.out.println("==========================================\n");

        // 1. Configure the Rate Limiter: 3 requests per 5 seconds
        SlidingWindowStrategy strategy = new SlidingWindowStrategy(3, 5);
        ExternalService proxy = new RateLimiterProxy(strategy);

        // 2. Simulate concurrent requests using an ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(5);

        System.out.println("🚀 [Simulation] Sending 10 simultaneous requests...\n");

        for (int i = 1; i <= 10; i++) {
            final int requestId = i;
            executor.submit(() -> {
                String response = proxy.callApi("Request #" + requestId);
                System.out.println("[Client] Result for Request " + requestId + ": " + response);
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n--- ⏳ Waiting for window to reset (6 seconds) ---");
        Thread.sleep(6000);

        System.out.println("\n🚀 [Simulation] Sending another request after window reset...");
        System.out.println("[Client] Result: " + proxy.callApi("Post-Window Request"));

        System.out.println("\n==========================================");
        System.out.println("🛡️ Simulation Completed.");
    }
}
