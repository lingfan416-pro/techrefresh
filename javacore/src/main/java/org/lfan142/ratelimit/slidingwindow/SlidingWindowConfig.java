package org.lfan142.ratelimit.slidingwindow;

public class SlidingWindowConfig {

    private final long slidingTimeWindow;
    private final int allowRequestCnt;

    public SlidingWindowConfig(long slidingTimeWindow, int allowRequestCnt){

        this.slidingTimeWindow = slidingTimeWindow;
        this.allowRequestCnt = allowRequestCnt;
    }

    public int getAllowRequestCnt() {
        return allowRequestCnt;
    }

    public long getSlidingTimeWindow() {
        return slidingTimeWindow;
    }
}
