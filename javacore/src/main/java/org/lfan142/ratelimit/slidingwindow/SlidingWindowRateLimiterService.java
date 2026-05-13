package org.lfan142.ratelimit.slidingwindow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowRateLimiterService {

    private final SlidingWindowConfig config;

    private final Map<String, SlidingWindowRateLimiter> slidingWindowRateLimiters = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiterService(SlidingWindowConfig config){
        if(config == null){
            throw new IllegalArgumentException("invalid config");
        }
        this.config = config;
    }

    public boolean allowRequest(String key){
        if(key == null || key.isBlank()){
            throw new IllegalArgumentException("Ileegal input key");
        }
        SlidingWindowRateLimiter rateLimiter =
                slidingWindowRateLimiters.computeIfAbsent(key, k -> new SlidingWindowRateLimiter(config));

        return rateLimiter.allowRequest();
    }
}
