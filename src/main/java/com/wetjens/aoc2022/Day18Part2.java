package com.wetjens.aoc2022;

import com.wetjens.aoc2022.Day18Part1.Vector3D;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day18Part2 {

    public static void main(String[] args) throws Exception {
        var cubes = Files.lines(Paths.get("inputs/day18/input.txt"))
                .map(Vector3D::parse)
                .collect(Collectors.toSet());


        var boundingBox = BoundingBox3D.from(cubes);

        var cache = new HashMap<Vector3D, Boolean>();
        var count = cubes.stream()
                .flatMap(Day18Part2::neighbors)
                .filter(side -> !cubes.contains(side) && waterCanReach(side, cubes, boundingBox, cache))
                .count();

        System.out.println(count);
    }

    private static boolean waterCanReach(Vector3D point, Set<Vector3D> cubes, BoundingBox3D boundingBox, Map<Vector3D, Boolean> cache) {
        var visited = new HashSet<Vector3D>();
        var unvisited = new LinkedList<Vector3D>();
        unvisited.add(point);

        var result = false;

        while (!unvisited.isEmpty()) {
            var p = unvisited.poll();

            visited.add(p);

            var cached = cache.get(p);
            if (cached != null && cached) {
                result = true;
                break;
            }

            if (!boundingBox.contains(p)) {
                result = true;
                break;
            }

            neighbors(p)
                    .filter(n -> !cubes.contains(n) && !visited.contains(n))
                    .forEach(unvisited::addFirst);
        }

        if (result) {
            for (var p : visited)
                cache.put(p, true);
        }

        cache.put(point, result);

        return result;
    }


    static Stream<Vector3D> neighbors(Vector3D point) {
        return Stream.of(
                new Vector3D(point.x() + 1, point.y(), point.z()),
                new Vector3D(point.x() - 1, point.y(), point.z()),
                new Vector3D(point.x(), point.y() + 1, point.z()),
                new Vector3D(point.x(), point.y() - 1, point.z()),
                new Vector3D(point.x(), point.y(), point.z() + 1),
                new Vector3D(point.x(), point.y(), point.z() - 1)
        );
    }

    record BoundingBox3D(Vector3D min, Vector3D max) {
        static BoundingBox3D from(Collection<Vector3D> points) {
            int minX = 0, minY = 0, minZ = 0;
            int maxX = 0, maxY = 0, maxZ = 0;

            for (var point : points) {
                minX = Math.min(minX, point.x());
                minY = Math.min(minY, point.y());
                minZ = Math.min(minZ, point.z());
                maxX = Math.max(maxX, point.x());
                maxY = Math.max(maxY, point.y());
                maxZ = Math.max(maxZ, point.z());
            }

            return new BoundingBox3D(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
        }

        boolean contains(Vector3D point) {
            return point.x() >= min.x() && point.x() <= max.x()
                    && point.y() >= min.y() && point.y() <= max.y()
                    && point.z() >= min.z() && point.z() <= max.z();
        }
    }
}
