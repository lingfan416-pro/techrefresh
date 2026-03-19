package org.lfan142.java8plus.features;

import java.util.function.Supplier;

public class SupplierTest {

    public static void main(String[] args) {
        Supplier<String> stest = () -> "hello";
        System.out.println(stest.get());
    }
}
