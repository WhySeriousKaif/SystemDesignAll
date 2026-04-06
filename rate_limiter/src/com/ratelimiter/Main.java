package com.ratelimiter;

import com.ratelimiter.proxy.*;
import com.ratelimiter.strategies.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Rate Limiter Pro Simulation 🚀");
        System.out.println("===============================\n");

        // 1. User T1 - Example Use Case (5 calls per minute)
        testStrategy("User T1: Example Use Case (5 req/min)", "UserT1",
                     new FixedWindowCounter(5, 60000), 7);

        // 2. Token Bucket (Handles Bursts + HIGH CONCURRENCY)
        testConcurrentStrategy("Token Bucket Concurrency (5 tokens, 1/sec refill)", "Tenant_A",
                               new TokenBucketStrategy(5, 1), 10);

        // 3. Leaky Bucket (Smooths Output Rate)
        testStrategy("Leaky Bucket (Capacity 3, Leak 1/sec)", "UserB",
                     new LeakyBucketStrategy(3, 1), 6);

        // 4. Sliding Log (High Precision)
        testStrategy("Sliding Log (2 req/5sec)", "UserC",
                     new SlidingLogStrategy(2, 5000), 4);

        // 5. Composite Strategy (Nested Limits: 2/5sec AND 5/min)
        CompositeRateLimitStrategy composite = new CompositeRateLimitStrategy();
        composite.addStrategy(new FixedWindowCounter(2, 5000));
        composite.addStrategy(new FixedWindowCounter(5, 60000));
        testStrategy("Composite (2 req/5sec AND 5 req/min)", "UserT1",
                     composite, 7);

        System.out.println("\n✅ All simulations completed.");
    }

    private static void testStrategy(String name, String key, RateLimitStrategy strategy, int iterations) throws InterruptedException {
        System.out.println("\n--- " + name + " ---");
        IRemoteResource real = new RealRemoteResource();
        IRemoteResource proxy = new RemoteResourceProxy(real, strategy);
        Service service = new Service(proxy);

        for (int i = 1; i <= iterations; i++) {
            System.out.print("Request " + i + " for " + key + ": ");
            service.process(key);
            Thread.sleep(100);
        }
    }

    private static void testConcurrentStrategy(String name, String key, RateLimitStrategy strategy, int threadCount) throws InterruptedException {
        System.out.println("\n--- " + name + " ---");
        IRemoteResource real = new RealRemoteResource();
        IRemoteResource proxy = new RemoteResourceProxy(real, strategy);
        Service service = new Service(proxy);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> service.process(key));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
