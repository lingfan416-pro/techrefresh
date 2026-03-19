# Serializable
## what is Serializable
It has no methods, that's why it is called a marker interface. It simply tells the JVM and related APIs the class is allowed to be serialized
```java
public interface Serializable{
    
}
```

## What is serialization
Serialization means converting an object into bytes. e.g.
```java
import java.io.Serializable;

public class User implements Serializable {// now User object can be serialized
    private String name;
    private int age;
    
    public User(String name, int age){
        this.name = name;
        this.age = age;
    }
}
```
Example of writing object to file

```java
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.attribute.UserPrincipal;

class User implements Serializable {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}


public class Test {
    public static void main(String[] args) {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.cer"));
        oos.writeObject(new User("Alice", 20));
    }
}

```
Deserialization means converting bytes back into an object
```java
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class TestRead {
    public static void main(String[] args) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("user.ser"));

        User user = (User) ois.readObject();
        ois.close();
    }
}
```

If an object contains references to other object, those objects object be serializable.This may fail if Address does not implement Serializable.
You may get:
NotSerializableException
```java
class Address {
    String city;
}

class User implements Serializable {
    private Address address; 
}
```

## Transient
If a field should not be serialized, mark it transient. e.g.
```java
import java.io.Serializable;

class User implements Serializable {
    private String username;
    private transient String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
```
After deserialization username is restored, password will be null for reference types, or default values for primitives
This is very important for
- passwords
- tokens
- temporary/calculated fields
- sensitive data

## SerialVersionUID
This is a version identifier for a serializable class. serialVersionUID is used to verify compatibility during deserialization.
When deserializing, JVM checks whether the class version matches
the serialized data. if not, it may throw:
- InvalidClassException

## Common interview questions
- Is Serializable a marker interface?
Yes.
- Does it contain methods?
No.
- What happens if a field is not serializable?
Serialization may fail with NotSerializableException.
- How to skip a field?
Use transient.
- Why use serialVersionUID?
To control version compatibility during deserialization.
 
## Java native serialization is old and often discouraged in modern systems because of:
- security risks 
- versioning complexity 
- performance overhead 
- tight coupling to Java class structure 
In modern applications, people often prefer:
- JSON 
- XML 
- Protobuf 
- Avro