package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day15Part2 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day15/input.txt"));

        var sensors = lines.stream()
                .map(Sensor::parse)
                .collect(Collectors.toMap(Sensor::position, Function.identity()));

        var grid = new HashMap<Point, Character>();

        sensors.forEach((position, sensor) -> {
            grid.put(sensor.position, 'S');
            grid.put(sensor.closestBeacon, 'B');
        });

        outer:
        for (var sensor : sensors.values()) {
            for (var y = -sensor.distanceToClosestBeacon - 1; y <= sensor.distanceToClosestBeacon + 1; y++) {
                var maxX = sensor.distanceToClosestBeacon - Math.abs(y) + 1;
                var minX = -maxX;

                var left = new Point(minX + sensor.position.x, y + sensor.position.y);
                if (left.x >= 0 && left.y >= 0 && left.y <= 4000000 && sensors.values().stream()
                        .noneMatch(otherSensor -> otherSensor.position.manhattanDistance(left) <= otherSensor.distanceToClosestBeacon)) {
                    System.out.println(left + " " + (left.x * 4000000L + left.y));
                    break outer;
                }

                var right = new Point(maxX + sensor.position.x, y + sensor.position.y);
                if (right.x <= 4000000 && right.y >= 0 && right.y <= 4000000 && sensors.values().stream()
                        .noneMatch(otherSensor -> otherSensor.position.manhattanDistance(right) <= otherSensor.distanceToClosestBeacon)) {
                    System.out.println(right + " " + (right.x * 4000000L + right.y));
                    break outer;
                }
            }
        }
    }

    record Point(int x, int y) {
        int manhattanDistance(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }
    }

    record Sensor(Point position, Point closestBeacon, int distanceToClosestBeacon) {
        static Sensor parse(String str) {
            // Sensor at x=2, y=0: closest beacon is at x=2, y=10
            var parts = str.split("[^-\\d]+");

            var position = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            var closestBeacon = new Point(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));

            return new Sensor(position, closestBeacon, position.manhattanDistance(closestBeacon));
        }
    }
}
