package com.wetjens.aoc2022;

import com.wetjens.aoc2022.Day4Part2.Pair;
import com.wetjens.aoc2022.Day4Part2.Range;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day4Part2Test {

    @Test
    void overlap() {
        assertThat(new Pair(new Range(2, 4), new Range(6, 8)).overlap()).isFalse();
        assertThat(new Pair(new Range(2, 3), new Range(4, 5)).overlap()).isFalse();

        assertThat(new Pair(new Range(5, 7), new Range(7, 9)).overlap()).isTrue();
        assertThat(new Pair(new Range(2, 8), new Range(3, 7)).overlap()).isTrue();
        assertThat(new Pair(new Range(6, 6), new Range(4, 6)).overlap()).isTrue();
        assertThat(new Pair(new Range(2, 6), new Range(4, 8)).overlap()).isTrue();
    }
}
