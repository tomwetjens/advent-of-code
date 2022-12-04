package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day3Part1 {

    public static void main(String[] args) throws Exception {
        try (var lines = Files.lines(Paths.get("inputs/day3/input.txt"))) {
            System.out.println(lines
                    .mapToInt(line -> priority(inBothCompartments(line)))
                    .sum());
        }
    }

    static char inBothCompartments(String rucksack) {
        var mid = rucksack.length() / 2;
        for (var i = 0; i < mid; i++) {
            var item = rucksack.charAt(i);
            if (rucksack.indexOf(item, mid) >= 0) {
                return item;
            }
        }
        return 0;
    }

    static int priority(char item) {
        return Character.isLowerCase(item) ? item - 96 : item - 38;
    }
}
