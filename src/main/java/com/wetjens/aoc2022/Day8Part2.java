package com.wetjens.aoc2022;

import one.util.streamex.IntStreamEx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Day8Part2 {

    public static void main(String[] args) throws Exception {
        var grid = parseGrid(Paths.get("inputs/day8/input.txt")); // [y][x]

        printGrid(grid);

        var highestScenicScore = IntStream.range(0, grid.length)
                .flatMap(y -> IntStream.range(0, grid[y].length)
                        .map(x -> scenicScore(x, y, grid)))
                .max()
                .orElseThrow();

        System.out.println(highestScenicScore);
    }

    private static int scenicScore(int x, int y, int[][] grid) {
        var height = grid[y][x];

        var up = IntStreamEx.rangeClosed(y - 1, 0, -1).takeWhileInclusive(y2 -> grid[y2][x] < height).count();
        var down = IntStreamEx.range(y + 1, grid.length).takeWhileInclusive(y2 -> grid[y2][x] < height).count();
        var left = IntStreamEx.rangeClosed(x - 1, 0, -1).takeWhileInclusive(x2 -> grid[y][x2] < height).count();
        var right = IntStreamEx.range(x + 1, grid[y].length).takeWhileInclusive(x2 -> grid[y][x2] < height).count();

        return (int) (up * down * left * right);
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
