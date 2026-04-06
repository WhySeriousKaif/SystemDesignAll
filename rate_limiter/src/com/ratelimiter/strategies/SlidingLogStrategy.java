package com.ratelimiter.strategies;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Log Algorithm:
 * - Every request's timestamp is stored in a sorted log.
 * - Prune old timestamps beyond the window for each request.
 * - Precision: High (no boundary issues).
 * - Best for: High accuracy requirements despite memory overhead.
 */
public class SlidingLogStrategy implements RateLimitStrategy {
    private final int limit;
    private final long windowSizeInMillis;
    private final ConcurrentHashMap<String, TreeSet<Long>> keyLogs = new ConcurrentHashMap<>();

    public SlidingLogStrategy(int limit, long windowSizeInMillis) {
        this.limit = limit;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public boolean canProceed(String key) {
        long now = System.currentTimeMillis();
        TreeSet<Long> log = keyLogs.computeIfAbsent(key, k -> new TreeSet<>());

        synchronized (log) {
            // Prune old entries
            long boundary = now - windowSizeInMillis;
            log.headSet(boundary).clear();

            if (log.size() < limit) {
                // Add new entry, ensuring it's unique by adding a tiny epsilon if needed
                // In practice, nanoTime or a counter suffix is used for uniqueness
                long uniqueTimestamp = now;
                while (log.contains(uniqueTimestamp)) {
                    uniqueTimestamp++;
                }
                log.add(uniqueTimestamp);
                return true;
            }
        }
        return false;
    }
}
