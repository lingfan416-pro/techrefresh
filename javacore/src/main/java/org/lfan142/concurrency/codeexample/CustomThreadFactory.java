package org.lfan142.concurrency.codeexample;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {

    private final String prefix;
    private final boolean daemon;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public CustomThreadFactory(String prefix, boolean daemon){
        this.prefix = prefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(prefix + "-" + threadNumber.getAndIncrement());
        t.setDaemon(daemon);
        t.setUncaughtExceptionHandler((thread, ex) ->{
            System.out.println("Thread " + thread.getName() + " failed: " + ex.getMessage());
        });
        return t;
    }
}
