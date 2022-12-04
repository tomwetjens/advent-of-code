package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day4Part1 {

    public static void main(String[] args) throws Exception {
        try (var lines = Files.lines(Paths.get("inputs/day4/input.txt"))) {
            System.out.println(lines.map(Pair::parse)
                    .filter(Pair::oneContainsOther)
                    .count());
        }
    }

    record Pair(Range a, Range b) {
        static Pair parse(String str) {
            var parts = str.split(",", 2);
            return new Pair(Range.parse(parts[0]), Range.parse(parts[1]));
        }

        boolean oneContainsOther() {
            return a.contains(b) || b.contains(a);
        }
    }

    record Range(int from, int to) {
        static Range parse(String str) {
            var parts = str.split("-", 2);
            return new Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        boolean contains(Range other) {
            return from <= other.from && to >= other.to;
        }
    }
}
