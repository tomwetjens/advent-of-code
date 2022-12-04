package com.wetjens.aoc2022.library;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

public class Intersection<T> {

    private Set<T> result;

    public static <T, C extends Collection<T>> Collector<C, Intersection<T>, Set<T>> intersection() {
        return Collector.of(Intersection::new, Intersection::accumulate, Intersection::combine, Intersection::finish);
    }

    private Set<T> finish() {
        return Collections.unmodifiableSet(result);
    }

    private Intersection<T> combine(Intersection<T> other) {
        result.retainAll(other.result);
        return this;
    }

    private void accumulate(Collection<T> col) {
        if (result  == null) {
            result = new HashSet<>(col);
        } else {
            result.retainAll(col);
        }
    }

}
