package org.lfan142.javabasic.markableinterface;

public class Person implements Cloneable{

    String name;

    Person(String name){
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
