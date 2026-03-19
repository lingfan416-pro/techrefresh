package org.lfan142.java8plus.features;

import java.util.Arrays;
import java.util.List;

public class StreamTest {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Tom", "Alice", "Bob", "Anna");
        List<String> result = names.stream().filter((name) -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
        System.out.println(result);
    }
}
