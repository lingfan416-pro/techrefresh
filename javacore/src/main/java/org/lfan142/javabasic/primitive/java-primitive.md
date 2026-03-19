# Java Primitive Type
Java has 8 primitive type,
| Type      |                  Size | Default value | Example                |
| --------- | --------------------: | ------------- | ---------------------- |
| `byte`    |                 8-bit/1 byte | `0`           | `byte b = 10;`         |
| `short`   |                16-bit/2 byte | `0`           | `short s = 100;`       |
| `int`     |                32-bit/4 byte | `0`           | `int i = 1000;`        |
| `long`    |                64-bit/8 byte | `0L`          | `long l = 10000L;`     |
| `float`   |                32-bit/4 byte | `0.0f`        | `float f = 3.14f;`     |
| `double`  |                64-bit/8 byte | `0.0d`        | `double d = 3.14;`     |
| `char`    |                16-bit/2 byte | `'\u0000'`    | `char c = 'A';`        |
| `boolean` | JVM-dependent storage | `false`       | `boolean flag = true;` |

## Group
### Integer types:
- byte
- short
- int
- long

### Floating-point types
- float
- double

### character type
- char

### logical type
- boolean

## Primitive vs Reference type
Primitive type stores the actual value directly. reference type stores a reference to an object.

## Wrapper classes
each primitive has a corresponding wrapper class: 
- byte -> Byte
- short -> Short
- int -> Integer
- long -> Long
- float -> Float
- double -> Double
- char -> Character
- boolean -> Boolean

## primitive type storage
Primitive types are not objects. They store actual values directly, and in JVM memory their location depends on usage: local primitive variables are typically stored in a stack frame’s local variable table, while primitive fields are stored inside the object or class storage associated with that field.
- local primitive variable → stack frame 
- primitive instance field → heap object 
- primitive static field → class-level storage 
- primitive array elements → heap array

In Java, a reference type does not have one fixed language-level size.
Its size depends on the JVM implementation, 32-bit vs 64-bit, and whether compressed oops is enabled.

Practical answer

For HotSpot JVM, the common cases are:

32-bit JVM → reference is usually 4 bytes

64-bit JVM with compressed oops enabled → reference is usually 4 bytes

64-bit JVM without compressed oops → reference is usually 8 bytes

So in many modern Java environments, a reference is often 4 bytes, not always 8.