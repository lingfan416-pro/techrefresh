package org.lfan142.ratelimit.fixwindow;

public class FixWindowCircuitBreaker {

    private final FixWindowRateLimiterConfig config;

    private long preTimeStamp;
    private double requestCnt;

    public FixWindowCircuitBreaker(FixWindowRateLimiterConfig config){
        this.config = config;
    }

    public synchronized boolean allowRequest(){
        long now = System.currentTimeMillis();
        if(now - preTimeStamp >= config.getFixWindowTimeThreshold()){
            preTimeStamp = now;
            requestCnt = 1;
            return true;
        }
        requestCnt ++;
        if(requestCnt > config.getRequestThreshold()){
            return false;
        }
        return true;
    }

}
