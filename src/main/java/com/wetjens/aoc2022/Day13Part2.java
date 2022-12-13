package com.wetjens.aoc2022;

import one.util.streamex.IntStreamEx;
import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day13Part2 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day13/input.txt"));

        var packets = lines.stream()
                .filter(line -> !line.isEmpty())
                .map(JSONArray::new)
                .collect(Collectors.toCollection(ArrayList::new));

        var divider1 = new JSONArray("[[2]]");
        var divider2 = new JSONArray("[[6]]");
        packets.add(divider1);
        packets.add(divider2);

        packets.sort(Day13Part2::compare);

        var index1 = IntStreamEx.range(0, packets.size()).findFirst(i -> packets.get(i) == divider1).orElseThrow() + 1;
        var index2 = IntStreamEx.range(0, packets.size()).findFirst(i -> packets.get(i) == divider2).orElseThrow() + 1;

        System.out.println(index1 * index2);
    }

    private static int compare(Object a, Object b) {
        if (a instanceof JSONArray ar && b instanceof JSONArray br) {
            return compareLists(ar, br);
        } else if (a instanceof JSONArray ar) {
            return compareLists(ar, new JSONArray(List.of(b)));
        } else if (b instanceof JSONArray br) {
            return compareLists(new JSONArray(List.of(a)), br);
        } else {
            return compareIntegers((Integer) a, (Integer) b);
        }
    }

    private static int compareIntegers(Integer a, Integer b) {
        return a.compareTo(b);
    }

    private static int compareLists(JSONArray ar, JSONArray br) {
        for (var i = 0; i < Math.max(ar.length(), br.length()); i++) {
            if (i >= ar.length()) {
                return -1;
            }
            if (i >= br.length()) {
                return 1;
            }

            var result = compare(ar.get(i), br.get(i));
            if (result != 0)
                return result;
        }
        return 0;
    }
}
