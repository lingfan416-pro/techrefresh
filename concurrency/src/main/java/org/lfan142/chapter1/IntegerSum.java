package org.lfan142.chapter1;

public class IntegerSum {
    private int value = 0;

    public int add(int factor){
        value += factor;
        return value;
    }

    public int min(int factor){
        value -= factor;
        return value;
    }

    public int getValue(){
        return value;
    }
}

