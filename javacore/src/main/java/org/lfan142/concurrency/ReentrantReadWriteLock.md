# ReentrantReadWriteLock
ReentrantReadWriteLock is a reentrant lock that provides separate read and write locks. Multiple threads can hold the read lock concurrently as long as no thread holds the write lock, while the write lock is exclusive. 
It is useful for protecting shared data structures that are read frequently and written infrequently, because it allows better concurrency for readers than a normal exclusive lock.
ReentrantReadWriteLock is a Java lock that separates read access and write access.
`java.util.concurrent.locks.ReentrantReadWriteLock`

Core idea:
It allows:
- multiple threads to hold the read lock at the same time 
- only one thread to hold the write lock 
- no readers while a writer holds the write lock
So it is useful when:
reads are frequent, writes are rare, and you want better concurrency than a single exclusive lock.

## Why it exists
With a normal synchronized or ReentrantLock, both reads and writes are exclusive. That means even simple read operations block each other.
Example:
```
    lock.lock();
    try {
        return map.get(key);
    } finally {
        lock.unlock();
    }
```
If 100 threads are only reading, they still have to go one by one. ReentrantReadWriteLock improves this by saying:
- readers can share the lock 
- writers still need exclusive access

## Main structure
```
ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

Lock readLock = rwLock.readLock();
Lock writeLock = rwLock.writeLock();
```
So it gives you two locks:
- readLock()
- writeLock()

## Basic rules
Read lock
- multiple readers can acquire it together 
- but not if a writer already holds the write lock
Write lock
- exclusive 
- only one writer at a time 
- blocks both readers and other writers


## Simple Example
Meaning:
many threads can call get() concurrently
put() is exclusive
```java
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

```
## Why it is called “reentrant”

Like ReentrantLock, it is reentrant.
That means:
- a thread that already holds the write lock can acquire it again
- a thread that already holds a read lock can acquire the read lock again
So repeated locking by the same thread does not deadlock itself.

## Important behavior: write lock can downgrade, but read lock cannot safely upgrade
Lock downgrading: allowed
A thread holding the write lock can acquire the read lock before releasing the write lock.
```
    writeLock.lock();
    try {
        // modify state
        readLock.lock();
    } finally {
        writeLock.unlock();
    }
    
    try {
        // continue reading safely
    } finally {
        readLock.unlock();
    }
```
This is called lock downgrading.

## Lock upgrading: dangerous / generally not supported
A thread holding a read lock should not try to acquire the write lock in the normal way.
Example:
```
readLock.lock();
try {
    // bad idea
    writeLock.lock();
} finally {
    readLock.unlock();
}

```

Why dangerous?
Because:
- other readers may also hold read locks
- all readers are waiting to upgrade
- nobody releases first
- deadlock or permanent waiting can happen
- 
ReentrantReadWriteLock supports downgrading from write to read, but not safe upgrading from read to write.

## Fair vs non-fair
You can create it as:
### Non-fair (default)
`ReentrantReadWriteLock lock = new ReentrantReadWriteLock();`
### Fair
`ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);`

Non-fair
- usually higher throughput 
- some threads may wait longer

Fair 
- roughly honors waiting order 
- reduces starvation risk 
- usually lower performance

In practice, non-fair is more common unless fairness is clearly required.

## When it is useful
Good fit when:
- data is read a lot 
- writes are infrequent 
- read operations are not extremely tiny 
- concurrent reads matter

Typical examples:
- configuration cache 
- metadata registry 
- in-memory lookup tables 
- mostly-read shared state

## When it is not useful
It may be a poor choice when:
- writes are frequent 
- contention is high on writes 
- read operations are very short 
- the protected structure already has a better concurrent implementation

Examples:
- for a concurrent map, ConcurrentHashMap is often better than manually protecting HashMap with a read-write lock 
- for simple exclusive access, ReentrantLock may be simpler

## Common methods
### Common methods
- lock()
- unlock()
- tryLock()
- lockInterruptibly()
  
### Extra inspection methods on ReentrantReadWriteLock
- getReadLockCount()
- isWriteLocked()
- isWriteLockedByCurrentThread()
- getQueueLength()

These are mainly for monitoring/debugging.

## Comparison with ReentrantLock
ReentrantLock
- one lock 
- all access is exclusive 
- simpler

ReentrantReadWriteLock
- two logical locks 
- read-read concurrency allowed 
- more complex 
- helpful mainly in read-heavy scenarios


## Comparison with synchronized
synchronized
- intrinsic monitor 
- simpler 
- one exclusive lock only

ReentrantReadWriteLock 
- explicit lock API 
- separate read and write modes 
- supports fairness options 
- more flexible, but more complex

## Comparison with StampedLock
ReentrantReadWriteLock
- reentrant 
- read/write locks 
- easier and safer to use

StampedLock 
- not reentrant 
- supports optimistic read 
- can perform better in some read-heavy cases 
- more dangerous and complex

## Common pitfalls
1. Forgetting to unlock 
2. Always use try/finally. 
3. Trying to upgrade read → write , Can deadlock or hang. 
4. Using it when writes are common. Then the benefit disappears. 
5. Using it where concurrent collections already solve the problem
Example: prefer ConcurrentHashMap in many map scenarios

6. Assuming it always improves performance. It helps only if the read-heavy pattern is real.

