package org.lfan142.chapter3;


import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class SpeciesGenderComparator implements Comparator<Animal> {


    @Override
    public int compare(Animal o1, Animal o2) {
        return 0;
    }

    @NotNull
    @Override
    public Comparator reversed() {
        return Comparator.super.reversed();
    }

    @NotNull
    @Override
    public Comparator thenComparing(@NotNull Comparator other) {
        return Comparator.super.thenComparing(other);
    }

    @NotNull
    @Override
    public Comparator thenComparingInt(@NotNull ToIntFunction keyExtractor) {
        return Comparator.super.thenComparingInt(keyExtractor);
    }

    @NotNull
    @Override
    public Comparator thenComparingLong(@NotNull ToLongFunction keyExtractor) {
        return Comparator.super.thenComparingLong(keyExtractor);
    }

    @NotNull
    @Override
    public Comparator thenComparingDouble(@NotNull ToDoubleFunction keyExtractor) {
        return Comparator.super.thenComparingDouble(keyExtractor);
    }

    @NotNull
    @Override
    public Comparator thenComparing(@NotNull Function keyExtractor) {
        return Comparator.super.thenComparing(keyExtractor);
    }

    @NotNull
    @Override
    public Comparator thenComparing(@NotNull Function keyExtractor, @NotNull Comparator keyComparator) {
        return Comparator.super.thenComparing(keyExtractor, keyComparator);
    }
}
