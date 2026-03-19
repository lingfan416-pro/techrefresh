package org.lfan142.java8plus.features;

public class VirtualThreadTest {
    public static void main(String[] args) {
        try(var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()){
            executor.submit(() -> System.out.println("running in virtual thread"));
        }
    }
}
