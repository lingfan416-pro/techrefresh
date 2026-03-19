package org.lfan142.chapter3;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class StackConfinement {

    public int loadTheArk(Collection<Animal> candidates) {
        SortedSet<Animal> animals;
        int numPairs = 0;
        Animal candidate = null;

        //animals confined to method, don't let them escape
        animals = new TreeSet<>((Comparator<? super Animal>) new SpeciesGenderComparator());
        animals.addAll(candidates);
        for (Animal a : animals) {
            if(candidate == null || !candidate.isPotentialMate(a)){
                candidate = a;
            } else{
                ++numPairs;
                candidate = null;
            }
        }
        return numPairs;
    }
}
