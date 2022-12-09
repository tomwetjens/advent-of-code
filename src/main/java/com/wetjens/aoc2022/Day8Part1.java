package com.wetjens.aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Day8Part1 {

    public static void main(String[] args) throws Exception {
        var grid = parseGrid(Paths.get("inputs/day8/input.txt")); // [y][x]

        printGrid(grid);

        var count = IntStream.range(0, grid.length)
                .map(y -> (int) IntStream.range(0, grid[y].length)
                        .filter(x -> isVisible(x, y, grid))
                        .count())
                .sum();

        System.out.println(count);
    }

    private static boolean isVisible(int x, int y, int[][] grid) {
        var height = grid[y][x];
        return IntStream.range(0, y).allMatch(y2 -> grid[y2][x] < height)
                || IntStream.range(y + 1, grid.length).allMatch(y2 -> grid[y2][x] < height)
                || IntStream.range(0, x).allMatch(x2 -> grid[y][x2] < height)
                || IntStream.range(x + 1, grid[y].length).allMatch(x2 -> grid[y][x2] < height);
    }

    private static void printGrid(int[][] grid) {
        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    private static int[][] parseGrid(Path path) throws IOException {
        try (var lines = Files.lines(path)) {
            return lines.map(line -> line.chars().map(Character::getNumericValue).toArray()).toArray(int[][]::new);
        }
    }

}
