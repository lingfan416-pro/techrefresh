package org.lfan142.javabasic.reflection;

import java.lang.reflect.InvocationTargetException;

public class ReflectionTest {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class clazz = Class.forName("org.lfan142.javabasic.reflection.User");
        Object obj = clazz.getDeclaredConstructor().newInstance();

        java.lang.reflect.Field field = clazz.getDeclaredField("name");
        field.setAccessible(true);
        System.out.println(field.getName() + " : "+ field.get(obj));
    }
}
