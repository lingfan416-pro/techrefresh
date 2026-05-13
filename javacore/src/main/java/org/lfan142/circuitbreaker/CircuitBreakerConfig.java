package org.lfan142.circuitbreaker;

public class CircuitBreakerConfig {

    private final int limitFailedThreshold;
    private final String serviceName;
    private final long openTimeoutThreshold;

    public CircuitBreakerConfig(int limitFailedThreshold, String serviceName, long openTimeoutThreshold){
        this.limitFailedThreshold = limitFailedThreshold;
        this.serviceName = serviceName;
        this.openTimeoutThreshold = openTimeoutThreshold;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getLimitFailedThreshold() {
        return limitFailedThreshold;
    }

    public long getOpenTimeoutThreshold() {
        return openTimeoutThreshold;
    }
}
