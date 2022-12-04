package com.wetjens.aoc2022;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day3Part2Test {

    @Test
    void common() {
        assertThat(Day3Part2.common(Stream.of(
                "vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg"
        ))).isEqualTo('r');

        assertThat(Day3Part2.common(Stream.of(
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                "ttgJtRGJQctTZtZT",
                "CrZsJsPPZsGzwwsLwLmpwMDw"
        ))).isEqualTo('Z');
    }
}