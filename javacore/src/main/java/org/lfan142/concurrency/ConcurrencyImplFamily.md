# Main implementation families in Java concurrency

## JVM intrinsic monitor locks
This is the synchronized family.
Examples:
- synchronized block 
- synchronized method 
- wait() / notify() / notifyAll()

### Underlying idea
These are implemented by the JVM itself, using:
- object header / mark word 
- monitor enter / exit bytecode 
- inflated object monitors under contention

This is the classic alternative to AQS. Typical examples

```
synchronized (lock) {
// critical section
}
```
So if someone asks “what is not based on AQS?”, the first answer is:
`synchronized` / object monitor locks

## AQS-based synchronizers
This is the java.util.concurrent.locks / synchronizer family built on:
`AbstractQueuedSynchronizer`
Examples:
- ReentrantLock 
- ReentrantReadWriteLock 
- Semaphore 
- CountDownLatch 
- ReentrantReadWriteLock 
- many Condition-related mechanisms 
- FutureTask also uses AQS(AbstractQueuedSynchronizer)-style synchronization ideas internally

### Underlying idea
AQS gives:
- a state field
- a FIFO/CLH-style wait queue
- exclusive or shared acquisition model
This is the main framework for many explicit synchronizers.

## Custom state-based lock implementations
These are not classic object monitors and not standard AQS-based locks.

Main example: StampedLock
StampedLock is the best-known one here. It is based on:
- a long state 
- stamp/version tokens 
- read lock / write lock / optimistic read 
- custom waiter coordination

Not AQS-based This is important.  So another answer to your question is:
`StampedLock` uses its own state/stamp-based design, not AQS

## CAS / lock-free / non-blocking mechanisms
These are not “locks” in the classic mutual-exclusion sense, 
but they are another big implementation style for concurrency control.
Examples:
- AtomicInteger 
- AtomicLong 
- AtomicReference 
- LongAdder 
- ConcurrentLinkedQueue 
- many parts of ConcurrentHashMap
  Underlying idea

They use:
- CAS (compareAndSet)
- volatile semantics 
- retry loops 
- striped counters 
- lock-free / non-blocking algorithms

So these are not really lock implementations, but they are definitely another major concurrency implementation family.

| Family                       | Main examples                                                            | Core mechanism                            |
|------------------------------| ------------------------------------------------------------------------ | ----------------------------------------- |
| **Monitor-based**            | `synchronized`, `wait/notify`                                            | JVM object monitor                        |
| **AQS-based(AbstractQueuedSynchronizer)**              | `ReentrantLock`, `Semaphore`, `CountDownLatch`, `ReentrantReadWriteLock` | `state` + wait queue                      |
| **Stamp/state-based custom** | `StampedLock`                                                            | state bits + stamp/version + custom queue |
| **CAS / lock-free**          | `AtomicInteger`, `LongAdder`, `ConcurrentLinkedQueue`                    | CAS + volatile + retry                    |



