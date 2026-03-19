# JAVA Reflection
Java Reflection is a mechanism that lets a program inspect and operate on classes, methods, fields, constructors, and annotations at runtime, even if their exact types 
were not known at compile time.

## Common things reflection can do
- get class information
- list methods / fields / constructors
- create objects dynamically
- access private fields or methods
- inspect annotations
- support frameworks and libraries

Basic example: get `Class` object
```java
Class<?> clazz = String.class;

Class<?> clazz = Class.forName("java.lang.String");

// System.out.println(clazz.getName()); //get class Name
```
`Class` is the entry point of reflection. 
- Create object with reflection

```java
import java.lang.reflect.InvocationTargetException;

class User {
  public User() {
    System.out.println("User created");
  }


}


public class ReflectionTest {

  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class clazz = Class.forName("org.lfan142.javabasic.reflection.User");
    Object obj = clazz.getDeclaredConstructor().newInstance();
  }
}

```

- Invoke method with reflection
```java
public class User {

    public User(){
        System.out.println("User Created");
    }


    public void sayHello(String name){
        System.out.println("Hello " + name);
    }
}



```
caller side

```java
import java.lang.reflect.InvocationTargetException;

public class ReflectionTest {

  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Class clazz = Class.forName("org.lfan142.javabasic.reflection.User");
    User obj = (User) clazz.getDeclaredConstructor().newInstance();

    obj.sayHello("Tracey");
  }
}

```

Output
`
User Created
Hello Tracey
`
- Access field with reflection
- Access private members
this is powerful, but should be used carefully.
```java
public class User {

    private String name = "Bob"; //has to be public

    public User(){
        System.out.println("User Created");
    }


    public void sayHello(String name){
        System.out.println("Hello " + name);
    }
}

```

```java
import java.lang.reflect.InvocationTargetException;

public class ReflectionTest {

  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    Class clazz = Class.forName("org.lfan142.javabasic.reflection.User");
    Object obj = clazz.getDeclaredConstructor().newInstance();

    java.lang.reflect.Field field = clazz.getDeclaredField("name");
    field.setAccessible(true);
    System.out.println(field.getName() + " : " + field.get(obj));
  }
}

```

- Common reflective APIs 
main package
`java.lang.reflect`
important classes:
  - Class
  - Method
  - Field
  - Constructor
  - Modifier
  - Proxy

- getMethod, getField, getConstructor vs getDeclaredMethod, getDeclaredField, getDeclaredConstructor
getMethod, getField, getConstructor
- finds only public methods, fields, constructors
- includes inherited methods, fields
getDeclaredMethod, getDeclaredField, getDeclaredConstructor
- finds methods, fields, constructors declared in the current class
- including private, protected, default
- does not automatically search inherited ones the same way.

## Why reflection is used
Reflection is heavily used in frameworks, example:
- Spring
- Hibernate /JPA
- Jackson
- JUnit
- dependency injection
- ORM mapping
- annotation processing at runtime
- plugin systems
Typical framework use cases:
- instantiate bean by a class name
- inject dependencies into fields
- scan annotations like @Service, @Autowired, @Entity
- call methods dynamically

## Reflection and annotations
This is one reason reflection is so important in framework.
example:
```java
@Retention(RetentionPolicy.RUNTIME)
@interface myAnno {
    String value();
}
```

```java
@MyAnno("demo")
class TestClass {
    
}

```
Then at runtime

```java
import java.lang.reflect.InvocationTargetException;

public class ReflectionTest {

  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    Class clazz = Class.forName("org.lfan142.javabasic.reflection.User");
    Object obj = clazz.getDeclaredConstructor().newInstance();

    java.lang.reflect.Field field = clazz.getDeclaredField("name");
    field.setAccessible(true);
    System.out.println(field.getName() + " : " + field.get(obj));
  }
}

```
## Advantages
- Flexibility: we can operate on unknown classes dynamically.
- Framework support: Enable DI, ORM, serialization libraries, testing frameworks
- Metadata inspection: useful for annotations and generic runtime behaviors.
## Disadvantage
- Slower than direct calls, reflection usually has more overhead than normal method calls
- Less type-safe, may errors move from compile to runtime
- Harder to read and maintain: reflective code is more complex and less explicit
- Can break encapsulation, especially when access private members

