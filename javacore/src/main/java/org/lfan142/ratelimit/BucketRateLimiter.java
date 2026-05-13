package org.lfan142.ratelimit;

public class BucketRateLimiter {

    private final RateLimitConfig config;
    private long preTimeStamp = System.nanoTime();
    private double curTokens;

    public BucketRateLimiter(RateLimitConfig config){
        this.config = config;
        this.curTokens = config.getCapacity();
    }

    public synchronized boolean acquireToken(){
        refillToken();
        if(curTokens < config.getTokensPerRequest()){
            return false;
        }
        curTokens -= config.getTokensPerRequest();
        return true;
    }

    private void refillToken() {
        long curTimeStamp = System.nanoTime();
        double tokenSize = (curTimeStamp - preTimeStamp) * config.getRefillTokensPerSecond()/1000000000.0;

        curTokens = Math.min(config.getCapacity(), curTokens + tokenSize);
        preTimeStamp = curTimeStamp;

    }

}
