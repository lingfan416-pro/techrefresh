package org.lfan142.javabasic.markableinterface;

public class CloneableUsage {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person p1 = new Person("Alice");
        Person p2 = (Person) p1.clone();

        System.out.println(p1 == p2); //false;
        System.out.println(p1.name.equals(p2.name)); //true
    }
}
