package org.lfan142.javabasic.reflection;

public class User {

    private String name = "Bob"; //has to be public

    public User(){
        System.out.println("User Created");
    }


    public void sayHello(String name){
        System.out.println("Hello " + name);
    }
}
