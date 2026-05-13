package org.lfan142.ratelimit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitService {

    private final Map<String,RateLimitConfig> policyConfig = new HashMap<>();
    private final Map<String, BucketRateLimiter> bucketRateLimiters = new ConcurrentHashMap<>();

    public RateLimitService(List<RateLimitConfig> configs){
        if(configs == null){
            throw new IllegalArgumentException("Invalid rate limit configuration");
        }
        configs.forEach(config ->{
            policyConfig.putIfAbsent(config.getPolicy(), config);
        });
    }

    public boolean allowRequest(String key, String policyId){
        if(key == null || key.isBlank()){
            throw new IllegalArgumentException("the rate limit key you input is invalid");
        }
        if(policyId == null || policyId.isBlank()){
            throw new IllegalArgumentException("the rate policyId you input is invalid");
        }

        String bucketKey = key + ":" + policyId;
        RateLimitConfig config = policyConfig.get(policyId);
        if(config == null){
            throw new IllegalArgumentException("the policy is not defined "+ policyId);
        }
        BucketRateLimiter bucketLimiter = bucketRateLimiters
                .computeIfAbsent(bucketKey, k -> new BucketRateLimiter(config));
        return bucketLimiter.acquireToken();
    }

}
