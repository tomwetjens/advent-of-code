
package com.wetjens.aoc2022;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

public class Day9Part2 {

    public static void main(String[] args) throws Exception {
        var rope = new Rope(10);

        System.out.println("== Initial State ==\n");

        var writer = new PrintWriter(System.out);
        rope.print(writer);

        var visited = new HashSet<Coords>();
        visited.add(rope.tail());

        for (var line : Files.readAllLines(Paths.get("inputs/day9/input.txt"))) {
            var motion = Motion.parseLine(line);

            System.out.println("== " + motion.direction() + " " + motion.steps() + " ==\n");

            for (var step = 0; step < motion.steps(); step++) {
                rope.move(motion.direction());

                visited.add(rope.tail());

                rope.print(writer);
                writer.println();
                writer.flush();
            }
        }

        System.out.println("Visited: " + visited.size());
    }

    static class Rope {

        private final Coords[] knots;

        Rope(int length) {
            knots = new Coords[length];
            Arrays.fill(knots, new Coords(0, 0));
        }

        static Rope parse(String str) {
            var lines = str.split("\n");

            var rope = new Rope(10);

            for (var i = 0; i < lines.length; i++) {
                var line = lines[i];
                var y = i - lines.length + 1;

                for (var x = 0; x < line.length(); x++) {
                    var ch = line.charAt(x);

                    if (ch != '.') {
                        var coords = new Coords(x, y);

                        if (ch == 'H') {
                            rope.knots[0] = coords;
                        } else if (Character.isDigit(ch)) {
                            var index = Character.getNumericValue(ch);
                            rope.knots[index] = coords;
                        }
                    }
                }
            }

            return rope;
        }

        Coords tail() {
            return knots[knots.length - 1];
        }

        void print(PrintWriter writer) {
            var extents = Extents.of(knots);

            for (var y = Math.min(extents.topLeft.y, -4); y <= Math.max(0, extents.bottomRight.y); y++) {
                for (var x = Math.min(0, extents.topLeft.x); x <= Math.max(extents.bottomRight.x, 5); x++)
                    printKnot(x, y, writer);
                writer.println();
            }

            writer.println();
            writer.flush();
        }

        private void printKnot(int x, int y, PrintWriter writer) {
            for (var i = 0; i < knots.length; i++) {
                if (knots[i].x == x && knots[i].y == y) {
                    writer.print(i == 0 ? "H" : i);
                    return;
                }
            }

            if (x == 0 && y == 0) {
                writer.print('s');
            } else {
                writer.print('.');
            }
        }

        void move(Direction direction) {
            knots[0] = knots[0].move(direction);

            for (var i = 1; i < knots.length; i++) {
                var dx = knots[i - 1].x - knots[i].x;
                var dy = knots[i - 1].y - knots[i].y;

                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                    if (dx == 0 || dy == 0) {
                        if (dy > 1) knots[i] = new Coords(knots[i].x, knots[i].y + 1);
                        else if (dy < -1) knots[i] = new Coords(knots[i].x, knots[i].y - 1);
                        else if (dx > 1) knots[i] = new Coords(knots[i].x + 1, knots[i].y);
                        else knots[i] = new Coords(knots[i].x - 1, knots[i].y);
                    } else {
                        if (dx > 0 && dy > 0) knots[i] = new Coords(knots[i].x + 1, knots[i].y + 1);
                        else if (dx > 0) knots[i] = new Coords(knots[i].x + 1, knots[i].y - 1);
                        else if (dy > 0) knots[i] = new Coords(knots[i].x - 1, knots[i].y + 1);
                        else knots[i] = new Coords(knots[i].x - 1, knots[i].y - 1);
                    }
                }
            }
        }

        @Override
        public String toString() {
            var writer = new StringWriter();
            print(new PrintWriter(writer));
            return writer.toString();
        }

        @Override
        public boolean equals(Object o) {
            return Arrays.equals(knots, ((Rope) o).knots);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(knots);
        }
    }

    record Coords(int x, int y) {
        Coords move(Direction direction) {
            return switch (direction) {
                case L -> new Coords(x - 1, y);
                case R -> new Coords(x + 1, y);
                case U -> new Coords(x, y - 1);
                case D -> new Coords(x, y + 1);
            };
        }
    }

    record Extents(Coords topLeft, Coords bottomRight) {
        static Extents of(Coords[] points) {
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

            for (var point : points) {
                minX = Math.min(minX, point.x);
                maxX = Math.max(maxX, point.x);
                minY = Math.min(minY, point.y);
                maxY = Math.max(maxY, point.y);
            }

            return new Extents(new Coords(minX, minY), new Coords(maxX, maxY));
        }
    }

    enum Direction {
        R, L, U, D;
    }

    record Motion(Direction direction, int steps) {
        static Motion parseLine(String line) {
            var parts = line.split(" ");
            return new Motion(Direction.valueOf(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}
