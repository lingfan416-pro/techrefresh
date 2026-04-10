package org.lfan142.concurrency.codeexample;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private final String prefix;
    private final AtomicInteger count = new AtomicInteger(1);

    public NamedThreadFactory(String prefix){
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(prefix + "-" + count.getAndIncrement());
        return t;
    }
}
