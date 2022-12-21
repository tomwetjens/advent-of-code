package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day17Part1 {

    static final List<RockType> ROCK_TYPES = List.of(
            // ####
            new RockType(4, Set.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0))),

            // .#.
            // ###
            // .#.
            new RockType(3, Set.of(new Point(0, 1), new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(2, 1))),

            // ..#
            // ..#
            // ###
            new RockType(3, Set.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2))),

            // #
            // #
            // #
            // #
            new RockType(1, Set.of(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3))),

            // ##
            // ##
            new RockType(2, Set.of(new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)))
    );

    public static void main(String[] args) throws Exception {
        var jets = Files.readAllLines(Paths.get("inputs/day17/input.txt")).get(0).chars()
                .map(ch -> ch == '<' ? -1 : 1)
                .toArray();

        var jetIndex = 0;
        var rockTypeIndex = 0;

        var grid = new Grid(7, Integer.MAX_VALUE);

        for (var n = 1; n <= 2022; n++) {
            var rockType = ROCK_TYPES.get(rockTypeIndex);

            var rock = new Rock(rockType, (n == 1 ? 0 : grid.maxY() + 1) + 3);

            rock.push(grid, jets[jetIndex]);
            jetIndex = (jetIndex + 1) % jets.length;

            while (rock.canFall(grid)) {
                rock.fall(grid);

                rock.push(grid, jets[jetIndex]);
                jetIndex = (jetIndex + 1) % jets.length;
            }

            rock.stop(grid);

            rockTypeIndex = (rockTypeIndex + 1) % 5;

            System.out.println();
        }

        grid.print();

        System.out.println(grid.maxY() + 1);
    }

    static class Grid {
        private final Map<Point, Character> points = new HashMap<>();

        private final int width;
        private final int height;

        public Grid(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public boolean contains(Point point) {
            return points.containsKey(point);

        }

        public void set(int x, int y, char ch) {
            points.put(new Point(x, y), ch);
        }

        public void print() {
            for (var y = 21; y >= 0; y--) {
                for (var x = 0; x < 7; x++) {
                    System.out.print(points.getOrDefault(new Point(x, y), '.'));
                }
                System.out.println();
            }
            System.out.println();
        }

        public int maxY() {
            return points.keySet().stream()
                    .mapToInt(Point::y)
                    .max()
                    .orElse(0);
        }
    }

    record Point(int x, int y) {
    }

    static class Rock {
        final RockType type;
        int rockLeftEdge;
        int rockBottomEdge;

        public Rock(RockType type, int rockBottomEdge) {
            this.type = type;
            this.rockLeftEdge = 2;
            this.rockBottomEdge = rockBottomEdge;
        }

        public void fall(Grid grid) {
            System.out.println(rockLeftEdge + "," + rockBottomEdge + " fall -1");
            rockBottomEdge--;
        }

        void push(Grid grid, int jet) {
            System.out.println(rockLeftEdge + "," + rockBottomEdge + " push " + jet);
            if (jet < 0) {
                if (!wouldCollide(grid, rockLeftEdge - 1, rockBottomEdge)) rockLeftEdge--;
            } else if (jet > 0) {
                if (!wouldCollide(grid, rockLeftEdge + 1, rockBottomEdge)) rockLeftEdge++;
            }
        }

        boolean canFall(Grid grid) {
            return !wouldCollide(grid, rockLeftEdge, rockBottomEdge - 1);
        }

        private boolean wouldCollide(Grid grid, int newLeftEdge, int newBottomEdge) {
            return newBottomEdge < 0 || newLeftEdge < 0 || newLeftEdge + type.width > 7
                    || type.points.stream().anyMatch(point -> grid.contains(new Point(newLeftEdge + point.x, newBottomEdge + point.y)));
        }

        void stop(Grid grid) {
            type.points.forEach(point ->
                    grid.set(point.x + rockLeftEdge, point.y + rockBottomEdge, '#'));
        }
    }

    record RockType(int width, Set<Point> points) {

    }
}
