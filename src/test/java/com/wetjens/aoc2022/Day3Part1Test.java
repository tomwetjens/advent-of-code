package com.wetjens.aoc2022;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day3Part1Test {

    @Test
    void inBothCompartments() {
        assertThat(Day3Part1.inBothCompartments("vJrwpWtwJgWrhcsFMMfFFhFp")).isEqualTo('p');
        assertThat(Day3Part1.inBothCompartments("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL")).isEqualTo('L');
        assertThat(Day3Part1.inBothCompartments("PmmdzqPrVvPwwTWBwg")).isEqualTo('P');
        assertThat(Day3Part1.inBothCompartments("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn")).isEqualTo('v');
        assertThat(Day3Part1.inBothCompartments("ttgJtRGJQctTZtZT")).isEqualTo('t');
        assertThat(Day3Part1.inBothCompartments("CrZsJsPPZsGzwwsLwLmpwMDw")).isEqualTo('s');
    }

    @Test
    void priority() {
        assertThat(Day3Part1.priority('p')).isEqualTo(16);
        assertThat(Day3Part1.priority('L')).isEqualTo(38);
        assertThat(Day3Part1.priority('P')).isEqualTo(42);
        assertThat(Day3Part1.priority('v')).isEqualTo(22);
        assertThat(Day3Part1.priority('t')).isEqualTo(20);
        assertThat(Day3Part1.priority('s')).isEqualTo(19);
    }
}