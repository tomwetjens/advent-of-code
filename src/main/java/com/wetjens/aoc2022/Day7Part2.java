package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;

public class Day7Part2 {

    static class Dir {
        final String name;
        final Map<String, Dir> subdirs = new LinkedHashMap<>();
        final Map<String, Long> files = new LinkedHashMap<>();

        Dir(String name) {
            this.name = name;
        }

        Stream<Dir> traverse() {
            return Stream.concat(Stream.of(this), subdirs.values().stream().flatMap(Dir::traverse));
        }

        long size() {
            return files.values().stream().mapToLong(Long::longValue).sum()
                    + subdirs.values().stream().mapToLong(Dir::size).sum();
        }
    }

    public static void main(String[] args) throws Exception {
        var root = new Dir("/");

        var current = root;

        var path = new Stack<Dir>();
        path.push(current);

        var lines = Files.readAllLines(Paths.get("inputs/day7/input.txt"));

        for (var line : lines) {
            if (line.startsWith("$ cd")) {
                var name = line.split(" ")[2];
                if (name.equals("..")) {
                    path.pop();
                    current = path.peek();
                } else if (name.equals("/")) {
                    current = root;
                    path.clear();
                    path.push(current);
                } else {
                    var dir = current.subdirs.computeIfAbsent(name, Dir::new);
                    path.push(dir);
                    current = dir;
                }
            } else if (!line.startsWith("$")) {
                if (!line.startsWith("dir")) {
                    var parts = line.split(" ");
                    var size = Long.parseLong(parts[0]);
                    current.files.putIfAbsent(parts[1], size);
                }
            }
        }

        printTree(root, "");

        var used = root.size();
        var total = 70000000;
        var free = total - used;
        var deleteAtLeast = 30000000 - free;

        var result = root.traverse()
                .filter(dir -> dir.size() >= deleteAtLeast)
                .min(Comparator.comparing(Dir::size))
                .orElseThrow();

        System.out.println(result.name + ": " + result.size());
    }

    static void printTree(Dir dir, String prefix) {
        System.out.println(prefix + "- " + dir.name + " (dir)");
        dir.subdirs.forEach((name, subdir) ->
                printTree(subdir, "  " + prefix));
        dir.files.forEach((name, size) ->
                System.out.println(prefix + "  - " + name + " (file, size=" + size + ")"));
    }

}
