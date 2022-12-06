package com.wetjens.aoc2022;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day6Part1Test {

    @Test
    void process() {
        assertThat(Day6Part1.process("mjqjpqmgbljsphdztnvjfqwrcgsmlb")).isEqualTo(7);
        assertThat(Day6Part1.process("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(5);
        assertThat(Day6Part1.process("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(6);
        assertThat(Day6Part1.process("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(10);
        assertThat(Day6Part1.process("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(11);
    }
}