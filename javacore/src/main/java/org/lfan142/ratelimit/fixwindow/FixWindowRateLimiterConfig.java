package org.lfan142.ratelimit.fixwindow;

public class FixWindowRateLimiterConfig {

    private final long fixWindowTimeThreshold;
    private final int requestThreshold;
    private final String policyId;

    public FixWindowRateLimiterConfig(long fixWindowTimeThreshold, int requestThreshold, String policyId){
        if(fixWindowTimeThreshold <= 0){
            throw new IllegalArgumentException("invalid fixWindowTimeThreshold value "+ fixWindowTimeThreshold);
        }
        if(requestThreshold <= 0){
            throw new IllegalArgumentException(" illega ");
        }
        this.fixWindowTimeThreshold = fixWindowTimeThreshold;
        this.requestThreshold = requestThreshold;
        this.policyId = policyId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public int getRequestThreshold() {
        return requestThreshold;
    }

    public long getFixWindowTimeThreshold() {
        return fixWindowTimeThreshold;
    }
}
