package com.wetjens.aoc2022;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day2Part1Test {

    @Test
    void example() {
        assertThat(Day2Part1.score("A Y")).isEqualTo(8);
        assertThat(Day2Part1.score("B X")).isEqualTo(1);
        assertThat(Day2Part1.score("C Z")).isEqualTo(6);
    }
}