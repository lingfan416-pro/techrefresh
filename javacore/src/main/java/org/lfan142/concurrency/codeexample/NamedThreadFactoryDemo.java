package org.lfan142.concurrency.codeexample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NamedThreadFactoryDemo {

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(
                2, //the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set
                2, //the maximum number of threads to allow in the pool
                0L, // when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
                TimeUnit.MILLISECONDS, //the time unit for the keepAliveTime argument
                new ArrayBlockingQueue<>(10), // the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
                new NamedThreadFactory("business-worker")

        );

        executorService.submit(()->
        {
            System.out.println(Thread.currentThread().getName());
        });
        executorService.shutdown();
    }
}
