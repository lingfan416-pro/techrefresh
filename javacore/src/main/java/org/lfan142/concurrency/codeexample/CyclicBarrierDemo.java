package org.lfan142.concurrency.codeexample;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3);

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " phase 1");
                barrier.await();

                System.out.println(Thread.currentThread().getName() + " phase 2");
                barrier.await();

                System.out.println(Thread.currentThread().getName() + " phase 3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(task, "Thread 1").start();
        new Thread(task, "Thread 2").start();
        new Thread(task, "Thread 3").start();
    }
}
