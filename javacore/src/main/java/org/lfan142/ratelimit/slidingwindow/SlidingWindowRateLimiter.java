package org.lfan142.ratelimit.slidingwindow;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class SlidingWindowRateLimiter {

    private final SlidingWindowConfig config;

    private final Queue<Long> historyRequstTime = new LinkedBlockingDeque<>();

    public SlidingWindowRateLimiter(SlidingWindowConfig config){
        if(config == null){
            throw new IllegalArgumentException("config is illegal, value is "+config);
        }
        this.config = config;
    }

    public synchronized boolean allowRequest(){
        long now = System.currentTimeMillis();
        Long preTimeStamp = historyRequstTime.peek();
        while(historyRequstTime.peek() != null && now - preTimeStamp > config.getSlidingTimeWindow()){
            historyRequstTime.remove();
            preTimeStamp = historyRequstTime.peek();
        }
        if(historyRequstTime.size() < config.getAllowRequestCnt()){
            historyRequstTime.add(now);
            return true;
        }
        return false;
    }



}
