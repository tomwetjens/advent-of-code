package com.wetjens.aoc2022;

import com.wetjens.aoc2022.Day4Part1.Pair;
import com.wetjens.aoc2022.Day4Part1.Range;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day4Part1Test {

    @Test
    void parise() {
        assertThat(Pair.parse("3-92,4-93")).isEqualTo(new Pair(new Range(3, 92), new Range(4, 93)));
    }

}