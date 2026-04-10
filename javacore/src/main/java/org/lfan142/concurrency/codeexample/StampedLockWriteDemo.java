package org.lfan142.concurrency.codeexample;

import java.util.concurrent.locks.StampedLock;

public class StampedLockWriteDemo {

    public static void main(String[] args) {
        StampedLock lock = new StampedLock();
        long stamp = lock.writeLock();
        try{
            //write shared state
        } finally {
            lock.unlock(stamp);
        }
    }
}
