package org.lfan142.java8plus.features;

import java.util.function.Function;

public class RunnableTest {

    public static void main(String[] args) {
      Runnable runnable = () -> System.out.println("Hello");



      PlusFunction plusFunction = Integer::sum;

      Thread td = new Thread(runnable);
      td.start();
        String abc = "abc";
        Function<Integer, Character> f = idx -> (char)(idx + 'a');
        System.out.println(f.apply(10));
    }
}
