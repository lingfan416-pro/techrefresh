# Concurrency

## Process vs Thread
- Process is an independent running program with its own memory space and system resource. Examples:
  - a Chrome browser instance
  - a Java application running as one JVM process
  - a database server process
- Thread is a smaller execution unit inside a process. A thread is a smaller execution unit inside a process.
A process can have one thread, or multiple threads. Threads within the same process share most of the process resources.

Simple analogy:
- Process: a house
- Thread: people working inside the house.
Different houses are isolated from each other. People inside the same house can share  furniture and rooms.

### Main differences
| Aspect              | Process                                                   | Thread                                      |
| ------------------- | --------------------------------------------------------- | ------------------------------------------- |
| Definition          | independent running program                               | execution unit inside a process             |
| Memory              | separate address space                                    | shares process memory                       |
| Resource ownership  | owns resources                                            | uses process resources                      |
| Isolation           | strong                                                    | weak                                        |
| Communication       | slower, more complex                                      | faster, easier                              |
| Creation cost       | higher                                                    | lower                                       |
| Context switch cost | higher                                                    | lower                                       |
| Failure impact      | one process crash usually does not directly crash another | one bad thread can affect the whole process |


### Memory difference
- Process: each process has its own memory space. so one process usually cannot directly access another process's memory.
That gives:
  - better isolation
  - better fault separation
  - stronger security boundaries

- Thread: Threads in the same process share memory,  including:
  - heap
  - static data
  - open resources in many cases
  But each thread has its own:
  - stack
  - program counter
  - local execution context
This is why threads are lightweight, but also why thread safety matters.
  
### Communication difference
Between process communication is harder and slower. Typical IPC methods:
- Socket
- pipe
- shared memory
- message queue
- file
- REST/gRPC between services
Between threads communication is easier because they share memory. They can communicate through:
- shared objects
- shared variables
- concurrent collections
- queues
But you must handle:
- race conditions
- visibility
- synchronization

### Performance difference
Process: creating a process is usually more expensive because the OS needs to create:
- separate memory space
- process metadata
- resource context

Thread: creating a thread is cheaper than creating a process. 
Switching between threads is also usually cheaper than switching between processes. That is why threads are commonly used for:
- concurrency inside one application
- request handling
- background tasks

### Isolation and failure
Process: processes are more isolated. If one process crashes, other processes often continue running. Example:
- one app crashes, OS and other apps still run.
Thread: threads are less isolated. If one critical thread crashes badly or corrupts shared state, it may 
 affect the whole process. In java:
- if one thread throws an uncaught exception, that thread dies
- but shared corrupted state can still impact the JVM process.

In java context
- Process in Java: A Java application usually runs inside one JVM process.
- Thread in Java: inside that JVM, you may have many threads, such as:
  - main thread
  - GC thread
  - worker threads
  - HTTP request threads
  - scheduler threads
So 
  - JVM = Process
  - Java Thread = thread inside tat JVM process

### When to use process vs thread
#### Use multiple processes when you want:
- isolation
  - separate deployment/runtime boundary
  - fault separation
  - security boundary
  - independent scaling
Examples:
  - microservices
  - browser tabs/processes
  - database separate from application
#### Use multiple threads when you want
  - concurrency inside one application
  - shared memory access
  - lightweight parallel task execution
  - lower overhead than processes
Example:
  - web server handling many requests
  - producer-consumer inside one app
  - background scheduled jobs.

A process is an independent running program with its own memory space and OS resources,
while a thread is lightweight execution unit within a process. Threads in the same process share memory and resources, s
so they are cheaper to create and communicate faster, but they require synchronization
 and have weaker isolation than processes.

Summarize:
  - Process = isolated running program
  - Thread = execution path inside a process
  - processes are heavier but safer
  - threads are lighter but need synchronization