# Java yield()

### A little background on java thread scheduling
A Java virtual machine is required to implement a preemptive, priority-based scheduler among its various threads. This means that each thread in a Java program is assigned a certain priority, a positive integer that falls within a well-defined range. This priority can be changed by the developer. The Java virtual machine never changes the priority of a thread, even if the thread has been running for a certain period of time.
The priority value is important because the contract between the Java virtual machine and the underlying operating system is that the operating system must generally choose to run the Java thread with the highest priority. That’s what we mean when we say that Java implements a priority-based scheduler. This scheduler is implemented in a preemptive fashion, meaning that when a higher-priority thread comes along, that thread interrupts (preempts) whatever lower-priority thread is running at the time. The contract with the operating system, however, is not absolute, which means that the operating system can sometimes choose to run a lower-priority thread. 

## Understanding thread priorities
Understanding the Thread priorities is next important step in learning Multi-threading and specially how yield() works.
Remember that all the threads carry normal priority when a priority is not specified.
Priorities can be specified from 1 to 10. 10 being the highest, 1 being the lowest priority and 5 being the normal priority.
Remember that the thread with highest priority will be given preference in execution. But there is no guarantee that it will be in running state the moment it starts.
Always the currently executing thread might have the higher priority when compared to the threads in the pool who are waiting for their chance.
It is the thread scheduler which decides what thread should be executed.
t.setPriority() can be used to set the priorities for the threads.
Remember that the priorities should be set before the threads start method is invoked.
You can use the constants, MIN_PRIORITY,MAX_PRIORITY and NORM_PRIORITY for setting priorities.

## yield() method
Theoretically, to ‘yield’ means to let go, to give up, to surrender. A yielding thread tells the virtual machine that it’s willing to let other threads be scheduled in its place. This indicates that it’s not doing something too critical. Note that it’s only a hint, though, and not guaranteed to have any effect at all.

yield() is defined as following in Thread.java.
```
/**
* A hint to the scheduler that the current thread is willing to yield its current use of a processor. The scheduler is free to ignore
* this hint. Yield is a heuristic attempt to improve relative progression between threads that would otherwise over-utilize a CPU.
* Its use should be combined with detailed profiling and benchmarking to ensure that it actually has the desired effect.
  */

public static native void yield();
```
Let’s list down important points from above definition:

Yield is a Static method and Native too.
Yield tells the currently executing thread to give a chance to the threads that have equal priority in the Thread Pool.
There is no guarantee that Yield will make the currently executing thread to runnable state immediately.
It can only make a thread from Running State to Runnable State, not in wait or blocked state.

## yield() method example usage
In below example program, I have created two threads named producer and consumer for no specific reason. Producer is set to minimum priority and consumer is set to maximum priority. I will run below code with/without commenting the line Thread.yield(). Without yield(), though the output changes sometimes, but usually first all consumer lines are printed and then all producer lines.

With using yield() method, both prints one line at a time and pass the chance to another thread, almost all the time.

```java
package test.core.threads;
 
public class YieldExample
{
   public static void main(String[] args)
   {
      Thread producer = new Producer();
      Thread consumer = new Consumer();
       
      producer.setPriority(Thread.MIN_PRIORITY); //Min Priority
      consumer.setPriority(Thread.MAX_PRIORITY); //Max Priority
       
      producer.start();
      consumer.start();
   }
}
 
class Producer extends Thread
{
   public void run()
   {
      for (int i = 0; i < 5; i++)
      {
         System.out.println("I am Producer : Produced Item " + i);
         Thread.yield();
      }
   }
}
 
class Consumer extends Thread
{
   public void run()
   {
      for (int i = 0; i < 5; i++)
      {
         System.out.println("I am Consumer : Consumed Item " + i);
         Thread.yield();
      }
   }
}
```
Output of above program “without” yield() method
`
I am Consumer : Consumed Item 0
 I am Consumer : Consumed Item 1
 I am Consumer : Consumed Item 2
 I am Consumer : Consumed Item 3
 I am Consumer : Consumed Item 4
 I am Producer : Produced Item 0
 I am Producer : Produced Item 1
 I am Producer : Produced Item 2
 I am Producer : Produced Item 3
 I am Producer : Produced Item 4
`
Output of above program “with” yield() method added
`
 I am Producer : Produced Item 0
 I am Consumer : Consumed Item 0
 I am Producer : Produced Item 1
 I am Consumer : Consumed Item 1
 I am Producer : Produced Item 2
 I am Consumer : Consumed Item 2
 I am Producer : Produced Item 3
 I am Consumer : Consumed Item 3
 I am Producer : Produced Item 4
 I am Consumer : Consumed Item 4
`





