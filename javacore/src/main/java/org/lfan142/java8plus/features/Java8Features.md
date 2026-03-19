

# new features
Java 8 new features
| Feature               | First introduced | Final/usable since |
| --------------------- | ---------------: | -----------------: |
| Lambda expressions    |        **JDK 8** |          **JDK 8** |
| Stream API            |        **JDK 8** |          **JDK 8** |
| Functional interfaces |        **JDK 8** |          **JDK 8** |
| `Optional`            |        **JDK 8** |          **JDK 8** |


Other new features:
| Feature                           |     First introduced | Final/usable since |
| --------------------------------- | -------------------: | -----------------: |
| Records                           | **JDK 14** (preview) |         **JDK 16** |
| Sealed classes                    | **JDK 15** (preview) |         **JDK 17** |
| Pattern matching for `instanceof` | **JDK 14** (preview) |         **JDK 16** |
| Pattern matching for `switch`     | **JDK 17** (preview) |         **JDK 21** |
| Virtual threads                   | **JDK 19** (preview) |         **JDK 21** |

# Java 8 new features
## Lambda expression

A lambda is a concise way to write an anonymous function. It is mainly used to pass behavior as data. example:
`Runnable r = () -> System.out.printly("hello");`
Syntax
`
(parameters) -> expression;
(parameters) -> {statement}
`
### Why Lambda is useful
- less boilerplate
- easier collection processing
- works well with functional interfaces
- makes code more expressive

## Functional interface
A functional interface is an interface with exactly one abstract method. It is the target type for lambda expressions. 
A functional interface can still have 
- default methods
- static methods
It must have only one abstract method. example
```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}

public static void main(String[] args){
    Calculator c = (a, b) -> a + b;
    
   //  Calculator c1 = Integer ::sum;
    System.out.println(c.add(2, 3));
}

```
### Common build-in functional interfaces
From java.util.function:
- Function<T, R>  -> input to output
input type is T, output type is R.
```java
import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args){
        Function<String, Integer> f = name -> name.length;
        System.out.println(f.apply(Alice));
    }
}
```
- Consumer<T> -> takes input, return nothing
Input type T to no return
`Consumer<String> c = name -> System.out.println(name)`
- Supplier<T> -> returns value, no input type.
No input type to T output type
`Supplier<string> s = () -> "Hello"; `
- Predicate<T> -> returns boolean
Input type T to output type boolean
`Predicate<String> p= name -> name.isEmpty();`

### Stream API
A stream is a sequence of elements supporting functional-style operations. It is used to process collections more declaratively. 
- a stream does not store data
- it operates on data from a source like a list
- it supports chained operations.
example:
```java
public class StreamTest {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Tom", "Alice", "Bob", "Anna");
        List<String> result = names.stream().filter((name) -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
        System.out.println(result);
    }
}
```
### Common Stream Operations
Intermediate operations, return another stream.
- filter
- map
- sorted
- distinct
- limit
Terminal operations, produce result or side effect
- collect
- forEach
- count
- reduce
- findFirst


## Optional
Optional<T> is a container object that may or may not contain a non-null value. It is used to reduce direct null handling.

Example:
```
Optional<String> name = Optional.of("Tom");
System.out.println(name.get());

# empty example
Optional<String> empty = Optional.empty();

# Safe use
Optional<String> name = Optional.ofNullable(getName());
name.ifPresent(System.out::println);
```
### Common methods
- of(value) : value must not be null
- ofNullable(value) -> null allowed
- empty() -> empty optional
- isPresent() -> check value
- ifPresent(...) -> consume if exists
- orElse(defaultValue) -> fallback
- orElseGet(...) -> lazy fallback
- orElseThrow(...) -> throw if absent
- map(...) -> transform value

### why Optional is useful
- makes absence explicit
- reduces some null-check boilerplate
- improves API clarity

### Important interview note
Optional is usually best for 
- return value. 
Usually not ideal for 
- entity fields, 
- method parameters
- Serialization-heavy domain models


