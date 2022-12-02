package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day2Part2 {

    private static final int DRAW = 3;
    private static final int WIN = 6;
    private static final int LOSE = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(
                Files.lines(Paths.get("inputs/day2/input.txt"))
                        .mapToInt(Day2Part2::score)
                        .sum());
    }

    static int score(String line) {
        // A=Rock(1) B=Paper(2) C=Scissors(3)
        var opponent = 1 + line.charAt(0) - 'A'; // 1,2,3
        var outcome = 3 * (line.charAt(2) - 'X'); // X=0,Y=3,Z=6

        // 1 defeats 3
        // 2 defeats 1
        // 3 defeats 2

        return outcome + (outcome == DRAW ? opponent
                : outcome == LOSE ? (opponent == 1 ? 3 : opponent == 2 ? 1 : 2)
                : (opponent == 1 ? 2 : opponent == 2 ? 3 : 1));
    }

}
