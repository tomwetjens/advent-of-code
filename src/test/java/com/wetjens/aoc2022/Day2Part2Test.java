package com.wetjens.aoc2022;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Part2Test {

    @Test
    void example() {
        assertThat(Day2Part2.score("A Y")).isEqualTo(4);
        assertThat(Day2Part2.score("B X")).isEqualTo(1);
        assertThat(Day2Part2.score("C Z")).isEqualTo(7);
    }
}