# other new features:
| Feature                           |     First introduced | Final/usable since |
| --------------------------------- | -------------------: | -----------------: |
| Records                           | **JDK 14** (preview) |         **JDK 16** |
| Sealed classes                    | **JDK 15** (preview) |         **JDK 17** |
| Pattern matching for `instanceof` | **JDK 14** (preview) |         **JDK 16** |
| Pattern matching for `switch`     | **JDK 17** (preview) |         **JDK 21** |
| Virtual threads                   | **JDK 19** (preview) |         **JDK 21** |


## 1. Records
### What is record?
A record is a compact way to define an immutable data carrier. Records are best for immutable data carriers. They reduce boilerplate and make value semantics explicit. I would use them for DTOs, commands, events, and API models, but I would avoid them for heavily stateful domain entities or framework-managed proxy objects where mutability or special construction rules matter.
`
public record User(String id, String name){}
`
the compiler automatically generates:
- constructor
- getters like id() and name()
- equals()
- hashCode()
- toString()
### Why java introduced it
Before records, we wrote a lot of boilerplate for simple DTO/value objects. example
```java
public final class User {
    private final String id;
    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

   
}
```

Records are good for:
- DTOs 
- API request/response objects 
- event payloads 
- query results 
- immutable value objects

They improve:
- readability 
- immutability by default 
- correctness of equality 
- less boilerplate

### Important limitations
A record is not just “shorter POJO syntax.”  It has a strong semantic meaning:
- transparent carrier for data 
- fields are final 
- not suited for mutable entities 
- not ideal when framework/proxy/lifecycle needs traditional mutable beans
 
I would use records for value-oriented objects, but not blindly for JPA entities or objects that require mutable lifecycle 
and framework proxies.

## Sealed classes
### what it is
Sealed classes let us control which classes are allowed to extend or implement a type.
Sealed classes are useful when the type hierarchy is part of the domain model and should be closed. They make illegal extensions impossible at the type-system level and work very well with pattern matching, because the compiler can reason about exhaustiveness.
`
public sealed interface Shape permits Circle, Retangle, Triangle{}
`

```java
public final class Circle implements Shape {}
public final class Rectangle implements Shape {}
public final class Triangle implements Shape {}
```
### Why java introduced it
Traditional inheritance is open-ended:
- any class can implement an interface
- any class can extend a non-final class
That can be too loose when the domain is closed. For example:
- payment states
- expression tree nodes
- result types
- compiler AST
- domain-specific finite hierarchies
  This helps:
- domain modeling 
- exhaustiveness in pattern matching 
- safer APIs 
- better readability of allowed subtype space

It is especially useful when the business model is closed.
Examples:
- Success, Failure 
- Pending, Approved, Rejected 
- AST nodes like Literal, BinaryExpr, UnaryExpr

A permitted subclass must declare one of:
- final 
- sealed 
- non-sealed
  
```
public sealed interface Result permits Success, Failure {}

public record Success(String value) implements Result {}
public record Failure(String error) implements Result {}
```
Use them when:
- the hierarchy is intentionally closed 
- the business domain has finite known variants

Do not use them when:
- extension by third parties is part of the design 
- plugin model is needed 
- API should remain open

### virtual threads
Virtual threads are lightweight threads managed by the JVM, designed to let you write thread-per-task code without the high cost of platform threads.
Virtual threads let us keep the readability of blocking code while dramatically lowering the cost of concurrency for IO-bound workloads. 
I see them as a simplification tool as much as a scalability feature. 
But I would still analyze bottlenecks carefully, 
because virtual threads do not eliminate limits in databases, external APIs, CPU, or synchronization-heavy code.
example:

```
try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        System.out.println("running in virtual thread");
    });
}
```

Virtual threads are especially good for:
- IO-bound workloads 
- request handling 
- database calls 
- HTTP client calls 
- service orchestration 
- simplifying asynchronous code
They are less about making CPU work faster, and more about making concurrency cheaper.
- Virtual threads improve scalability for blocking IO-heavy workloads, not raw compute throughput.

### Why they matter architecturally

They can reduce the need for complex async code in many services.
Before:
- CompletableFuture 
- reactive chains 
- callback-heavy logic

Now, in some cases:
- plain sequential style becomes scalable enough

That improves:
- readability 
- debuggability 
- observability 
- maintenance