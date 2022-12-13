package com.wetjens.aoc2022;

import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day13Part1 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day13/input.txt"));

        var sum = 0;

        for (var i = 0; i < lines.size(); i += 3) {
            var a = new JSONArray(lines.get(i));
            var b = new JSONArray(lines.get(i + 1));

            var pairNr = (i / 3) + 1;

            System.out.println("== Pair " + pairNr + " ==");
            if (compare(a, b, "") <= 0) {
                sum += pairNr;
            }
            System.out.println();
        }

        System.out.println(sum);
    }

    private static int compare(Object a, Object b, String indent) {
        System.out.println(indent + "- Compare " + a + " vs " + b);
        if (a instanceof JSONArray ar && b instanceof JSONArray br) {
            return compareLists(ar, br, indent + "  ");
        } else if (a instanceof JSONArray ar) {
            return compareLists(ar, new JSONArray(List.of(b)), indent + "  ");
        } else if (b instanceof JSONArray br) {
            return compareLists(new JSONArray(List.of(a)), br, indent + "  ");
        } else {
            return compareIntegers((Integer) a, (Integer) b, indent + "  ");
        }
    }

    private static int compareIntegers(Integer a, Integer b, String indent) {
        if (a < b) {
            System.out.println(indent + "- Left side is smaller, so inputs are in the right order");
        } else if (a > b) {
            System.out.println(indent + "- Right side is smaller, so inputs are not in the right order");
        }
        return a.compareTo(b);
    }

    private static int compareLists(JSONArray ar, JSONArray br, String indent) {
        for (var i = 0; i < Math.max(ar.length(), br.length()); i++) {
            if (i >= ar.length()) {
                System.out.println(indent + "- Left side ran out of items, so inputs are in the right order");
                return -1;
            }
            if (i >= br.length()) {
                System.out.println(indent + "- Right side ran out of items, so inputs are not in the right order");
                return 1;
            }

            var result = compare(ar.get(i), br.get(i), indent + "  ");
            if (result != 0)
                return result;
        }
        return 0;
    }
}
