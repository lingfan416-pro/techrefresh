package org.lfan142.javabasic.reflection;

public class AnnotationTest {


    public static void main(String[] args) {
        Class<?> clazz = AnnoTestClass.class;
        MyAnno anno = clazz.getAnnotation(MyAnno.class);
        System.out.println(anno.value());
    }
}
