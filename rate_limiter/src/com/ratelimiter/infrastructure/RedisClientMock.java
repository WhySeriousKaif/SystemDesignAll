package com.ratelimiter.infrastructure;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock Redis Client to simulate distributed rate limiting.
 * - Centralized storage using a Map.
 * - Simulates atomic INC and EXPIRE operations.
 */
public class RedisClientMock {
    private static RedisClientMock instance;
    private final ConcurrentHashMap<String, AtomicInteger> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> expiryStore = new ConcurrentHashMap<>();

    private RedisClientMock() {}

    public static synchronized RedisClientMock getInstance() {
        if (instance == null) {
            instance = new RedisClientMock();
        }
        return instance;
    }

    /**
     * Simulates Redis INCR command.
     */
    public int incr(String key) {
        cleanIfExpired(key);
        return store.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Simulates Redis EXPIRE command.
     */
    public void expire(String key, long windowSizeInMillis) {
        expiryStore.putIfAbsent(key, System.currentTimeMillis() + windowSizeInMillis);
    }

    private void cleanIfExpired(String key) {
        Long expiry = expiryStore.get(key);
        if (expiry != null && System.currentTimeMillis() > expiry) {
            store.remove(key);
            expiryStore.remove(key);
        }
    }

    public void clear() {
        store.clear();
        expiryStore.clear();
    }
}
