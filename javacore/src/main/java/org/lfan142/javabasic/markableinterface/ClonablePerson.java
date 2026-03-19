package org.lfan142.javabasic.markableinterface;

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
        System.out.println(p1.address == p2.address); //true
        System.out.println(p1.address.city); //Shanghai
    }
}
