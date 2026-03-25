# CountDownLatch
CountDownLatch is a concurrency utility in Java used to let one or more threads wait until a set of operations has finished.
CountDownLatch is a one-shot synchronization aid that allows one or more threads to wait until a set of operations performed by other threads completes. It is initialized with a count, countDown() decreases that count, and await() blocks until the count reaches zero.
Package: `java.util.concurrent.CountDownLatch`

Example: 
`CouontDownLatch latch = new CountDownLatch(3);`
This means:
- the latch starts with count = 3 
- some threads call countDown()
- waiting threads call await()
- when the count reaches 0, the waiting threads are released

Example:
```java

package org.lfan142.concurrency.codeexample;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExm {


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is working");

            } finally {
                latch.countDown();
            }

        };
        Thread t1 = new Thread(runnable, "worker-1");
        Thread t2 = new Thread(runnable, "worker-2");
        Thread t3 = new Thread(runnable, "worker-2");
        t1.start();
        t2.start();
        t3.start();
        latch.await();
        System.out.println("all workers finishes");



    }
}
```

## Typical use cases
-  Wait for multiple tasks to finish

Example:
    - start 5 parallel subtasks
    - main thread waits for all of them
- Start multiple threads at the same time
You can use a latch like a start gate.
- Service startup coordination
Example:
application waits until several components initialize
- Testing concurrent code
Useful for coordinating test threads precisely

## Difference from join()
- join() is thread lifecycle waiting
- CountDownLatch is general coordination

## Difference from CyclicBarrier
CountDownLatch
- one-shot 
- count only goes down 
- waiting threads are released when count reaches 0

CyclicBarrier
- reusable 
- multiple threads wait for each other at a barrier point 
- all proceed together when enough threads arrive

Rule of thumb:
- wait for tasks to finish → CountDownLatch 
- all threads meet at same phase repeatedly → CyclicBarrier

