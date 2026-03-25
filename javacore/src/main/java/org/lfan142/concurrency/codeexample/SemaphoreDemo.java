package org.lfan142.concurrency.codeexample;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    private static final Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName()+ " acquired permit");
                Thread.sleep(200);
                System.out.println(Thread.currentThread().getName() + " releasing permit");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }

        };
        for (int i = 0; i < 5; i++) {
            new Thread(task, "T" + i).start();
        }
    }
}
