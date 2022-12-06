package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day6Part2 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day6/input.txt"));
        System.out.println(process(lines.get(0)));
    }

    static int process(String str) {
        for (var i = 14; i < str.length(); i++) {
            var marker = str.substring(i - 14, i).toCharArray();

            if (allUnique(marker)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean allUnique(char[] marker) {
        for (var i = 0; i < marker.length; i++) {
            for (var j = i + 1; j < marker.length; j++) {
                if (marker[i] == marker[j]) return false;
            }
        }
        return true;
    }

}
