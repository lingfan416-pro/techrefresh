# ThreadPoolTaskExecutor
`ThreadPoolTaskExecutor` is Spring's convenient wrapper around Java's `ThreadPoolExecutor`. It is typically configured as Spring bean 
and used with @Async to run business tasks asynchronously. It supports settings such as core pool size, max pool size, queue capacity, keep-alive time, thread name prefix, and rejection policy.
In Spring Boot microservices,it is a common best practice to define one shared bounded business executor and possibly a separate scheduler executor, rather than creating threads directly in business code.

`ThreadPoolTaskExecutor` is wrapper around  a Java thread pool, usually used for:
- @Async
- application async tasks
- custom executors in Spring Boot
Package:
`org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor`

## Core Idea
It is basically a pring-friendly wrapper over `ThreadPoolExecutor`. So if ExecutorService is the standard Java way, then:
`ThreadPoolTaskExecutor` is the Spring-style way to configure and use a thread pool as a bean.

## Why spring uses it
In plain java, we might write:
`
ExecutorService executor = new ThreadPoolExecutor(
    4,
    8,
    60,
    TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100)
);
`
In Spring, we often want:
- bean-managed lifecycle
- easy integration with @Async
- thread name prefix
- clean configuration in one place
- graceful shutdown with application context

This is why `ThreadPoolTaskExecutor` is commonly used.

## Main properties
1. CorePoolSize: minimum number of threads kept in the pool.
2. maxPoolSize: Maximum number of threads allowed
3. queueCapacity: How many tasks can wait in queue before more threads are created up to max.
4. keepAliveSeconds: How long extra threads above core size may stay alive when idle.
5. threadNamePrefix: Very useful for logs and debugging.
6. rejectedExecutionHandler: what to do when pool and queue are full. Example:
   - AbortPolicy
   - CallerRunsPolicy
   - DiscardPolicy
   - DiscardOldestPolicy