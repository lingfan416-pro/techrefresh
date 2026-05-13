package org.lfan142.core;

import java.lang.reflect.InvocationTargetException;

public class Student {

    private String name;
    private String number;

    public Student(String name, String number){
        this.name = name;
        this.number = number;
    }


    public static void main(String[] args) {
        try {
            Class<?> c = Class.forName("org.lfan142.core.Student");
            Student student = (Student) c.getDeclaredConstructor(String.class, String.class).newInstance("Ling Fan", "1330440650");
            System.out.println(student.name + " "+ student.number);

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
