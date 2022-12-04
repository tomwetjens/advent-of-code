package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day4Part2 {

    public static void main(String[] args) throws Exception {
        try (var lines = Files.lines(Paths.get("inputs/day4/input.txt"))) {
            System.out.println(lines.map(Pair::parse)
                    .filter(Pair::overlap)
                    .count());
        }
    }

    record Pair(Range a, Range b) {
        static Pair parse(String str) {
            var parts = str.split(",", 2);
            return new Pair(Range.parse(parts[0]), Range.parse(parts[1]));
        }

        boolean overlap() {
            return a.overlap(b);
        }
    }

    record Range(int from, int to) {
        static Range parse(String str) {
            var parts = str.split("-", 2);
            return new Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        boolean overlap(Range other) {
            return (from >= other.from && from <= other.to) || (to >= other.from && to <= other.to)
                    || (from <= other.from && to >= other.from) || (from <= other.to && to >= other.to);
        }
    }
}
