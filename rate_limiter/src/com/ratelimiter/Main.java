package com.ratelimiter;

import com.ratelimiter.proxy.*;
import com.ratelimiter.strategies.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Rate Limiter Pro Simulation 🚀");
        System.out.println("===============================\n");

        // 1. Fixed Window (Traditional)
        testStrategy("Fixed Window (3 req/5sec)", 
                     new FixedWindowCounter(3, 5000), 5);

        // 2. Sliding Window (Smoother)
        testStrategy("Sliding Window (3 req/5sec)", 
                     new SlidingWindowCounter(3, 5000), 5);

        // 3. Token Bucket (Handles Bursts + HIGH CONCURRENCY)
        testConcurrentStrategy("Token Bucket (Concurrency Test - 5 tokens, 1/sec refill)", 
                               new TokenBucketStrategy(5, 1), 10);

        // 4. Distributed Rate Limiter (Mock Redis)
        testStrategy("Distributed (Mock Redis - 2 req/5sec)", 
                     new DistributedRateLimiter("api_resource_1", 2, 5000), 4);

        System.out.println("\n✅ All simulations completed.");
    }

    private static void testStrategy(String name, RateLimitStrategy strategy, int iterations) throws InterruptedException {
        System.out.println("\n--- " + name + " ---");
        IRemoteResource real = new RealRemoteResource();
        IRemoteResource proxy = new RemoteResourceProxy(real, strategy);
        Service service = new Service(proxy);

        for (int i = 1; i <= iterations; i++) {
            System.out.print("Request " + i + ": ");
            service.process();
            Thread.sleep(100);
        }
    }

    private static void testConcurrentStrategy(String name, RateLimitStrategy strategy, int threadCount) throws InterruptedException {
        System.out.println("\n--- " + name + " ---");
        IRemoteResource real = new RealRemoteResource();
        IRemoteResource proxy = new RemoteResourceProxy(real, strategy);
        Service service = new Service(proxy);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(service::process);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
