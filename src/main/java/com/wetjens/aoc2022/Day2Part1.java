package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day2Part1 {

    private static final int DRAW = 3;
    private static final int WIN = 6;
    private static final int LOSE = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(
                Files.lines(Paths.get("inputs/day2/input.txt"))
                        .mapToInt(Day2Part1::score)
                        .sum());
    }

    static int score(String line) {
        // A/X=Rock(1) B/Y=Paper(2) C/Z=Scissors(3)
        var opponent = 1 + line.charAt(0) - 'A'; // 1,2,3
        var self = 1 + line.charAt(2) - 'X'; // 1,2,3

        // 1 defeats 3
        // 2 defeats 1
        // 3 defeats 2

        return self + (opponent == self ? DRAW : self == 1 && opponent == 3 || self == 2 && opponent == 1 || self == 3 && opponent == 2 ? WIN : LOSE);
    }

}
