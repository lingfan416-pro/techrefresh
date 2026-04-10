package org.lfan142.concurrency.codeexample;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheDemo {

    private final Map<String, String> map = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public String get(String key){
        rwLock.readLock().lock();

        try{
            return map.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void put(String key, String value){
        rwLock.writeLock().lock();
        try{
            map.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

}
