package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wetjens.aoc2022.library.Chunked.chunked;
import static com.wetjens.aoc2022.library.Intersection.intersection;

public class Day3Part2 {

    public static void main(String[] args) throws Exception {
        try (var lines = Files.lines(Paths.get("inputs/day3/input.txt"))) {
            System.out.println(chunked(lines, 3)
                    .mapToInt(group -> priority(common(group)))
                    .sum());
        }
    }

    static char common(Stream<String> group) {
        return group.map(String::chars)
                .map(chars -> chars
                        .mapToObj(i -> (char) i)
                        .collect(Collectors.toSet()))
                .collect(intersection())
                .stream()
                .findFirst()
                .orElseThrow();
    }

    static int priority(char item) {
        return Character.isLowerCase(item) ? item - 96 : item - 38;
    }
}
