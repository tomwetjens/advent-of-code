package com.wetjens.aoc2022;

import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12Part2 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day12/input.txt"));

        var heightmap = Heightmap.parse(lines);
        var target = heightmap.target();

        var distances = heightmap.calculateDistancesTo(target);

        var distancesFromLowest = EntryStream.of(distances)
                .filterValues(dist -> dist > 0)
                .filterKeys(square -> square.height() == 0)
                .toMap();

        var steps = EntryStream.of(distancesFromLowest).values()
                .min(Comparator.naturalOrder())
                .orElseThrow();

        System.out.println(steps + " steps");
    }

    record Heightmap(Square[][] squares) {

        static Heightmap parse(List<String> lines) {
            return new Heightmap(IntStream.range(0, lines.size())
                    .mapToObj(y -> {
                        var line = lines.get(y);
                        return IntStream.range(0, line.length())
                                .mapToObj(x -> new Square(x, y, line.charAt(x)))
                                .toArray(Square[]::new);
                    })
                    .toArray(Square[][]::new));
        }

        Square target() {
            return IntStreamEx.range(0, squares.length)
                    .flatMapToObj(y -> Arrays.stream(squares[y])
                            .filter(square -> square.letter == 'E'))
                    .findFirst()
                    .orElseThrow();
        }

        public Stream<Move> edges(Square square) {
            return Stream.of(
                            square.x > 0 ?
                                    new Move(square, squares[square.y][square.x - 1]) : null,
                            square.x < squares[square.y].length - 1 ?
                                    new Move(square, squares[square.y][square.x + 1]) : null,
                            square.y > 0 ?
                                    new Move(square, squares[square.y - 1][square.x]) : null,
                            square.y < squares.length - 1 ?
                                    new Move(square, squares[square.y + 1][square.x]) : null
                    )
                    .filter(Objects::nonNull)
                    .filter(move -> move.from.height() - move.to.height() <= 1);
        }

        Map<Square, Integer> calculateDistancesTo(Square target) {
            var unvisited = new HashSet<Square>();
            var dist = new HashMap<Square, Integer>();

            Arrays.stream(squares)
                    .flatMap(Arrays::stream)
                    .forEach(node -> {
                        dist.put(node, Integer.MAX_VALUE);
                        unvisited.add(node);
                    });

            dist.put(target, 0);

            while (!unvisited.isEmpty()) {
                var current = unvisited.stream()
                        .min(Comparator.comparingInt(dist::get))
                        .orElseThrow(() -> new IllegalStateException("no more unvisited nodes"));

                unvisited.remove(current);

                edges(current)
//                    .filter(edge -> unvisited.contains(edge.to()))
                        .forEach(edge -> {
                            var v = edge.to();

                            var alt = dist.get(current) + edge.cost();

                            if (alt < dist.get(v)) {
                                dist.put(v, alt);
                            }
                        });
            }

            return dist;
        }
    }

    record Square(int x, int y, char letter) {
        int height() {
            return (letter == 'S' ? 'a' : letter == 'E' ? 'z' : letter) - 'a';
        }
    }

    record Move(Square from, Square to) {
        public int cost() {
            return 1;
        }
    }

}
