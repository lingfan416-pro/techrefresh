package org.lfan142.concurrency.codeexample;

import java.util.concurrent.*;

public class CallableDemo {

    public static void main(String[] args) {
        Callable<String> c = () -> {
            if(true) throw new Exception("failed");
            return "ok";
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> future = executor.submit(c);

        try{
            String result = future.get();
        } catch (ExecutionException e){
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();

    }
}
