# CompletableFuture
CompletableFuture is a more powerful, modern alternative to plain Future.
Future is mainly a holder for an asynchronous result, while CompletableFuture supports asynchronous computation pipelines and composition.
Package:
 `java.util.concurrent.CompletableFuture`

## Core Idea
A normal Future mainly lets you 
- submit async work
- wait for result with get()
- cancel
- check done status

But CompletableFuture lets you do much more:
- run tasks asynchronously 
- chain multiple async steps
- transform results
- combine multiple async tasks
- handle exceptions
- avoid blocking style in many cases

## Why Future was not enough
With plain Future, code often looks like this:
```
Future<Integer> future = executor.submit(() -> 42);
Integer result = future.get(); // blocking
```
Problems:
- get() blocks 
- hard to chain another step 
- hard to combine multiple futures 
- poor exception composition 
- no fluent async pipeline

That is why CompletableFuture was introduced in Java 8.

## Basic example
Create an async task
```
public class ComputableFutureDemo {

    public static void main(String[] args) {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> 42);
        System.out.println(future.join());
    }
}
```
Here:
- supplyAsync() runs asynchronously 
- it returns a CompletableFuture<Integer>
- join() gets the result

## Two main creation styles
1. `runAsync()`
For tasks with no return value
```
CompletableFuture<Void> future =
        CompletableFuture.runAsync(() -> {
            System.out.println("running");
        });
```
Equivalent idea:
- like async Runnable

2. supplyAsync()
For tasks that return a value
```
CompletableFuture<String> future =
        CompletableFuture.supplyAsync(() -> "hello");
```
Equivalent idea:
- like async Callable

## Main difference from Future
CompletableFuture is not only a result holder. It is also a completion stage.
That means:
- when one stage completes 
- another stage can automatically run
```
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(s -> s + " world")
                .thenApply(String::toUpperCase);
    System.out.println(future1.join());
```
This is the real power.
## Common methods
1. thenApply()
   Transform the result
```java
CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(String::length);
```
Meaning:
- first stage returns "hello"
- second stage converts it to 5
Like synchronous transformation after completion

2. thenAccept()
Consume result, no return value.
```
CompletableFuture<Void> future =
        CompletableFuture.supplyAsync(() -> "hello")
                .thenAccept(System.out::println);
```
3. thenRun()
Run next step without using previous result
```java
CompletableFuture<Void> future =
        CompletableFuture.supplyAsync(() -> "hello")
                .thenRun(() -> System.out.println("done"));
```
## Async variants
Many methods have async versions:
- thenApply() vs thenApplyAsync()
- thenAccept() vs thenAcceptAsync()

Non-async methods may continue in the same completion thread, while async methods schedule the next stage asynchronously.
### Difference
- thenApply(): May run in the thread that completes the previous stage. 
- thenApplyAsync(): Schedules next stage asynchronously, usually in ForkJoinPool or given executor.
```java
CompletableFuture<Integer> calFuture = CompletableFuture.supplyAsync(() -> 10)
                .thenApplyAsync(x -> x* x);
```


## Combining multiple futures
This is where CompletableFuture becomes much better than plain Future.
1. thenCombine(): Combine two independent results
```
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 20);
        
        CompletableFuture combinedFuture = f1.thenCombine(f2, Integer::sum);
        System.out.println(combinedFuture.join());
```

2. allOf(): Wait for all futures
```
   CompletableFuture<String> sf = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> sf2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<Void> all = CompletableFuture.allOf(sf, sf2);
        all.join();
        System.out.println(sf.get() + " "+sf2.get());
```
Important:
- allOf() returns CompletableFuture<Void>
- you still need to read each original future

3. anyOf(): Wait for the first completed one

```java
CompletableFuture<Object> any =
        CompletableFuture.anyOf(f1, f2);
```

## Exception handling
1. exceptionally(): Provide fallback when exception happens
```
CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("error");
        }).exceptionally(ex -> 0);

System.out.println(future.join()); // 0
```
2. handle(): Handle both success and failure
```
CompletableFuture<String> future =
        CompletableFuture.supplyAsync(() -> "hello")
                .handle((result, ex) -> {
                    if (ex != null) {
                        return "fallback";
                    }
                    return result.toUpperCase();
                    });
```
3. whenComplete(): Observe result/exception, but usually not transform it
```java
CompletableFuture<String> future =
        CompletableFuture.supplyAsync(() -> "hello")
                .whenComplete((result, ex) -> {
                    System.out.println("result=" + result + ", ex=" + ex);
                });
```
Easy distinction
- exceptionally → fallback on failure
- handle → inspect and transform both success/failure
- whenComplete → side effect / logging

## get() vs join()
Both can wait for result.
1. get()
From Future
- checked exceptions
- throws InterruptedException, ExecutionException

2. join()
join() is often more convenient in pipelines because it does not force checked exception handling.
From CompletableFuture
- throws unchecked CompletionException
Example:
```
Integer result = future.join();
```
## Custom executor
In production systems, I prefer providing an explicit executor instead of relying blindly on the common pool.
By default, many async methods use:
- ForkJoinPool.commonPool()
 But in real applications, we often want our own executor.
example:
```java
ExecutorService executor = Executors.newFixedThreadPool(4);

CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> 42, executor)
                .thenApplyAsync(x -> x * 2, executor);
```
Why this matters:
- more predictable resource control
- avoid overusing common pool 
- separate IO-bound and CPU-bound tasks


## Typical use cases
- Parallel service calls: Call multiple remote services in parallel, then combine results.
- Async pipeline: Load data → transform → enrich → persist
- Non-blocking orchestration: Run independent work and combine later
- Fallback handling: If service call fails, use default value or backup source
  Example: parallel service calls, This is a common backend pattern.
```
CompletableFuture<String> userFuture =
        CompletableFuture.supplyAsync(() -> "UserA");

CompletableFuture<Integer> scoreFuture =
        CompletableFuture.supplyAsync(() -> 95);

CompletableFuture<String> result =
        userFuture.thenCombine(scoreFuture,
                (user, score) -> user + ":" + score);

System.out.println(result.join()); // UserA:95
```

## Why it is called “Completable”
Because unlike plain Future, it can also be completed manually.
Example:
```
CompletableFuture<String> future = new CompletableFuture<>();

future.complete("done");

System.out.println(future.join()); // done
```

Or fail manually:
```
future.completeExceptionally(new RuntimeException("failed"));
```
This is useful in framework/library code.


## CompletableFuture vs Future
| Feature              | Future | CompletableFuture |
| -------------------- | ------ | ----------------- |
| Get result later     | Yes    | Yes               |
| Cancel task          | Yes    | Yes               |
| Blocking `get()`     | Yes    | Yes               |
| Chain steps          | No     | Yes               |
| Combine tasks        | Hard   | Easy              |
| Exception pipeline   | Weak   | Strong            |
| Manual completion    | No     | Yes               |
| Functional style API | No     | Yes               |


## Common pitfalls
1. Calling join() too early
If you do this immediately, you lose async benefit. 
Bad example:
```
CompletableFuture<Integer> f = CompletableFuture.supplyAsync(() -> 42);
Integer x = f.join();
```
If done repeatedly in sequence, this becomes effectively synchronous.
2. Using common pool blindly: Can cause resource contention in real systems.
ForkJoinPool.commonPool() — a shared global thread pool provided by the JVM.
It is the default pool used by many CompletableFuture async methods when you do not provide your own executor.
3. Mixing blocking IO inside wrong executor
If async tasks block heavily, pool design matters a lot.
4. Exception handling forgotten
If pipeline fails and you never inspect it, bugs become hidden.



