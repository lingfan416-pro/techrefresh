package org.lfan142.javabasic.markableinterface;

import java.io.Serializable;

public class User implements Serializable {

    private static final Long serializableUID = 5L;

    private String name;
    private transient String password;
    private int age;

    public User(String name, String password, int age){
        this.name = name;
        this.password = password;
        this.age = age;
    }
}
