package com.wetjens.aoc2022.library.graphs;

import java.util.stream.Stream;

public interface Graph<N, E extends Edge<N>> {
    Stream<N> nodes();

    Stream<E> edges(N node);
}
