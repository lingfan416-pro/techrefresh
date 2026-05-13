package org.lfan142.core;

import org.lfan142.java8plus.features.RunnableTest;

public class Rectangle implements Drawable{
    private String name;

    private int width;
    private int length;

    @Override
    public void draw(String name, int... shapes) {
        if(shapes.length <2){
            throw new RuntimeException(" the input is incorrect");
        }
        this.name = name;
        this.width = shapes[0];
        this.length = shapes[1];
        System.out.println("area is "+ (width * length));
    }
}
