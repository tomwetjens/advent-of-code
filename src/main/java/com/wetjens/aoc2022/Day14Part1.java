package com.wetjens.aoc2022;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Day14Part1 {

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day14/input.txt"));

        char[][] grid = new char[1000][1000];
        int[] abyss = new int[1000];

        for (var line : lines) {
            var path = line.split(" -> ");

            var prevx = -1;
            var prevy = -1;

            for (var part : path) {
                var point = part.split(",");

                var x = Integer.parseInt(point[0]);
                var y = Integer.parseInt(point[1]);

                if (prevx != -1 && prevy != -1) {
                    var dx = Integer.compare(x, prevx);
                    var dy = Integer.compare(y, prevy);

                    if (dy == 0) {
                        var lx = prevx;
                        for (var n = 0; n <= Math.abs(x - prevx); n++) {
                            grid[y][lx] = '#';
                            abyss[lx] = Math.max(abyss[lx], y);
                            lx += dx;
                        }
                    } else if (dx == 0) {
                        var ly = prevy;
                        for (var n = 0; n <= Math.abs(y - prevy); n++) {
                            grid[ly][x] = '#';
                            abyss[x] = Math.max(abyss[x], ly);
                            ly += dy;
                        }
                    }
                }

                prevx = x;
                prevy = y;
            }
        }

        grid[0][500] = '+';

//        printGrid(grid);

        var count = 0;

        for (var n = 0; n < 1000000; n++) {
            var sx = 500;
            var sy = 0;

            while (sy < abyss[sx]) {
                if (grid[sy+1][sx] == '\0') {
                    // down
                    grid[sy][sx] = '\0';
                    grid[sy+1][sx] = 'o';
                    sy++;
                } else if (grid[sy+1][sx-1] == '\0') {
                    // down and left
                    grid[sy][sx] = '\0';
                    grid[sy+1][sx-1] = 'o';
                    sy++;
                    sx--;
                } else if (grid[sy+1][sx+1] == '\0') {
                    // down and right
                    grid[sy][sx] = '\0';
                    grid[sy+1][sx+1] = 'o';
                    sy++;
                    sx++;
                } else {
                    // rest
                    break;
                }

//                printGrid(grid);
            }

            if (sy >= abyss[sx]) {
                // abyss
                break;
            }

            count++;
        }

        printGrid(grid);
        System.out.println(count);

    }

    private static void printGrid(char[][] grid) {


        for (var y = 0; y < 10; y++) {
            for (var x = 494; x < 504; x++) {
                System.out.print(grid[y][x] != '\0' ? grid[y][x] : '.');
            }
            System.out.println();
        }
        System.out.println();
    }

    record Point(int x,int y){}
}
