package com.wetjens.aoc2022;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day6Part2Test {

    @Test
    void process() {
        assertThat(Day6Part2.process("mjqjpqmgbljsphdztnvjfqwrcgsmlb")).isEqualTo(19);
        assertThat(Day6Part2.process("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(23);
        assertThat(Day6Part2.process("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(23);
        assertThat(Day6Part2.process("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(29);
        assertThat(Day6Part2.process("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(26);
    }
}