package org.lfan142;

import java.util.HashSet;
import java.util.Set;

public class PersonSet<T> {

    private final Set<T> mySet = new HashSet();

    public synchronized void addT(T person){
        mySet.add(person);
    }

    public synchronized boolean containsPerson(T person){
        return mySet.contains(person);
    }
}
