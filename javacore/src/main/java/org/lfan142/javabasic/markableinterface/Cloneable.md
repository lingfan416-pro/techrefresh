# Cloneable
Cloneable in java is also a marker interface. It has no methods. Its purpose is to indicate that a class allows object cloning through Object.clone().
Cloneable itself does not define a clone() method. The actual clone() method comes from Object:
protected Object clone() throws CloneNotSupportedException
sO usually need to:
- Implement Cloneable
- override clone()
- call super.clone()
super.clone() usually performs a shallow copy. That means
- Primitive fields are copied
- object references are copied as references, not deeply duplicated.

e.g.
```java
public class Address {
    String city;

    Address(String city){
        this.city = city;
    }
}

public class ClonablePerson implements Cloneable{

    String name;
    Address address;

    ClonablePerson(String name, Address address){
        this.name = name;
        this.address = address;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
       return super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Address addr = new Address("Auckland");
        ClonablePerson p1 = new ClonablePerson("Alice", addr);
        ClonablePerson p2 = (ClonablePerson) p1.clone();
        p2.name = "Bob";

        p2.address.city = "Shanghai";
        System.out.println(p1.name == p2.name); //false
        System.out.println(p1.address == p2.address); //true. Because both p1 and p2 point to the same Address object.
        System.out.println(p1.address.city); //Shanghai
    }
}
```

```java
public interface Cloneable {
    
}
```

## What it does
If a class does not implement Cloneable, calling clone() usually throws:
- CloneNotSupportedException
If a class does implement Cloneable, then Object.clone() is allowed to make a field-by-field copy.
e.g.

```java
package org.lfan142.core.markableinterface;

public class Person implements Cloneable {

    String name;

    Person(String name) {
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

//Clone usage
public class CloneableUsage {

    public static void main(String[] args) throws CloneNotSupportedException {
        org.lfan142.javabasic.markableinterface.Person p1 = new org.lfan142.javabasic.markableinterface.Person("Alice");
        org.lfan142.javabasic.markableinterface.Person p2 = (org.lfan142.javabasic.markableinterface.Person) p1.clone();

        System.out.println(p1 == p2); //false;
        System.out.println(p1.name.equals(p2.name)); //true
    }
}

```

## Deep copy

##Why Cloneable is often considered problematic

In modern Java, Cloneable is often seen as flawed because:

- it is not very intuitive 
- clone() is protected in Object 
- it relies on tricky conventions 
- shallow vs deep copy is confusing 
- exception handling is awkward 
- constructors are bypassed

Because of that, many developers prefer:
- copy constructors 
- factory methods 
- builders 
- manual copy methods

## Better alternative example
### Copy constructor
This is often clearer than clone(). e.g.
```java
class Person {
    String name;
    
    Person(String name) {
        this.name = name;
    }
    
    Person(Person other){
        this.name = other.name;
    }
}


public static void main(String[] args) {
    Person p1 = new Person("Alice");
    Person p2 = new Person(p1);
}
```

## interview-ready answer:
Cloneable is a marker interface in Java that indicates an object supports cloning through Object.clone(). A class usually implements Cloneable and overrides clone() to call super.clone(). By default this creates a shallow copy. Because the cloning mechanism is awkward and error-prone, modern Java code often prefers copy constructors or factory methods instead.