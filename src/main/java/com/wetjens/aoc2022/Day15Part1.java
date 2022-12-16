package com.wetjens.aoc2022;

import one.util.streamex.IntStreamEx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day15Part1 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day15/input.txt"));

        var sensors = lines.stream()
                .map(Sensor::parse)
                .collect(Collectors.toMap(Sensor::position, Function.identity()));

        var grid = new HashMap<Point, Character>();

        var result = new HashSet<Point>();

        sensors.forEach((position, sensor) -> {
            grid.put(sensor.position, 'S');
            grid.put(sensor.closestBeacon, 'B');

            positionsOnRowWithoutBeacon(sensor, 2000000)
                    .forEach(result::add);

//            positionsAroundWithoutBeacon(sensor)
//                    .forEach(pos -> grid.put(pos, '#'));
        });

//        printGrid(grid);

        System.out.println(result.size());
    }

    static Stream<Point> positionsAroundWithoutBeacon(Sensor sensor) {
        var distance = sensor.position.manhattanDistance(sensor.closestBeacon);

        return IntStreamEx.rangeClosed(sensor.position.y - distance, sensor.position.y + distance)
                .flatMapToObj(y -> positionsOnRowWithoutBeacon(sensor, y));
    }

    static Stream<Point> positionsOnRowWithoutBeacon(Sensor sensor, int y) {
        var distance = sensor.position.manhattanDistance(sensor.closestBeacon);

        return IntStream.rangeClosed(sensor.position.x - distance, sensor.position.x + distance)
                .mapToObj(x -> new Point(x, y))
                .filter(pos -> !pos.equals(sensor.position) && !pos.equals(sensor.closestBeacon))
                .filter(pos -> pos.manhattanDistance(sensor.position) <= distance);
    }

    static void printGrid(Map<Point, Character> grid) {
        var boundingBox = BoundingBox.fromPoints(grid.keySet());

        for (var y = boundingBox.minY; y <= boundingBox.maxY; y++) {
            for (var x = boundingBox.minX; x <= boundingBox.maxX; x++) {
                System.out.print(grid.getOrDefault(new Point(x, y), '.'));
            }
            System.out.println();
        }
    }

    record BoundingBox(int minX, int minY, int maxX, int maxY) {
        static BoundingBox fromPoints(Collection<Point> points) {
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

            for (var point : points) {
                if (point.x < minX) minX = point.x;
                if (point.x > maxX) maxX = point.x;
                if (point.y < minY) minY = point.y;
                if (point.y > maxY) maxY = point.y;
            }

            return new BoundingBox(minX, minY, maxX, maxY);
        }
    }

    record Point(int x, int y) {
        int manhattanDistance(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }
    }

    record Sensor(Point position, Point closestBeacon) {
        static Sensor parse(String str) {
            // Sensor at x=2, y=0: closest beacon is at x=2, y=10
            var parts = str.split("[^-\\d]+");

            var position = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            var closestBeacon = new Point(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));

            return new Sensor(position, closestBeacon);
        }
    }
}
