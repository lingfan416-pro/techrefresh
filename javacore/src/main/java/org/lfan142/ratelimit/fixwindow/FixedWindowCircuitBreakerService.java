package org.lfan142.ratelimit.fixwindow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowCircuitBreakerService {

    private final FixWindowRateLimiterConfig config;
    private final Map<String, FixWindowCircuitBreaker> fixedWindowBreakers = new ConcurrentHashMap<>();

    public FixedWindowCircuitBreakerService(FixWindowRateLimiterConfig config){
        if(config == null){
            throw new IllegalArgumentException("config is null, please provide valid object");
        }
        this.config = config;
    }

    public boolean allowRequest(String key){
        if(key == null || key.isBlank()){
            throw new IllegalArgumentException("invalid parameter key :" + key);
        }
        FixWindowCircuitBreaker circuitBreaker = fixedWindowBreakers.computeIfAbsent(key, k->new FixWindowCircuitBreaker(config));

        return circuitBreaker.allowRequest();
    }
}
