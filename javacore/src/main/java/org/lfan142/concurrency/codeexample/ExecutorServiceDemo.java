package org.lfan142.concurrency.codeexample;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> System.out.println(Thread.currentThread().getName() + " start"));
        executorService.submit(() -> System.out.println(Thread.currentThread().getName() + " start"));

        Future<Integer> future = executorService.submit(() -> {
            Thread.sleep(100);
            return 42;
        });
        Integer result = future.get();
        System.out.println(result);

    }
}
