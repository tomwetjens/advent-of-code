package com.wetjens.aoc2022;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class Day1Part2 {

    public static void main(String[] args) {
        var elves = new ArrayList<Integer>();

        try {
            var elf = 0;
            for (var line : Files.readAllLines(Paths.get("inputs/day1/input.txt"))) {
                if (!line.isEmpty()) {
                    var calories = Integer.parseInt(line);
                    elf += calories;
                } else {
                    elves.add(elf);
                    elf = 0;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        var top3 = elves.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(top3);
    }

}
