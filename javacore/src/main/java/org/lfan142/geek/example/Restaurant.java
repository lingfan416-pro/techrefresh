package org.lfan142.geek.example;

public class Restaurant {

    public static void main(String[] args) {
        Thread t1 = new CookingTask("Pasta");
        Thread t2 = new CookingTask("Rice");
        Thread t3 = new CookingTask("Dessert");
        Thread t4 = new CookingTask("Salad");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

    }
}
