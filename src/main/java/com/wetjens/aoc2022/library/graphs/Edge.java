package com.wetjens.aoc2022.library.graphs;

public interface Edge<N> {
    N from();

    N to();

    int cost();
}
