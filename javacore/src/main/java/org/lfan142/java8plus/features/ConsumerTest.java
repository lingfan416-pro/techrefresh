package org.lfan142.java8plus.features;

import java.util.function.Consumer;

public class ConsumerTest {
    public static void main(String[] args) {
        Consumer<String> con = (ABC) -> System.out.println(ABC);
        con.accept("wdx");
    }
}
