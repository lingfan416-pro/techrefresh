package org.lfan142.ratelimit.slidingwindow;



public class SlidingWindowRateLimiter {

    private final SlidingWindowConfig config;

    private final long[] historyRequstTime;

    private int head = 0;
    private int size = 0;

    public SlidingWindowRateLimiter(SlidingWindowConfig config){
        if(config == null){
            throw new IllegalArgumentException("config is illegal, value is "+config);
        }
        this.config = config;
        historyRequstTime = new long[config.getAllowRequestCnt()];
    }

    public synchronized boolean allowRequest(){
        long now = System.currentTimeMillis();
        while(size > 0 && now - historyRequstTime[head] >= config.getSlidingTimeWindow()){
            head ++;
            head = head % config.getAllowRequestCnt();
            size --;
        }
        if(size >= config.getAllowRequestCnt()){
            return false;
        }
        int tail = (head + size)%config.getAllowRequestCnt();
        historyRequstTime[tail] = now;
        size ++;
        return true;
    }



}
