package com.wetjens.aoc2022;

import com.wetjens.aoc2022.Day19Part1.Blueprint;
import com.wetjens.aoc2022.Day19Part1.State;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day19Part1Test {

    @Test
    void example() {
        var blueprint = new Blueprint(4, 2, 3, 14, 2, 7, 4);

//        var state = new State(21, 4,25,7,2,1,4,2,1);

//        assertThat(Day19Part1.maxCrackedGeodes(blueprint, new State(24, 5, 37, 6, 7, 1, 4, 2, 2))).isEqualTo(9);
//        assertThat(Day19Part1.maxCrackedGeodes(blueprint, new State(23, 4, 33, 4, 5, 1, 4, 2, 2))).isEqualTo(9);
//        assertThat(Day19Part1.maxCrackedGeodes(blueprint, new State(22, 3, 29, 2, 3, 1, 4, 2, 2))).isEqualTo(9);

        assertThat(Day19Part1.maxCrackedGeodes(blueprint, State.INITIAL)).isEqualTo(9);
    }
}