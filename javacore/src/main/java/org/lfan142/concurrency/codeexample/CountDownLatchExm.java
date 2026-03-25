package org.lfan142.concurrency.codeexample;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExm {


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is working");

            } finally {
                latch.countDown();
            }

        };
        Thread t1 = new Thread(runnable, "worker-1");
        Thread t2 = new Thread(runnable, "worker-2");
        Thread t3 = new Thread(runnable, "worker-2");
        t1.start();
        t2.start();
        t3.start();
        latch.await();
        System.out.println("all workers finishes");



    }
}