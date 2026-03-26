package org.lfan142.concurrency.codeexample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ComputableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> 42);
        System.out.println(future.join());


        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(s -> s + " world")
                .thenApply(String::toUpperCase);
        System.out.println(future1.join());

        CompletableFuture<Integer> calFuture = CompletableFuture.supplyAsync(() -> 10)
                .thenApplyAsync(x -> x* x);

        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 20);

        CompletableFuture combinedFuture = f1.thenCombine(f2, Integer::sum);
        System.out.println(combinedFuture.join());

        CompletableFuture<String> sf = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> sf2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<Void> all = CompletableFuture.allOf(sf, sf2);
        all.join();
        System.out.println(sf.get() + " "+sf2.get());
        

     }


}
