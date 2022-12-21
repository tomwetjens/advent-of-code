package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day17Part2 {

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
    public static final int MAX_ROCKS_TO_SIMULATE = 50000;

    public static void main(String[] args) throws Exception {
        var jets = Files.readAllLines(Paths.get("inputs/day17/input.txt")).get(0).chars()
                .map(ch -> ch == '<' ? -1 : 1)
                .toArray();

        var jetIndex = 0;
        var rockTypeIndex = 0;

        var grid = new Grid(7, Integer.MAX_VALUE);
        var numberOfRocksAtHeight = new HashMap<Integer, Integer>();

        for (var n = 1; n <= MAX_ROCKS_TO_SIMULATE; n++) {
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

            var height = grid.maxY() + 1;

            numberOfRocksAtHeight.put(height, n);

            rockTypeIndex = (rockTypeIndex + 1) % 5;
        }

        grid.print();


        var currentMaxY = grid.maxY();
        var smallestRepeatingHeight = Integer.MAX_VALUE;


        nextRepeatingLen:
        for (var repeatingLen = 500; repeatingLen < 3000; repeatingLen++) {
            for (var y = 0; y < repeatingLen; y++) {
                for (var x = 0; x < 7; x++) {
                    if (!Objects.equals(
                            grid.points.get(new Point(x, currentMaxY - y)),
                            grid.points.get(new Point(x, currentMaxY - repeatingLen - y)))) {
                        continue nextRepeatingLen;
                    }
                }
            }

            smallestRepeatingHeight = repeatingLen;

            break;
        }

        var currentHeight = currentMaxY + 1;

        System.out.println("found smallest repeating section: height=" + smallestRepeatingHeight);

        var repeatsStartHeight = currentHeight;
        var rocksAtRepeatsStartHeight = numberOfRocksAtHeight.get(repeatsStartHeight);
        System.out.println("repeats start at height " + repeatsStartHeight + " (" + rocksAtRepeatsStartHeight + " rocks)");


        var rocksPerRepeat = rocksAtRepeatsStartHeight - numberOfRocksAtHeight.get(currentHeight - smallestRepeatingHeight);
        System.out.println("every repeat means " + rocksPerRepeat + " rocks");

        var checkRocks = 2022;
        var checkRepeats = (checkRocks - rocksAtRepeatsStartHeight) / (double) rocksPerRepeat;
        System.out.println("target " + checkRocks + " rocks means " + checkRepeats + " repeats = " + (checkRepeats * smallestRepeatingHeight) + " height");
        System.out.println(Math.ceil(checkRepeats * smallestRepeatingHeight + repeatsStartHeight));


        var targetRocks = 1000000000000L;
        var targetRepeats = (long) Math.floor((targetRocks - rocksAtRepeatsStartHeight) / (double) rocksPerRepeat);
        System.out.println("target " + targetRocks + " rocks means " + targetRepeats + " repeats");
        var addedHeightFromRepeats = (targetRepeats * smallestRepeatingHeight);

        var remainingToSimulate = targetRocks - rocksAtRepeatsStartHeight - targetRepeats * rocksPerRepeat;
        System.out.println(remainingToSimulate + " more to simulate");

        for (var n = 1; n <= remainingToSimulate; n++) {
            var rockType = ROCK_TYPES.get(rockTypeIndex);

            var rock = new Rock(rockType, grid.maxY() + 1 + 3);

            rock.push(grid, jets[jetIndex]);
            jetIndex = (jetIndex + 1) % jets.length;

            while (rock.canFall(grid)) {
                rock.fall(grid);

                rock.push(grid, jets[jetIndex]);
                jetIndex = (jetIndex + 1) % jets.length;
            }

            rock.stop(grid);

            rockTypeIndex = (rockTypeIndex + 1) % 5;
        }

        var totalHeight = addedHeightFromRepeats + grid.maxY() + 1;
        System.out.println(totalHeight);

        // found smallest repeating section: height=2610
        // repeats start at height 76965 (50000 rocks)
        // every repeat means 1695 rocks
        // target 1000000000000 rocks means 589970471 repeats
        // 1655 more to simulate
        // total height: 1539823008825
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
            for (var y = 2000; y >= 0; y--) {
                System.out.print(String.format("%4d ", y));
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
            rockBottomEdge--;
        }

        void push(Grid grid, int jet) {
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
