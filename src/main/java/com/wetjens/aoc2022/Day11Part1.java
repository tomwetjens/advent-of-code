package com.wetjens.aoc2022;

import one.util.streamex.IntStreamEx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11Part1 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day11/input.txt"));

        var monkeys = IntStreamEx.range(0, lines.size(), 7)
                .mapToObj(i -> new Monkey(lines.subList(i + 1, i + 6)))
                .collect(Collectors.toList());

        IntStream.rangeClosed(1, 20)
                .forEach(round -> {
                    IntStream.range(0, monkeys.size())
                            .forEach(i -> {
                                System.out.println("Monkey " + i);

                                var monkey = monkeys.get(i);

                                while (monkey.hasItems()) {
                                    monkey.inspect()
                                            .ifPresent(item -> {
                                                System.out.println("  Monkey inspects an item with a worry level of " + item + ".");

                                                var nw = monkey.operation.apply(item);

                                                System.out.println("    Worry level becomes " + nw + ".");

                                                var nwnw = nw / 3;

                                                System.out.println("    Monkey gets bored with item. Worry level is divided by 3 to " + nwnw + ".");

                                                if (nwnw % monkey.divisibleBy == 0) {
                                                    System.out.println("    Current worry level is divisible by " + monkey.divisibleBy + ".");

                                                    System.out.println("    Item with worry level " + nwnw + " is thrown to monkey " + monkey.ifTrue + ".");
                                                    monkeys.get(monkey.ifTrue).receive(nwnw);
                                                } else {
                                                    System.out.println("    Current worry level is not divisible by " + monkey.divisibleBy + ".");

                                                    System.out.println("    Item with worry level " + nwnw + " is thrown to monkey " + monkey.ifFalse + ".");
                                                    monkeys.get(monkey.ifFalse).receive(nwnw);
                                                }
                                            });
                                }

                                System.out.println();
                            });


                    System.out.println("After round " + round + ", the monkeys are holding items with these worry levels:");
                    IntStream.range(0, monkeys.size())
                            .forEach(i -> System.out.println("Monkey " + i + ": " + monkeys.get(i).items));
                });

        System.out.println();

        var inspections = IntStream.range(0, monkeys.size())
                .peek(i -> System.out.println("Monkey " + i + " inspected items " + monkeys.get(i).inspections + " times."))
                .map(i -> monkeys.get(i).inspections)
                .sorted()
                .toArray();

        System.out.println(inspections[inspections.length - 1]
                * inspections[inspections.length - 2]);
    }

    static class Monkey {

        private final List<Integer> items;
        private final Function<Integer, Integer> operation;
        private final int divisibleBy;
        private final int ifTrue;
        private final int ifFalse;

        private int inspections;

        Monkey(List<String> lines) {
            items = Arrays.stream(lines.get(0)
                            .trim()
                            .replace(",", "")
                            .split(" "))
                    .skip(2)
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(LinkedList::new));

            operation = parseOperation(lines.get(1));
            divisibleBy = Integer.parseInt(lines.get(2).trim().split(" ")[3]);
            ifTrue = parseThrowTo(lines.get(3));
            ifFalse = parseThrowTo(lines.get(4));
        }

        private static int parseThrowTo(String line) {
            var parts = line.trim().split(" ");
            return Integer.parseInt(parts[parts.length - 1]);
        }

        private static Function<Integer, Integer> parseOperation(String line) {
            var parts = line.trim().split(" ");

            var operator = parseOperator(parts[4].charAt(0));
            var right = parseExpression(parts[5]);

            return old -> operator.apply(old, right.apply(old));
        }

        private static Function<Integer, Integer> parseExpression(String str) {
            if (str.equals("old")) {
                return Function.identity();
            } else {
                var value = Integer.parseInt(str);
                return old -> value;
            }
        }

        private static BiFunction<Integer, Integer, Integer> parseOperator(char operator) {
            return switch (operator) {
                case '+' -> (a, b) -> a + b;
                case '-' -> (a, b) -> a - b;
                case '*' -> (a, b) -> a * b;
                case '/' -> (a, b) -> a / b;
                default -> throw new IllegalArgumentException("unknown operator: " + operator);
            };
        }

        Optional<Integer> inspect() {
            if (items.isEmpty())
                return Optional.empty();

            inspections++;

            return Optional.of(items.remove(0));
        }

        void receive(int item) {
            items.add(item);
        }

        boolean hasItems() {
            return !items.isEmpty();
        }
    }
}
