package com.wetjens.aoc2022;

import one.util.streamex.IntStreamEx;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11Part2 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day11/input.txt"));

        var monkeys = IntStreamEx.range(0, lines.size(), 7)
                .mapToObj(i -> new Monkey(lines.subList(i + 1, i + 6)))
                .collect(Collectors.toList());

        var g = monkeys.stream()
                .map(m -> m.divisibleBy)
                .reduce(BigInteger::multiply)
                .orElseThrow();

        IntStream.rangeClosed(1, 10000)
                .forEach(round -> {
                    IntStream.range(0, monkeys.size())
                            .forEach(i -> {
//                                System.out.println("Monkey " + i);

                                var monkey = monkeys.get(i);

                                while (monkey.hasItems()) {
                                    monkey.inspect()
                                            .ifPresent(item -> {
//                                                System.out.println("  Monkey inspects an item with a worry level of " + item + ".");

                                                var nw = monkey.operation.apply(item);

//                                                System.out.println("    Worry level becomes " + nw + ".");

                                                var nwnw = nw.mod(g);

//                                                System.out.println("    Monkey gets bored with item. Worry level is divided by 3 to " + nwnw + ".");

                                                if (nwnw.mod(monkey.divisibleBy).equals(BigInteger.ZERO)) {
//                                                    System.out.println("    Current worry level is divisible by " + monkey.divisibleBy + ".");

//                                                    System.out.println("    Item with worry level " + nwnw + " is thrown to monkey " + monkey.ifTrue + ".");
                                                    monkeys.get(monkey.ifTrue).receive(nwnw);
                                                } else {
//                                                    System.out.println("    Current worry level is not divisible by " + monkey.divisibleBy + ".");

//                                                    System.out.println("    Item with worry level " + nwnw + " is thrown to monkey " + monkey.ifFalse + ".");
                                                    monkeys.get(monkey.ifFalse).receive(nwnw);
                                                }
                                            });
                                }

//                                System.out.println();
                            });


                    if (round == 1 || round == 20 || round % 1000 == 0) {
                        System.out.println("After round " + round);
                        IntStream.range(0, monkeys.size())
                                .forEach(i -> System.out.println("Monkey " + i + " inspected items " + monkeys.get(i).inspections + " times."));
                    }
                });

        System.out.println();

        var inspections = IntStream.range(0, monkeys.size())
                .peek(i -> System.out.println("Monkey " + i + " inspected items " + monkeys.get(i).inspections + " times."))
                .map(i -> monkeys.get(i).inspections)
                .sorted()
                .toArray();

        System.out.println((long)inspections[inspections.length - 1]
                * (long)inspections[inspections.length - 2]);
    }

    static class Monkey {

        private final List<BigInteger> items;
        private final Function<BigInteger, BigInteger> operation;
        private final BigInteger divisibleBy;
        private final int ifTrue;
        private final int ifFalse;

        private int inspections;

        Monkey(List<String> lines) {
            items = Arrays.stream(lines.get(0)
                            .trim()
                            .replace(",", "")
                            .split(" "))
                    .skip(2)
                    .map(BigInteger::new)
                    .collect(Collectors.toCollection(LinkedList::new));

            operation = parseOperation(lines.get(1));
            divisibleBy = new BigInteger(lines.get(2).trim().split(" ")[3]);
            ifTrue = parseThrowTo(lines.get(3));
            ifFalse = parseThrowTo(lines.get(4));
        }

        private static int parseThrowTo(String line) {
            var parts = line.trim().split(" ");
            return Integer.parseInt(parts[parts.length - 1]);
        }

        private static Function<BigInteger, BigInteger> parseOperation(String line) {
            var parts = line.trim().split(" ");

            var operator = parseOperator(parts[4].charAt(0));
            var right = parseExpression(parts[5]);

            return old -> operator.apply(old, right.apply(old));
        }

        private static Function<BigInteger, BigInteger> parseExpression(String str) {
            if (str.equals("old")) {
                return Function.identity();
            } else {
                var value = new BigInteger(str);
                return old -> value;
            }
        }

        private static BiFunction<BigInteger, BigInteger, BigInteger> parseOperator(char operator) {
            return switch (operator) {
                case '+' -> BigInteger::add;
                case '-' -> BigInteger::subtract;
                case '*' -> BigInteger::multiply;
                default -> throw new IllegalArgumentException("unknown operator: " + operator);
            };
        }

        Optional<BigInteger> inspect() {
            if (items.isEmpty())
                return Optional.empty();

            inspections++;

            return Optional.of(items.remove(0));
        }

        void receive(BigInteger item) {
            items.add(item);
        }

        boolean hasItems() {
            return !items.isEmpty();
        }
    }
}
