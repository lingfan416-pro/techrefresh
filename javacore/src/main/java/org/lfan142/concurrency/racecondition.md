# Race Condition
## What is Race Condition
A race condition is a bug that happens when two or more threads access shared data at the same time,
 and the result depends on which thread runs first. The threads are "racing" with each other, If the program gives different or wrong results depending on timing, that is a race condition.
Example
```java
class Counter {
    int count = 0;
    
    void increment() {
        count ++;
    }
}
```
Suppose two threads call increment() at the same time. You may expect:
- Thread 1 adds 1
- Threads 2 adds 1
- final result = 2
But count ++ is not one single safe step, It is roughtly:
- read `count`
- add 1
- write back
So this can happen
- Thread A reads 0
- Threads B reads 0
- Thread A writes 1
- Thread B writes 1
Final result: `1` instead of `2`. This is race condition.

## Why race condition happens
Race conditions often happen with:
- counters
- shared maps/lists
- bank balance updates
- check-then-act code
- read-modify-write operations
why it happens:
- the data is shared
- multiple threads access it concurrently
- there is no proper synchronization
Example:
```

if(!map.containsKey(key)) { // two threads may both pass the check and both write.
    map.put(key, value);
}

```

Race condition = shared data + multiple threads + no proper coordination.
