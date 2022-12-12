package com.wetjens.aoc2022;

import com.wetjens.aoc2022.library.graphs.Dijkstra;
import com.wetjens.aoc2022.library.graphs.Edge;
import com.wetjens.aoc2022.library.graphs.Graph;
import one.util.streamex.IntStreamEx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12Part1 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day12/input.txt"));

        var heightmap = Heightmap.parse(lines);

        heightmap.print();
        System.out.println();

        var path = Dijkstra.shortestPath(heightmap, heightmap.source(), heightmap.target());

        heightmap.printPath(path);
        System.out.println();

        System.out.println(path.size() + " steps");
    }

    record Heightmap(Square[][] squares) implements Graph<Square, Move> {

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

        Square source() {
            return IntStreamEx.range(0, squares.length)
                    .flatMapToObj(y -> Arrays.stream(squares[y])
                            .filter(square -> square.letter == 'S'))
                    .findFirst()
                    .orElseThrow();
        }

        Square target() {
            return IntStreamEx.range(0, squares.length)
                    .flatMapToObj(y -> Arrays.stream(squares[y])
                            .filter(square -> square.letter == 'E'))
                    .findFirst()
                    .orElseThrow();
        }

        @Override
        public Stream<Square> nodes() {
            return Arrays.stream(squares).flatMap(Arrays::stream);
        }

        @Override
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
                    .filter(move -> move.to.height() - move.from.height() <= 1);
        }

        void print() {
            for (var row : squares) {
                for (var square : row) {
                    System.out.print(square.letter);
                }
                System.out.println();
            }
        }

        void printPath(List<Move> path) {
            char[][] result = new char[squares.length][squares[0].length];
            for (var row : result) {
                Arrays.fill(row, '.');
            }

            for (var move : path) {
                result[move.from.y][move.from.x] =
                        move.from.x < move.to.x ? '>'
                                : move.from.x > move.to.x ? '<'
                                : move.from.y < move.to.y ? 'v'
                                : '^';
            }

            var target = target();
            result[target.y][target.x] = 'E';

            for (var row : result) {
                for (var ch : row) {
                    System.out.print(ch);
                }
                System.out.println();
            }
        }
    }

    record Square(int x, int y, char letter) {
        int height() {
            return (letter == 'S' ? 'a' : letter == 'E' ? 'z' : letter) - 'a';
        }
    }

    record Move(Square from, Square to) implements Edge<Square> {
        @Override
        public int cost() {
            return 1;
        }
    }

}
