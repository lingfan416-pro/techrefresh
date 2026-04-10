# ThreadPoolExecutor
ThreadPoolExecutor is the core configurable thread-pool implementation in Java. It manages a set of worker threads and a task queue, and its behavior is controlled mainly by corePoolSize, maximumPoolSize, keepAliveTime, the work queue, the thread factory, and the rejection policy. On task submission, it creates threads up to core size, then uses the queue, then grows toward max size if the queue is full, and finally rejects tasks when both queue and thread limits are exhausted. In production, the most important design points are bounded queues, explicit rejection policies, workload-aware sizing, and proper monitoring.

`ThreadPoolExecutor` is the core Java class used to implement a configurable thread pool. Package:
`java.util.concurrent.ThreadPoolExecutor`
It is the most important concrete implementation behind many executor patterns. ThreadPoolExecutor is a configurable executor that manages a pool of worker threads, a task queue, and overload handling policies.

## Why it exists
Creating threads directly like this:
`new Thread(() -> doWork()).start();`
has problems:
- thread creation is expensive 
- too many threads can exhaust memory and CPU 
- no central queue or lifecycle control 
- hard to monitor and tune

`ThreadPoolExecutor` solves this by separating:
- task submission 
- thread management

## Core Idea
A `ThreadPoolExecutor` has:
- a set of worker threads
- a task queue
- rules for when to create a new threads
- rules for what to do when overloaded
So it is really:
`threads + queue + lifecycle + rejection policy`

## Constructor
The common constructor is:
`
ThreadPoolExecutor(
    int corePoolSize,
    int maximumPoolSize,
    long keepAliveTime,
    TimeUnit unit,
    BlockingQueue<Runnable> workQueue,
    ThreadFactory threadFactory,
    RejectedExecutionHandler handler
)
`
We can also use shorter constructors without the last one or two arguments.
## Meaning of each parameter
1. corePoolSize： The number of core threads the pool tries to keep available.
   - these are the “baseline” worker threads 
   - usually kept alive by default 
   - created lazily unless prestarted
2. maximumPoolSize. The upper limit of worker threads.
   - pool cannot grow beyond this 
   - extra threads above core are created only under pressure
3. keepAliveTime: How long non-core idle threads may stay alive before being removed. If the pool grew above core size during high load, extra threads will be removed after being idle for 60 seconds.
4. workQueue. The queue that stores tasks waiting to be executed. Common choices:
   - ArrayBlockingQueue 
   - LinkedBlockingQueue 
   - SynchronousQueue 
   - PriorityBlockingQueue
This choice has a huge effect on behavior.

5. ThreadFactory. Controls how new threads are created. Used for:
   - thread naming 
   - daemon flag 
   - uncaught exception handler


6. RejectedExecutionHandler. What happens when:
    - queue is full 
    - pool is at max size 
    - and a new task arrives

This is your overload policy.


## Execution flow

ThreadPoolExecutor roughly follows this order:
- Step 1: If running threads < corePoolSize → create a new thread to run the task 
- Step 2  Else, try to put the task into the queue 
- Step 3 If queue is full and running threads < maximumPoolSize → create a new thread beyond core size
- Step 4 If queue is full and thread count == maximumPoolSize → reject the task
This order is very important.

## Pool size at initialization
By default: the pool usually starts with 0 threads
Even if:
corePoolSize = 4
that does not mean 4 threads already exist.
Threads are usually created lazily, when tasks arrive.
You can explicitly prestart them:
`executor.prestartCoreThread();`
`executor.prestartAllCoreThreads();`

## Common queue choices
1. ArrayBlockingQueue: Bounded fixed-size queue. Good for:
    - predictable memory use 
    - explicit backpressure

2. LinkedBlockingQueue. Can be bounded or unbounded, but often used unbounded by accident.
   Important effect: if queue is effectively unbounded, the pool may almost never grow beyond corePoolSize
   Why?
   Because once core threads are full, tasks go into queue, and queue never fills, so no reason to create extra threads up to maximumPoolSize.
3. SynchronousQueue: No real storage capacity. Each task must be handed directly to a thread.
Effect:
   - tends to grow threads more aggressively 
   - often used in cached thread pool style

4. PriorityBlockingQueue. Tasks are ordered by priority. Use only if priority ordering is really intended.

## Rejection policies
When the pool is saturated, tasks are rejected.
1. AbortPolicy. Default in many cases. Throws RejectedExecutionException.
`new ThreadPoolExecutor.AbortPolicy()`
2. CallerRunsPolicy. The caller thread runs the task itself.
`new ThreadPoolExecutor.CallerRunsPolicy()`. 
This is often a useful backpressure mechanism.
3. DiscardPolicy. Silently drops the task.
4. DiscardOldestPolicy. Drops the oldest queued task, then tries again.

### Best Practice
For business systems, silent discard is often dangerous unless explicitly intended.
I prefer an explicit overload strategy such as CallerRunsPolicy, fail-fast rejection, or application-specific degradation, rather than silently losing work.

## Main methods
1. execute(Runnable task).
   Submit task, no Future returned.
`executor.execute(() -> doWork());`
2. submit(...). Inherited from ExecutorService, returns Future.
`Future<Integer> future = executor.submit(() -> 42);`
3. shutdown() Graceful shutdown.
   No new tasks accepted; existing ones continue.
4. shutdownNow() Try to stop quickly, usually via interrupts.
5. awaitTermination(...) Wait until pool fully stops.
6. getPoolSize() Current number of threads.
7. getActiveCount() Currently active worker threads.
8. getQueue() Access underlying queue.Mostly for monitoring/debugging, not normal business logic.


## core vs max threads
1. Core threads
- baseline workers 
- usually retained

Non-core threads
- extra threads above core 
- created only under load 
- removed after idle keepAliveTime

## Allowing core threads to time out

By default, core threads are usually kept alive.
But we can allow them to time out too:
`executor.allowCoreThreadTimeOut(true);`
Then even core threads can die after idle time.

## Why queue choice matters so much
If queue is too large or unbounded 
- tasks pile up in queue 
- latency grows 
- pool may never scale beyond core 
If queue is bounded
- overload becomes visible 
- pool can grow toward max when queue fills 
- backpressure is clearer

So pool tuning is not just thread count; queue design is equally important

## Common production best practices
1. Use bounded queue: Avoid unbounded queue unless you really understand the consequences.
2. Choose rejection policy deliberately: Do not ignore overload behavior.
3. Name threads clearly with ThreadFactory. Useful for logs and thread dumps.
4. Useful for logs and thread dumps: Do not mix unrelated workloads blindly in one pool.
5. Monitor pool metrics. Track:
   - active count 
   - queue size 
   - rejection count 
   - task latency 
   - execution time
6. Size for container/resource limits
   In Docker/Kubernetes, size based on container CPU/memory, not host machine only.

## CPU-bound vs I/O-bound tuning
1. CPU-bound tasks:
- Smaller pool. 
- Often around CPU core count.
  Why?
Too many threads only add context switching.

2. Blocking I/O tasks
May need a larger pool because threads spend time waiting. But still:
- bounded 
- monitored 
- protected by downstream limits too

# Common mistakes
1. Mistake 1: huge unbounded queue
Looks safe, but can cause:
- memory growth 
- huge latency 
- no scaling beyond core threads
2. Mistake 2: maxPoolSize set but never used. Often caused by unbounded queue.
3. Mistake 3: using one pool for everything. Can cause interference between workloads.
4. Mistake 4: no shutdown. App may not terminate cleanly.
5. Mistake 5: ignoring rejection. Then overload behavior surprises you in production.



