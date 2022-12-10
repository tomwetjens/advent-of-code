package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10Part2 {

    public static void main(String[] args) throws Exception {
        var sum = 0;
        var cycle = 1;
        var X = 1;

        var screen = new char[6][40];

        for (var line : Files.readAllLines(Paths.get("inputs/day10/input.txt"))) {
            System.out.println(cycle + ": X=" + X + " " + line);

            if ((cycle + 20) % 40 == 0)
                sum += cycle * X;

            draw(cycle, X, screen);

            cycle++;

            if (line.startsWith("addx")) {
                var V = Integer.parseInt(line.split(" ")[1]);

                System.out.println(cycle + ": X=" + X);

                if ((cycle + 20) % 40 == 0)
                    sum += cycle * X;

                draw(cycle, X, screen);

                cycle++;

                X += V;
            }
        }

        for (var y = 0; y < screen.length; y++) {
            for (var x = 0; x< screen[y].length; x++)
                System.out.print(screen[y][x]);
            System.out.println();
        }
    }

    private static void draw(int cycle, int X, char[][] screen) {
        var x = (cycle - 1) % 40;
        var y = (cycle - 1) / 40;
        screen[y][x] = x >= X - 1 && x <= X + 1 ? '#' : '.';
    }

}
