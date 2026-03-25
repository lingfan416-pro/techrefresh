# Happens-Before
The happens-before rule is the core rule in the Java Memory Model (JMM).
Happens-before is a rule in the Java Memory Model that defines when the effects of one action are guaranteed to be visible to another action in a different thread. If action A happens-before action B, then B will see A’s memory effects, and A is ordered before B for concurrency semantics. Key happens-before rules come from program order, synchronized blocks, volatile variables, thread start, thread join, and other concurrency utilities.
It tells you:
When one thread is guaranteed to see the effects of another thread’s actions.
If A happens-before B, then:
- A’s result is visible to B 
- A is ordered before B from the perspective of memory effects
This is the foundation for reasoning about:
- visibility 
- ordering 
- thread safety

## Main happens-before rules in Java
- Program order rule: Within one thread, earlier actions happen-before later actions.
- Monitor lock rule: An unlock on a monitor happens-before a later lock on the same monitor.
  That is why synchronized provides visibility.
- Volatile rule: A write to a volatile variable happens-before a later read of that same variable.
- Thread start rule: A call to thread.start() happens-before actions inside the started thread.
- Thread join rule: All actions in a thread happen-before another thread successfully returns from join() on it.
- Final field safety: If an object is properly constructed, final fields have stronger visibility guarantees after safe publication.
- Transitivity: Happens-before is transitive.
  If:
    - A happens-before B
    - B happens-before C
  then:
      - A happens-before C

This is very important and often used in reasoning.