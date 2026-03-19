# Method handler
it was introduced in Java 7 and enhanced in the following versions, the java.lang.invoke.MethodHandles.

## What Are Method Handles
A method handle is a typed, directly executable reference to an underlying method, constructor, field, or similar lower level
operation. with optional transformations of arguments or return types. method handles are a low-level mechanism for finding, adapting and invoking methods.

Method handles are immutable and have no visible state.

For creating and using a MethodHandle, 4 steps are required.
- Creating the lookup
- Creating the method type
- Finding the method handle
- Invoking the method handle.

## The main difference between reflection and methodhandle
| Aspect           | Reflection                                           | MethodHandle                                                         |
| ---------------- | ---------------------------------------------------- | -------------------------------------------------------------------- |
| Main package     | `java.lang.reflect`                                  | `java.lang.invoke`                                                   |
| Main purpose     | inspect metadata and invoke                          | dynamic invocation                                                   |
| Style            | metadata-driven                                      | callable handle                                                      |
| Performance      | generally slower                                     | generally faster                                                     |
| JIT friendliness | weaker                                               | stronger                                                             |
| Type checking    | looser, more runtime checks                          | stronger method-type model                                           |
| API level        | older, simpler to start with                         | newer, lower-level                                                   |
| Common use       | frameworks, annotation scanning, metadata inspection | JVM internals, lambdas, dynamic languages, high-performance dispatch |


Reflection and MethodHandle both support dynamic invocation, but they are different APIs. Reflection is mainly for runtime inspection and metadata-based access to classes, 
methods, and fields. MethodHandle is a lower-level invocation mechanism from java.lang.invoke, designed for more direct and JVM-optimized dynamic calls. Reflection is easier and commonly used in frameworks for introspection, 
while MethodHandle is generally faster and used in advanced runtime features such as lambdas, invokedynamic, and dynamic language support.

- Reflection = inspect + invoke 
- MethodHandle = invoke efficiently 
- Reflection is easier 
- MethodHandle is usually faster 
- Reflection is metadata-oriented 
- MethodHandle is execution-oriented

Reflection asks “what is this member?”
MethodHandle says “give me something callable.”