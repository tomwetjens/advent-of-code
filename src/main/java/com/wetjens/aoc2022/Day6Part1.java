package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day6Part1 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day6/input.txt"));
        System.out.println(process(lines.get(0)));
    }

    static int process(String str) {
        for (var i = 4; i < str.length(); i++) {
            var marker = str.substring(i - 4, i).toCharArray();

            if (marker[0] != marker[1] && marker[0] != marker[2] && marker[0] != marker[3]
                    && marker[1] != marker[2] && marker[1] != marker[3]
                    && marker[2] != marker[3]) {
                return i;
            }
        }
        return -1;
    }

}
