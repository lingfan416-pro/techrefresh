package org.lfan142.circuitbreaker;

public enum CircuitBreakerStatus {
    OPEN, //success
    CLOSED, //request failed
    HALF_OPEN //test
}