Reflection is slower because method and field access happens dynamically at runtime, 
with additional lookup and access-check overhead.

## why reflection is slower
### Runtime loopup cost
- With normal code, the target method is already known in the compiled bytecode. The JVM can directly
 resolve that call path very efficiently. 
- With reflection, the JVM must:
  - search method metadata
  - match the method name
  - match parameters types
  - return a Method object.
This lookup is extra overhead. Even if you cache the Method object, reflective invocation itself is still slower than a normal direct call
### Access checking
when use reflection, java usually check whether the access is allowed.
```java
Method m = clazz.getDeclaredMethod("sayHello");
m.invoke(obj);
```
The runtime may check:
- is the method public/protected/default/private
- is the caller allowed to access
- has accessibility been overridden by `setAccessible(true)` 

`m.setAccessible(true);` may reduce some repeated access-check cost, but reflection is still generally slower than a direct call.

### Method invocation goes through reflection machinery
A normal call:`user.sayHello();` is a direct language-level method invocation.
A reflection call `m.invoke(user);`does not directly call the target method in the same straightforward way. It goes through reflection APIs and
internal JVM handling. so runtime has to:
- validate the target object
- validate arguments
- package arguments into an Object[] in many cases
- convert types when needed
- dispatch through reflective infrastructure
- wrap exceptions in some cases.
The extra path makes it slower

### Argument boxing / array creation overhead
This is a big practical reason. Reflection often handles method arguments as objects. So there may be extra cost from:
- creating an argument array
- boxing primitive values like int to Integer
- runtime checking whether argument types match.
Example:
- normal call: `foo(1)`
- reflective call may internally deal with something more like `Object[]{Integer.valueOf(1)}`.
that creates more overhead and possibly more garbage collection pressure

### Less JIT(java-in-time compiler) optimization
This is one of the most important reasons. For the direct calls, the JIT can do many optimization, such as:
- inlining
- devirtualization in some cases
- escape analysis
- constant folding around the call path
- branch prediction improvements.
Example:
`user.sayHello()` if the JVM sees this is hot code, it may inline the method body directly. But for reflection:
`m.invoke(user)` the target is more dynamic and opaque to the optimizer. So the JIT has fewer opportunities to optimize it as aggressively
That means:
- more call overhead remains
- fewer low-level optimizations happen 
- performance gap becomes noticeable in tight loops

### Exception handling overhead
Reflection can wrap exceptions. For example, if the invoked method throws an exception, reflection may wrap it inside:
`InvocationTargetException`
So there is extra runtime handling around failures

### 7. Metadata objects and indirection
Reflection works with metadata objects such as:
- Class
- Method
- Field
- Constructor
These are abstraction layers over actual runtime structures. A normal call does not need the same level of
metadata-driven indirection each time. So reflection introduce another layer between code and the actual execution target.

## Interview-ready definition

Reflection in Java is a runtime mechanism that allows a program to inspect and manipulate classes, methods, fields, constructors, and annotations dynamically. It is widely used in frameworks such as Spring and Hibernate for dependency injection, object creation, and metadata processing.

## Very short version
- Reflection = inspect and operate on class structure at runtime 
- Core class = Class 
- Can access Method, Field, Constructor 
- Used by frameworks 
- Powerful but slower and less safe than direct code


## Is reflection always “too slow”?
Reflection is slower, but whether that matters depends on how often it is used and where it sits in the execution path.
In many business applications, reflection is used:
- during startup 
- during dependency injection 
- during framework scanning 
- during object mapping 
- In those cases, the overhead may be acceptable.

It becomes a real issue when reflection is used:
- in hot loops 
- in very high-frequency code paths 
- in latency-sensitive systems

## Why frameworks still use reflection
Because reflection provides flexibility
- dynamic object creation
- dependency injection
- annotation scanning
- method dispatch
- ORM mapping
Frameworks often accept some overhead in exchange for:
- extensibility
- configurability
- reduced boilerplate
Also, modern frameworks often try to reduce reflection cost by:
- caching metadata
- generating bytecode
- using method handles
- using code generation libraries