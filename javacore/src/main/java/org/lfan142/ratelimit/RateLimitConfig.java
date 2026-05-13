package org.lfan142.ratelimit;

public class RateLimitConfig {

    private final double capacity;
    private final long tokensPerRequest;
    private final double refillTokensPerSecond;
    private final String policy;

    public RateLimitConfig(double capacity, long tokensPerRequest, double refillTokensPerSecond, String policy){

        if(capacity <= 0){
            throw new IllegalArgumentException("capacity should be positive value");
        }
        if(tokensPerRequest <= 0){
            throw new IllegalArgumentException("tokensPerRequest should be positive value");
        }
        if(refillTokensPerSecond <= 0){
            throw new IllegalArgumentException("refillTokensPerSecond should be positive value");
        }
        if(policy == null || policy.isEmpty()){
            throw new IllegalArgumentException("policy is invalid");
        }
        this.capacity = capacity;
        this.tokensPerRequest = tokensPerRequest;
        this.refillTokensPerSecond = refillTokensPerSecond;
        this.policy = policy;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getRefillTokensPerSecond() {
        return refillTokensPerSecond;
    }

    public long getTokensPerRequest() {
        return tokensPerRequest;
    }

    public String getPolicy() {
        return policy;
    }
}
