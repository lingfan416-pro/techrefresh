# Lock
In Java, a lock is a synchronization mechanism used to protect shared mutable state from concurrent access problems. The simplest built-in form is synchronized, which uses an object monitor. Java also provides the Lock interface, with ReentrantLock as the most common implementation. Compared with synchronized, ReentrantLock gives more flexibility, such as timed lock attempts, interruptible waiting, fairness options, and multiple Conditions. Both locking approaches provide mutual exclusion and also important visibility and ordering guarantees under the Java Memory Model.
In java, Lock means a mechanism that controls exclusive access to shared resources so multiple threads do not corrupt shared state.
There are two big categories:
- Intrinsic lock: `synchronized`
- explicit lock: Lock interface, especially ReentrantLock


## synchronized vs Lock
### synchronized

Advantages:
- simple
- automatic unlock when leaving block
- less error-prone
- built into language/JVM

Disadvantages:
- less flexible
- no timeout lock attempt
- no interruptible lock acquisition
- only one built-in condition queue per monitor

### ReentrantLock
Advantages:
- tryLock()
- timed lock attempt
- interruptible lock acquisition
- multiple Conditions
- optional fairness

Disadvantages:
- must unlock manually
- easier to make mistakes if unlock() is forgotten

`finally` is important with Lock

```
lock.lock();
try{
    //critical section
} finally{
    lock.unlock();
}

```

If you do not use finally, and an exception happens, the lock may never be released.
That can cause deadlock-like behavior.


## Main method of `Lock`
1. lock(): Acquire lock, wait until available.
```
lock.lock();
```
2. unlock(): release lock. `lock.unlock()`
3. tryLock(): Try to acquire immediately, return `true` or `false`
Very useful wen do not want to block forever.
```
if(lock.tryLock()) {
    try{
        //do work
    }finally {
        lock.unlock();
    }
}

```
4. tryLock(timeout, unit) wait for a limited time

```
import java.util.concurrent.TimeUnit;

if(lock.tryLock(1,TimeUnit.SECONDS)){
    try{
        //do work
    } finally{
        lock.unlock();
    }
}
```
5. lockInterruptibly(): acquire lock, but allow interruption while waiting.

```
lock.lockInterruptibly();
```
Useful in cancellable tasks.


## Fair lock vs non-fair lock
ReentrantLock can be created as fair:
```
Lock lock = new ReentrantLock(true);
```
### Fair lock
Threads get lock roughly in waiting order.
Advantage:
- reduces starvation risk

Disadvantage:
- usually lower throughput

### Non-fair lock
Default behavior:
`Lock lock = new ReentrantLock();`
Advantage:
- better performance
Disadvantage:
- some threads may wait longer

### Condition
A big advantage of ReentrantLock is that it supports explicit conditions.
Example:

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class MessageBox {
    private String message;
    private boolean hasMessage = false;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    
    public void put(String message) throws InterruptedException {
        lock.lock();
        try {
            while(hasMessage){
                notFull.await();
            }
            this.message = message;
            hasMessage = true;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public String take( ){
        lock.lock();
        try{
            while(!hasMessage){
                notEmpty.await();
            }
            hasMessage = false;
            String result = message;
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }
}
```
This is similar to wait/notify, but more flexible. synchronized has one implicit condition queue per monitor, while ReentrantLock can create multiple Conditions.


At a lower level, locks solve concurrency by giving guarantees around:
- mutual exclusion
- visibility 
- ordering

So locks are not just “one thread at a time.”
They also make sure:
- writes by one thread become visible to another 
- operations are observed in the correct order

That is why locks help solve more than just race conditions

## Best practices
- Keep critical sections small: Only protect the shared state that really needs protection.
- Always unlock in `finally` For explicit locks.
- Avoid locking on publicly exposed objects. Bad example:
```
synchronized (this) { ... }
```
Because a publicly exposed object can be locked by code you do not control. That creates accidental coupling and can cause:
- unexpected blocking 
- deadlocks 
- starvation 
- very hard-to-debug performance problems
in some shared/public contexts can be risky. Often better:
`private final Object lock = new Object();`
- Prefer higher-level concurrency utilities when suitable
  Examples:
  - ConcurrentHashMap 
  - BlockingQueue 
  - Semaphore 
  - CountDownLatch 
  - ReadWriteLock 
  - StampedLock
Sometimes these are better than manual locking.
## Other lock types worth knowing
### ReadWriteLock
Allows:
- multiple readers 
- one writer
Useful when:
- reads are frequent
- writes are rare

Most common implementation: `ReentrantReadWriteLock`


### StampedLock

More advanced lock with:
- read lock 
- write lock 
- optimistic read

Useful in some high-read scenarios, but more complex.
