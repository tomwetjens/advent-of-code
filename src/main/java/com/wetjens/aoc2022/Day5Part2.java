package com.wetjens.aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day5Part2 {

    public static void main(String[] args) throws Exception {
        var path = Paths.get("inputs/day5/input.txt");

        var stacks = parseStacks(path);
        printStacks(stacks);

        try (var lines = Files.lines(path)) {
            lines.filter(line -> line.startsWith("move"))
                    .map(Move::parse)
                    .forEachOrdered(move -> {
                        System.out.println(move);

                        var from = stacks.get(move.fromIndex);
                        var to = stacks.get(move.toIndex);

                        var remaining = move.amount;

                        var subList = from.subList(from.size() - move.amount, from.size());
                        to.addAll(subList);
                        subList.clear();

                        printStacks(stacks);
                        System.out.println();
                    });
        }

        System.out.println();

        printStacks(stacks);

        System.out.println();

        System.out.println(stacks.stream()
                .map(Stack::peek)
                .map(Crate::id)
                .collect(Collectors.joining()));
    }

    private static void printStacks(List<Stack<Crate>> stacks) {
        var maxSize = stacks.stream().mapToInt(Stack::size).max().getAsInt();

        for (var rowIndex = maxSize - 1; rowIndex >= 0; rowIndex--) {
            for (int stackIndex = 0; stackIndex < stacks.size(); stackIndex++) {
                var stack = stacks.get(stackIndex);

                if (stack.size() > rowIndex) {
                    System.out.print("[" + stack.get(rowIndex).id + "] ");
                } else {
                    System.out.print("    ");
                }
            }
            System.out.println();
        }

        for (int stackNr = 1; stackNr <= stacks.size(); stackNr++) {
            System.out.print(" " + stackNr + "  ");
        }
        System.out.println();
    }

    private static List<Stack<Crate>> parseStacks(Path path) throws IOException {
        var stacks = new ArrayList<Stack<Crate>>();

        try (var lines = Files.lines(path)) {
            lines.filter(line -> line.contains("["))
                    .forEachOrdered(line -> {
                        var chars = line.toCharArray();

                        for (var i = 1; i <= chars.length; i += 4) {
                            if (chars[i] != ' ') {
                                var stackIndex = i / 4;

                                while (stackIndex >= stacks.size()) {
                                    stacks.add(new Stack<>());
                                }

                                var crate = new Crate(Character.toString(chars[i]));
                                stacks.get(stackIndex).add(0, crate);
                            }
                        }
                    });
        }

        return stacks;
    }

    record Crate(String id) {
    }

    record Move(int amount, int fromIndex, int toIndex) {
        static Move parse(String str) {
            var parts = str.split(" ");
            return new Move(Integer.parseInt(parts[1]), Integer.parseInt(parts[3]) - 1, Integer.parseInt(parts[5]) - 1);
        }
    }
}
