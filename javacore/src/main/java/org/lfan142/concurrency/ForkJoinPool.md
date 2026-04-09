# ForkJoinPool
ForkJoinPool is a specialized thread pool in Java for parallel divide-and-conquer tasks. It works with RecursiveTask and RecursiveAction, and its key optimization is work stealing, where idle worker threads steal tasks from busy workers. It is best suited for CPU-bound recursive computations, such as splitting a large problem into many smaller subtasks and combining the results.
ForkJoinPool is a special thread pool in Java designed for parallel divide-and-conquer tasks.
Package:
``java.util.concurrent.ForkJoinPool``

## Core Idea
It works best when a big task can be split into smaller subtasks, and those subtasks can run in parallel. Typical pattern:
- fork: split task into smaller tasks 
- process them in parallel 
- join: combine the results
That is why it is called ForkJoinPool.

## What problem it solves
Normal thread pools are good for general async tasks.
ForkJoinPool is especially good for:
- recursive parallel computation 
- CPU-bound work 
- divide-and-conquer algorithms 
- work stealing among worker threads
Examples:
- merge sort 
- sum of a large array 
- parallel tree traversal 
- parts of parallelStream()
## Main feature: work stealing
This is the most important concept. Each worker thread has its own local deque of tasks.
If one thread finishes its own tasks, it can steal tasks from another worker’s queue.
This improves:
- CPU utilization 
- load balancing 
- parallel efficiency
ForkJoinPool is optimized for fine-grained parallel tasks using a work-stealing algorithm.

## Basic classes used with it
Usually you use it with:
- RecursiveTask<V> → returns a result 
- RecursiveAction → no result
### Example with RecursiveTask

```java
package org.lfan142.concurrency.codeexample;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Long> {

    private static final int THRESHOLD  = 1000;
    private final int[] array;
    private final int start;
    private int end;

    SumTask(int[] array, int start, int end){
        this.array = array;
        this.start = start;
         this.end = end;
    }

    @Override
    protected Long compute() {
        if(end - start <= THRESHOLD){
            long sum = 0;
            for(int i = start; i < end; i++){
                sum += array[i];
            }
            return sum;
        }

        int mid = (start + end) / 2;
        SumTask left = new SumTask(array, start, mid);
        SumTask right = new SumTask(array, mid, end);

        left.fork(); //async execute left
        long rightResult = right.compute(); //compute right directly
        long leftResult = left.join(); //wait for left result

        return leftResult + rightResult;

    }
}


class SumTaskDemo {

    public static void main(String[] args) {
        int[] arr = new int[10000];
        for(int i=0; i<arr.length; i++){
            arr[i] = 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SumTask(arr, 0, arr.length));
        System.out.println(result);
    }
}

```
for this example, why `fork()` one side and `compute()` the other side?
This is common best practice.
Instead of:
```
left.fork();
right.fork();
return left.join() + right.join();
```
people often do:
```
left.fork();
long rightResult = right.compute();
long leftResult = left.join();
```
why?
- reduces task scheduling overhead
- better uses current thread
- avoids unnecessary queueing

### RecursiveAction
Use this when no result is needed. Example:
```java
package org.lfan142.concurrency.codeexample;

import java.util.concurrent.RecursiveAction;

public class PrintTask extends RecursiveAction {
    
    private final int start;
    private final int end;
    
    PrintTask(int start, int end){
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected void compute() {
        if(end - start <= 5){
            for(int i=start; i<end; i++){
                System.out.println(i);
            }
        } else {
            int mid = (start + end)/2;
            invokeAll(new PrintTask(start, mid),
                    new PrintTask(mid, end));
        }
    }
}

```

## Main methods of `ForkJoinPool`
1. `invoke(task)` submit task and wait for result
```
long result = pool.invoke(task);
```
2. `submit(task)` submit task asynchronously
3. `execute(task)` fire-and-forget style submission

## Main methods in `ForkJoinTask`

1. `fork()` schedule task asynchronously
2. `join()` wait for result
3. `invoke()` excute and wait
4. `invokeAll(...)` fork multiple tasks together.

## Common pool
Java provides a shared default pool:
```
ForkJoinPool.commonPool()
```
This is used by:
- many `CompletableFuture` async methods by default
- `parallelStream()` by default
Example:
```
int sum = list.parallelStream()
            .mapToInt(Integer::intValue)
            .sum();
```
This usually uses the common ForkJoinPool

## When ForkJoinPool is good fit
Good for: 
- CPU-bound parallel computation
- recursive task splitting
- many small independent subproblems
- algorithms that naturally divide and merge
Examples:
- sorting
- searching
- aggregation
- recursive decomposition

## When it is bad fit
No ideal for:
- long blocking I/O
- JDBC calls
- HTTP calls
- tasks that just wait most of the time
- heavy thread blocking inside worker threads

Why? Because ForkJoinPool is designed for active CPU work, not threads sitting blocked.

## Difference from ExecutorService
`ExecutorService`
- general-purpose
- good for ordinary async task execution
- queue + worker threads
- common for business tasks and service executors
`ForkJoinPool`
- Specialized for divide-and-conquer parallel tasks
- uses work stealing
- better for recursive CPU-heavy computations
So:
`ExecutorService` is general-purpose, while `ForkJoinPool` is specialized for recursive parallelism.

## Difference from normal fixed thread pool
A fixed thread pool usually has:
- one shared task queue
`ForkJoinPool` usually has:
- per-worker local queues
- work stealing
This makes it more efficient for irregular recursive task graphs.

## About parallelism level
You can configure the pool size:
```
ForkJoinPool pool = new ForkJoinPool(4);
```
This means target parallelism is around 4 worker threads.
Usually this should be related to CPU count for CPU-bound work.

## Threshold is very important
In recursive tasks, you usually define a threshold:
```
private static final int THRESHOLD = 10000;
```
why?
Because splitting forever creates too many tiny tasks.

So the pattern is:
- large task -> split
- small enough task -> compute directly
This balances:
- parallelism
- scheduling overhead
Too small threshold:
- too many tasks
- overhead high
Too large threshold:
- poor parallelism

## Exception handling
If a subtask throws an exception, it may surface on join()/invoke().
So production code should consider:
- task failure behavior
- cancellation
- cleanup

## Common interview example: `paralleStream()`
`parallelStream()` usually uses the common `ForkJoinPool'.
Example:
```
list.parallelStream()
    .map(x -> x*2)
    .forEach(System.out::println);
```
Important point:
- convenient
- but can be dangerous if overused in server-side code
- because it uses shared common pool unless otherwise controlled

This is a nice senior-level point


## Best practices
Good practices:
- use for CPU-bound divide-and-conquer
- keep task reasonably small, but not too small
- avoid blocking calls inside tasks
- tune threshold carefully
- prefer explicit pool in important applications when needed

Avoid
- blocking I/O in `ForkJoinPool`
- using it everywhere just becuase it is parallel
- overusing `parallelStream()` in backend services without understanding common pool impact 


