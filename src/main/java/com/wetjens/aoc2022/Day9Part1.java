package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.IntStream;

public class Day9Part1 {

    static final int MIN_PRINT_WIDTH = 6;
    static final int MIN_PRINT_HEIGHT = 5;

    public static void main(String[] args) throws Exception {
        var head = new Coords(0, 0);
        var tail = new Coords(0, 0);

        var visited = new HashSet<Coords>();
        visited.add(tail);

//        System.out.println("== Initial State ==");
//        printState(head, tail);

        for (var line : Files.readAllLines(Paths.get("inputs/day9/input.txt"))) {
//            System.out.println("== " + line + " ==\n");

            var motion = Motion.parseLine(line);

            for (var step = 0; step < motion.steps; step++) {
                var prevHead = head;
                head = motion.step(head);

                if (Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1) {
                    tail = prevHead;
                    visited.add(tail);
                }

//                printState(head, tail);
            }
        }

        System.out.println(visited.size());
    }

    private static void printState(Coords head, Coords tail) {
        var bottomLeft = new Coords(Math.min(0, Math.min(head.x, tail.x)), Math.max(0, Math.max(head.y, tail.y)));
        var topRight = new Coords(Math.max(MIN_PRINT_WIDTH - 1, Math.max(head.x, tail.x)), Math.min(-MIN_PRINT_HEIGHT + 1, Math.min(head.y, tail.y)));

        IntStream.rangeClosed(topRight.y, bottomLeft.y)
                .forEach(y -> {
                    IntStream.rangeClosed(bottomLeft.x, topRight.x)
                            .forEach(x -> printPoint(x, y, head, tail));
                    System.out.println();
                });
        System.out.println();
    }

    private static void printPoint(int x, int y, Coords head, Coords tail) {
        System.out.print(x == head.x && y == head.y ? 'H'
                : x == tail.x && y == tail.y ? 'T'
                : x == 0 && y == 0 ? 's'
                : '.');
    }

    record Coords(int x, int y) {
    }

    enum Direction {
        R, L, U, D;
    }

    record Motion(Direction direction, int steps) {
        static Motion parseLine(String line) {
            var parts = line.split(" ");
            return new Motion(Direction.valueOf(parts[0]), Integer.parseInt(parts[1]));
        }

        Coords step(Coords head) {
            return switch (direction) {
                case L -> new Coords(head.x - 1, head.y);
                case R -> new Coords(head.x + 1, head.y);
                case U -> new Coords(head.x, head.y - 1);
                case D -> new Coords(head.x, head.y + 1);
            };
        }
    }

}
