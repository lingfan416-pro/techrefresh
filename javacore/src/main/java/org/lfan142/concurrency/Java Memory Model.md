# JMM(Java Memory Model)
JMM = specification / contract
it defines the correctness rules for concurrent access to shared memory. The JVM implementation is free to choose how to implement those rules, as long as it obeys the JMM semantics.
JMM, or Java Memory Model, defines how threads interact through memory in Java. It specifies the rules for visibility, ordering, and atomicity, and it uses the happens-before relationship to define when one thread is guaranteed to see another thread’s actions. Constructs like volatile, synchronized, final, and atomic classes rely on JMM semantics.

JMM means Java Memory Model.  It defines how threads interact through memory in Java, especially:
- visibility: when one thread’s write can be seen by another
- ordering: whether operations can be observed in a different order
- atomicity: whether an operation happens as one indivisible step
- happens-before: the key rule for safe cross-thread communication
  Why JMM exists

Without a memory model, different JVMs, CPUs, and compilers could behave differently in multithreaded code.
Because modern systems use:
- CPU caches 
- registers 
- compiler/JIT optimizations 
- instruction reordering

two threads may not automatically see memory the same way. So JMM gives the rules that say:
when is concurrent Java code correct and predictable?

## The 3 core issues
In Java concurrent programming, atomicity, visibility, and ordering are the three fundamental concepts 
that must be managed to ensure thread safety and correctness, as defined by the Java Memory Model (JMM).
### Visibility
If Thread A updates a variable, when will Thread B see it? example : `boolean ready = false;`
Thread A sets:
`ready = true`
Thread B may still keep seeing false unless there is proper syncExample:
```
value = 42;
ready = true;
```
Another thread might see:
`ready == true` but `value == 0`

if there is no proper memory ordering guarantee.hronization.

### Ordering
Even if code is written in one order, another thread may observe it in a different order.
Example: `count++; `. This is actually:
- read
- add
- write
So two threads can interfere and lose updates.


## Happens-before
This is the heart of JMM. A happens-before relationship means:
`if action A happens-before action B, then B is guaranteed to see the effects of A.`
This is how Java defines safe visibility and ordering across threads.
### Common happens-before rules
- Program order rule: Within one thread, earlier actions happen-before later actions.
- Monitor lock rule: An unlock on a monitor happens-before a later lock on the same monitor.
That is why synchronized provides visibility.
- Volatile rule: A write to a volatile variable happens-before a later read of that same variable.
- Thread start rule: A call to thread.start() happens-before actions inside the started thread.
- Thread join rule: All actions in a thread happen-before another thread successfully returns from join() on it.
- Final field safety: If an object is properly constructed, final fields have stronger visibility guarantees after safe publication.

### Main tools related to JMM
1. volatile
Provides:
- visibility 
- ordering guarantees around that variable
Does not provide:
- full atomicity for compound operations like count++
Good for:
- flags
- state publication

2. synchronized
Provides:
- mutual exclusion 
- visibility 
- ordering

Good for:
- protecting shared mutable state
- compound operations 
- maintaining invariants

3. final
final fields have special visibility guarantees if the object is properly constructed and safely published.
This is one reason immutable objects are powerful in concurrent programming.
4. Atomic classes
Examples:
- AtomicInteger
- AtomicLong
- AtomicReference
These provide atomic operations plus proper memory semantics.


JMM = rules for multithreaded memory behavior in Java

core topics = visibility, ordering, atomicity

key concept = happens-before

main tools = volatile, synchronized, final, atomics