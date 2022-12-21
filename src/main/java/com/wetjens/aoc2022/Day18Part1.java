package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day18Part1 {

    public static void main(String[] args) throws Exception {
        var cubes= Files.lines(Paths.get("inputs/day18/input.txt"))
                .map(Vector3D::parse)
                .collect(Collectors.toSet());

        var count = cubes.stream()
                .flatMap(cube -> Stream.of(
                        new Vector3D(cube.x + 1, cube.y, cube.z),
                        new Vector3D(cube.x - 1, cube.y, cube.z),
                        new Vector3D(cube.x, cube.y + 1, cube.z),
                        new Vector3D(cube.x, cube.y - 1, cube.z),
                        new Vector3D(cube.x, cube.y, cube.z + 1),
                        new Vector3D(cube.x, cube.y, cube.z -1)
                ))
                .filter(side -> !cubes.contains(side))
                .count();

        System.out.println(count);
    }

    record Vector3D(int x, int y, int z) {
        static Vector3D parse(String s) {
            var parts = s.split(",", 3);
            return new Vector3D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }
    }
}
