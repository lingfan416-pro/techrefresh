package org.lfan142.circuitbreaker;

public class CircuitBreaker {

    private final CircuitBreakerConfig config;
    private int currentFailedTimes;
    private long preOpenTimestamp;
    private CircuitBreakerStatus status = CircuitBreakerStatus.CLOSED;

    public CircuitBreaker(CircuitBreakerConfig config){
        if(config == null){
            throw new IllegalArgumentException("config is null");
        }
        this.config = config;
    }

    public synchronized boolean allowRequest(){
        if(status == CircuitBreakerStatus.CLOSED){
            return true;
        }
        if(status == CircuitBreakerStatus.OPEN){
            long now = System.currentTimeMillis();
            if(now - preOpenTimestamp >= config.getOpenTimeoutThreshold()){
                status = CircuitBreakerStatus.HALF_OPEN;
                return true;
            }
            return false;
        }
        return false;
    }

    public synchronized void recordSuccess(){
        if(status != CircuitBreakerStatus.CLOSED){
            status = CircuitBreakerStatus.CLOSED;
        }
        currentFailedTimes  = 0;
    }

    public synchronized void recordFailed(){

        if(status == CircuitBreakerStatus.HALF_OPEN ){
            openCircuitBreaker();
        }
        if(status == CircuitBreakerStatus.CLOSED && currentFailedTimes >= config.getLimitFailedThreshold()){
            currentFailedTimes ++;
            openCircuitBreaker();
        }
    }

    private void openCircuitBreaker(){
        status = CircuitBreakerStatus.OPEN;
        preOpenTimestamp = System.currentTimeMillis();
    }

}
