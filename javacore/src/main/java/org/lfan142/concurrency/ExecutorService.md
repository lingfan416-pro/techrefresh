# ExecutorService
ExecutorService is a higher-level concurrency API in Java for managing and 
executing asynchronous tasks using a pool of worker threads. It supports task submission via execute() and submit(), result retrieval through Future, 
and controlled shutdown through methods like shutdown() and awaitTermination(). 
It is generally preferred over manually creating threads because it improves scalability, reuse, lifecycle control, 
and resource management.
Package: `java.util.concurrent.ExecutorService`

## Core idea
Instead of doing this:
```
new Thread(() -> doWork()).start();
new Thread(() -> doWork()).start();
new Thread(() -> doWork()).start();
```
with ExecutorService we can do:
```

ExecutorService executor = Executors.newFixedThreadPool(3);
executor.submit(() -> dowWork());
executor.submit(() -> dowWork());
executor.submit(() -> dowWork());

```
This is better because:
- threads can be reused 
- task management is easier 
- lifecycle control is better 
- performance is usually better than creating many new threads

## Why ExecutorService exists
Creating threads manually has problems:
- thread creation is expensive 
- too many threads can exhaust system resources 
- you need to manage lifecycle yourself 
- cancellation, timeout, result handling become messy

ExecutorService separates:
- task submission from thread management
That is the key design idea.

## Interface hierarchy
Simplified:
Executor
└── ExecutorService
        └── ScheduledExecutorService

### Executor
Basic interface:
`void execute(Runnable command)`

### ExecutorService
Adds:
- submit tasks 
- get results with Future 
- shutdown control 
- bulk task methods like invokeAll, invokeAny

## Main methods
1. execute(Runnable task)
Runs a task, no return value.
- fire-and-forget
- if task throws exception, you do not get it through Future
```
executor.execute(() -> {
    System.out.println("running");
});
```

2. submit(...)
Submits task and returns a Future.

- Submit Runnable
```
Future<?> future = executor.submit(() -> {
    System.out.println("task");
});
```
- Submit Callable
```
Future<Integer> future = executor.submit(() -> 42);
```
Here Callable can return a value and throw checked exceptions.

3. shutdown()
Graceful shutdown.
`executor.shutdown();`
Meaning:
- stop accepting new tasks
- already submitted tasks continue
4. shutdownNow()
Aggressive shutdown.
`executor.shutdownNow();`
Meaning:
- try to stop running tasks
- interrupt worker threads
- return tasks that never started
Important:
- not guaranteed to stop everything immediately

5. awaitTermination(timeout, unit)
Wait for executor to fully terminate.
```
executor.shutdown();
executor.awaitTermination(5, TimeUnit.SECONDS);
```
Useful during clean shutdown.
6. isShutdown()
Checks whether shutdown has been requested.

7. isTerminated()
Checks whether all tasks are finished and pool is fully terminated.

8. invokeAll(...)
Submit a batch of tasks and wait for all of them.

```
List<Callable<Integer>> tasks = List.of(
() -> 1,
() -> 2,
() -> 3
);

List<Future<Integer>> futures = executor.invokeAll(tasks);
```
9. invokeAny(...)
Submit a batch of tasks and return the first successful result.
`Integer result = executor.invokeAny(tasks);`


## Runnable vs Callable
Runnable
- no return value
- cannot throw checked exception
```
Runnable r = () -> System.out.println("hello");
```
Callable<V>
- returns value
- can throw checked exception
```Callable<Integer> c = () -> 100;```

submit(Callable) is useful when you need a result or exception propagation through Future.

## Future
When you submit a task, you usually get a Future.
Example:
```
ExecutorService executor = Executors.newFixedThreadPool(2);

Future<Integer> future = executorService.submit(() -> {
    Thread.sleep(100);
    return 42;
});
Integer result = future.get();
System.out.println(result);
```
### Common Future method:
1. get(): wait and get the result.
2. get(timeout, unit) wait with timeout.
3. cancel(boolean mayInterruptIfRunning) cancel task
4. isDone() check if finished
5. isCancelled() check if cancelled

## Common Thread pool types:
Usually created by Executors, though in production direct ThreadPoolExecutor is often preferred.
1. newFixedThreadPool(n)
Fixed number of worker threads.
```
ExecutorService executor = Executors.newFixedThreadPool(4);
```
Use when:
- concurrency level is stable
- you want bounded worker count

2. newCachedThreadPool()
Creates threads as needed, reuses old ones.
```
ExecutorService executor = Executors.newCachedThreadPool();
```

Use carefully:
- can create many threads
- risky under high load

3. newSingleThreadExecutor()
Single worker thread.
```
ExecutorService executor = Executors.newSingleThreadExecutor();
```
Use when:
- tasks must run sequentially
- but you still want executor lifecycle management

4. newScheduledThreadPool(n)
Actually returns ScheduledExecutorService.
Use for:
- delayed tasks 
- periodic tasks

## Why thread pools are better than creating threads manually
1. Thread reuse
Threads are expensive to create/destroy.
ExecutorService reuses worker threads.
2. Better resource control
You can limit concurrency.
Example:
- only 10 worker threads
- instead of accidentally creating 10,000 threads
3. Better task management
You get:
- queueing
- cancellation
- result tracking
- timeout handling
- graceful shutdown

4. Better architecture

Application code submits tasks, pool manages execution.

## Underlying implementation
Most common implementations are based on:
- ThreadPoolExecutor 
- blocking task queue 
- worker thread set 
- rejection policy 
- lifecycle state machine

Typical internal model:
- task submitted 
- if worker thread available → execute 
- otherwise queue task 
- if queue/pool limits reached → maybe reject task

So the executor is really:
- a task queue 
- plus worker threads 
- plus lifecycle management

## Important production point
In production code, I prefer explicit ThreadPoolExecutor configuration over blindly 
using Executors factory methods, because I want bounded queues and predictable overload behavior.
Using Executors.newFixedThreadPool(...) is okay for learning/interviews, 
but in real systems people often prefer explicit ThreadPoolExecutor.

Why?
Because Executors factory methods may hide queue behavior and can be dangerous under load.

Example:
```
ExecutorService executor = new ThreadPoolExecutor(
    4,
    8,
    60,
    TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100)
);
```
This gives control over:
- core pool size 
- max pool size 
- keep alive time 
- queue size 
- rejection policy

## Shutdown best practice
Proper shutdown pattern:
```
executor.shutdown();
try {
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        executor.shutdownNow();
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
    Thread.currentThread().interrupt();
}
```

Why:
- graceful stop first
- force stop if needed
- preserve interrupt signa

## ExecutorService vs new Thread
ExecutorService is preferred over manual thread creation because it decouples task submission from thread management and provides better scalability, resource control, result handling, and shutdown semantics.

new Thread
- manual thread management 
- one thread per task style 
- poor scalability if overused

ExecutorService 
- submit tasks, not raw threads 
- reuses workers 
- better lifecycle and control

## ExecutorService vs ForkJoinPool
A quick distinction:
- ExecutorService: General-purpose task execution
- ForkJoinPool: Optimized for recursive divide-and-conquer parallelism and work-stealing
Example use:
  - parallel computation
  - parallelStream()

Very short version
- ExecutorService = thread pool + task management 
- submit tasks instead of creating threads manually 
- supports Runnable, Callable, Future 
- supports graceful shutdown 
- common implementation = ThreadPoolExecutor


