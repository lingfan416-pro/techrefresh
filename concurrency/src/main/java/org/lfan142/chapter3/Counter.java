package org.lfan142.chapter3;

public class Counter {

    private long value = 0;

    public synchronized long getValue(){
        return value;
    }

    public synchronized long increment(){
        if(value == Long.MAX_VALUE){
            throw new IllegalStateException("counter overflow");
        }

        return ++value;
    }
}
