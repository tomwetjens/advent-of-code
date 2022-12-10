package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10Part1 {

    public static void main(String[] args) throws Exception {
        var sum = 0;
        var cycle = 1;
        var x = 1;

        for (var line : Files.readAllLines(Paths.get("inputs/day10/input.txt"))) {
            System.out.println(cycle + ": X=" + x + " " + line);

            if ((cycle + 20) % 40 == 0)
                sum += cycle * x;

            cycle++;

            if (line.startsWith("addx")) {
                var v = Integer.parseInt(line.split(" ")[1]);

                System.out.println(cycle + ": X=" + x);

                if ((cycle + 20) % 40 == 0)
                    sum += cycle * x;
                cycle++;

                x += v;
            }
        }

        System.out.println(sum);
    }

}
