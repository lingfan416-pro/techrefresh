# Class Modifier
| Modifier    | Top-level class? | Meaning                         |
| ----------- | ---------------- | ------------------------------- |
| `public`    | Yes              | accessible everywhere           |
| default     | Yes              | accessible only in same package |
| `final`     | Yes              | cannot be extended              |
| `abstract`  | Yes              | cannot be instantiated          |
| `static`    | No               | only for nested classes         |
| `private`   | No               | only for nested classes         |
| `protected` | No               | only for nested classes         |


Java modifiers: class vs method vs variable
| Modifier              | Class             | Method                | Variable | Meaning                                                                              |
| --------------------- | ----------------- | --------------------- | -------- | ------------------------------------------------------------------------------------ |
| `public`              | Yes               | Yes                   | Yes      | accessible from anywhere                                                             |
| `protected`           | nested class only | Yes                   | Yes      | accessible in same package and subclasses                                            |
| default (no modifier) | Yes               | Yes                   | Yes      | package-private                                                                      |
| `private`             | nested class only | Yes                   | Yes      | accessible only within the same class                                                |
| `final`               | Yes               | Yes                   | Yes      | class cannot be extended, method cannot be overridden, variable cannot be reassigned |
| `abstract`            | Yes               | Yes                   | No       | class cannot be instantiated, method has no body                                     |
| `static`              | nested class only | Yes                   | Yes      | belongs to class instead of instance                                                 |
| `synchronized`        | No                | Yes                   | No       | method/thread access controlled by monitor                                           |
| `volatile`            | No                | No                    | Yes      | variable visibility across threads                                                   |
| `transient`           | No                | No                    | Yes      | variable not serialized                                                              |
| `native`              | No                | Yes                   | No       | method implemented in non-Java code                                                  |
| `strictfp`            | Yes               | Yes                   | No       | strict floating-point behavior                                                       |
| `sealed`              | Yes               | No                    | No       | restricts which classes can extend/implement                                         |
| `non-sealed`          | Yes               | No                    | No       | allows extension after sealed parent                                                 |
| `const`               | No                | No                    | No       | reserved keyword, not used                                                           |
| `default` keyword     | No                | interface method only | No       | default implementation in interface                                                  |

## Super short memory version
- class: public, abstract, final 
- method: static, final, abstract, synchronized, native 
- variable: final, static, volatile, transient


## Four class modifier: public, protected, default, private
| Modifier                | Same class | Same package | Subclass in different package | Other classes |
| ----------------------- | ---------: | -----------: | ----------------------------: | ------------: |
| `public`                |        Yes |          Yes |                           Yes |           Yes |
| `protected`             |        Yes |          Yes |                           Yes |            No |
| default *(no modifier)* |        Yes |          Yes |                            No |            No |
| `private`               |        Yes |           No |                            No |            No |


- public means accessible everywhere. 
- protected means accessible in the same package and also in subclasses outside the package.
- Default access, or package-private means accessible only in the same package.
- private means accessible only within the same class.