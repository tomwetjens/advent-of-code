package com.wetjens.aoc2022;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Day1Part1 {

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

        var max = elves.stream().max(Integer::compareTo);

        System.out.println(max);
    }

}
