# Runnable, Callable and Future
## Runnable
Runnable represents a task that:
- takes no input
- returns no result
- cannot throw checked exceptions directly
It is a interface:
```java
@FunctionalInterface
public interface Runnable{
    void run();
}

```
Example:
```java
Runnable task = () -> {
    System.out.println("task is running");
};
```
Typical Use
- fire-and-forget tasks
- background jobs 
- simple thread execution

Within Thread
```
Thread t = new Thread(() -> {
    System.out.println("task is in progress!");
});
t.start();
```
## Callable
Callable represents a task that:
- takes no input 
- returns a result 
- can throw checked exceptions
Interface:
```java
@FunctionalInterface
public class Callable<V> {
    V call() throws Exception;
}
```
Example
```java
Callable<Integer> task = () -> {
    return 42;
};
```
Typical use
- async computation
- tasks that need to return a value
- tasks that may fail with checked exceptions

## Future
Future represents the result of an asynchronous computation.
When you submit a Callable or Runnable to an ExecutorService, you usually get a Future.

It lets you:
- check whether task is done
- get the result later
- cancel the task
- wait for completion

## Relationship between them
Runnable and Callable describe the work. Future describes the result/status of that work after submission
- Runnable: just the task
- Callable: task + result + checked exception support
- Future: handle for the submitted task’s result/state

## Main difference
| Feature                      | Runnable | Callable | Future                                 |
| ---------------------------- | -------- | -------- | -------------------------------------- |
| Is it a task?                | Yes      | Yes      | No, it is a result handle              |
| Returns value?               | No       | Yes      | `get()` returns task result            |
| Can throw checked exception? | No       | Yes      | `get()` may throw `ExecutionException` |
| Common method                | `run()`  | `call()` | `get()`, `cancel()`, `isDone()`        |


## Important Future methods
1. get() 
wait until result is ready.
``Integer result = future.get();``
2. get(timeout, unit)
wait with timeout.
`Integer result = future.get(1, TimeUnit.SECONDS);`
3. cancel(boolean mayInterruptIfRunning)
Try to cancel the task.
`future.cancel(true);`
4. isDone()
Check if task is finished.
``future.isDone();``
5. isCancelled()
Check if task was cancelled. 
```future.isCancelled();```

## Exception behavior
### Runnable
Cannot declare checked exceptions in run(). So this is invalid:
```
Runnable r = () -> {
    throw new IOException(); // not allowed directly
};
```
we must handle checked exceptions inside the runnable.

### Callable
Can throw checked exceptions:
```
Callable<String> c = () -> {
    if (true) throw new Exception("failed");
    return "ok";
};

```
When you call:
`future.get();`
the exception is wrapped in:
- ExecutionException
Example:
```java
public class CallableDemo {

    public static void main(String[] args) {
        Callable<String> c = () -> {
            if(true) throw new Exception("failed");
            return "ok";
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> future = executor.submit(c);

        try{
            String result = future.get();
        } catch (ExecutionException e){
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();

    }
}
```

## Why Callable is often better for async work
Because real async tasks often need:
- a return value 
- error propagation
That is why Callable + Future is more powerful than plain Runnable.

## Common comparison
1. Runnable. Use when:
   - no result needed
   - simple background task
2. Callable. Use when:
   - result needed 
   - checked exception handling needed
3. Future. Use when:
   - you need to monitor or retrieve async result


## One important limitation of Future
Future is useful, but get() is blocking.
So if you immediately call get() after submission:

```
Future<Integer> f = executor.submit(() -> 42);
Integer result = f.get();
```
you may lose async benefit. That is why in modern Java, interviewers may also mention:
- CompletableFuture
because it supports non-blocking composition better.
