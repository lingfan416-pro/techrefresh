# StampedLock
StampedLock is a Java lock designed for read-heavy concurrent access,
especially when you want something more efficient than ReentrantReadWriteLock in some scenarios.
package: `java.util.concurrent.locks.StampedLock`

## Core idea
It supports 3 modes:
- write lock: exclusive 
- read lock: shared 
- optimistic read: non-blocking, very lightweight read attempt

The lock returns a stamp (long) when you acquire it.
That stamp is like a token proving what kind of lock you currently hold.

## Why StampedLock exists
ReentrantReadWriteLock is good, but sometimes too heavy for very frequent reads.
StampedLock adds:
- cheaper optimistic reads 
- conversion methods between modes 
- potentially better performance in read-heavy workloads

## The 3 locking modes
1. Write lock: Exclusive, only one writer.
```
S
```
2. Read Lock
3. Optimistic read




