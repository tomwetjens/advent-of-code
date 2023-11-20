package com.wetjens.aoc2022;

import one.util.streamex.IntStreamEx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Day19Part1 {

    static final int MAX_MINUTES = 24;

    public static void main(String[] args) throws Exception {
        var blueprints = Files.lines(Paths.get("inputs/day19/input.txt"))
                .map(Blueprint::parse)
                .toList();

        var result = IntStreamEx.range(0, blueprints.size())
                .map(i -> {
                    var id = i + 1;
                    var geodes = maxCrackedGeodes(blueprints.get(i), State.INITIAL);
                    System.out.println("Blueprint " + id + ": " + geodes);
                    return geodes * id;
                })
                .sum();

        System.out.println(result);
    }

    static int maxCrackedGeodes(Blueprint blueprint, State state) {
        return maxCrackedGeodes(blueprint, state, new HashMap<>());
    }

    private static int maxCrackedGeodes(Blueprint blueprint, State state, Map<State, Integer> cache) {
        if (state.minute == MAX_MINUTES)
            return state.geodes;

        var cached = cache.get(state);
        if (cached != null) {
            return cached;
        }

        var max = maxCrackedGeodes(blueprint, state.advanceTime(1), cache);

        if (state.minute < MAX_MINUTES) {
            if (state.canStartBuildingGeodeRobot(blueprint))
                max = Math.max(max, maxCrackedGeodes(blueprint, state.startBuildingGeodeRobot(blueprint), cache));
            else {
                if (state.canStartBuildingOreRobot(blueprint) && state.oreRobots < blueprint.maxOreCost)
                    max = Math.max(max, maxCrackedGeodes(blueprint, state.startBuildingOreRobot(blueprint), cache));

                if (state.canStartBuildingClayRobot(blueprint) && state.clayRobots < blueprint.obsidianRobotCostClay)
                    max = Math.max(max, maxCrackedGeodes(blueprint, state.startBuildingClayRobot(blueprint), cache));

                if (state.canStartBuildingObsidianRobot(blueprint) && state.obsidianRobots < blueprint.geodeRobotCostObsidian)
                    max = Math.max(max, maxCrackedGeodes(blueprint, state.startBuildingObsidianRobot(blueprint), cache));
            }
        }

        cache.put(state, max);

        return max;
    }

    record State(
            int minute,
            int ore,
            int clay,
            int obsidian,
            int geodes,
            int oreRobots,
            int clayRobots,
            int obsidianRobots,
            int geodeRobots) {

        static final State INITIAL = new State(0, 0, 0, 0, 0, 1, 0, 0, 0);

        boolean canStartBuildingOreRobot(Blueprint blueprint) {
            return ore >= blueprint.oreRobotCostOre;
        }

        boolean canStartBuildingClayRobot(Blueprint blueprint) {
            return ore >= blueprint.clayRobotCostOre;
        }

        boolean canStartBuildingObsidianRobot(Blueprint blueprint) {
            return ore >= blueprint.obsidianRobotCostOre && clay >= blueprint.obsidianRobotCostClay;
        }

        boolean canStartBuildingGeodeRobot(Blueprint blueprint) {
            return ore >= blueprint.geodeRobotCostOre && obsidian >= blueprint.geodeRobotCostObsidian;
        }

        State startBuildingOreRobot(Blueprint blueprint) {
            return new State(minute + 1,
                    ore - blueprint.oreRobotCostOre + oreRobots,
                    clay + clayRobots,
                    obsidian + obsidianRobots,
                    geodes + geodeRobots,
                    oreRobots + 1,
                    clayRobots,
                    obsidianRobots,
                    geodeRobots);
        }

        State startBuildingClayRobot(Blueprint blueprint) {
            return new State(minute + 1,
                    ore - blueprint.clayRobotCostOre + oreRobots,
                    clay + clayRobots,
                    obsidian + obsidianRobots,
                    geodes + geodeRobots,
                    oreRobots,
                    clayRobots + 1,
                    obsidianRobots,
                    geodeRobots);
        }

        State startBuildingObsidianRobot(Blueprint blueprint) {
            return new State(minute + 1,
                    ore - blueprint.obsidianRobotCostOre + oreRobots,
                    clay - blueprint.obsidianRobotCostClay + clayRobots,
                    obsidian + obsidianRobots,
                    geodes + geodeRobots,
                    oreRobots, clayRobots,
                    obsidianRobots + 1,
                    geodeRobots);
        }

        State startBuildingGeodeRobot(Blueprint blueprint) {
            return new State(minute + 1,
                    ore - blueprint.geodeRobotCostOre + oreRobots,
                    clay + clayRobots,
                    obsidian - blueprint.geodeRobotCostObsidian + obsidianRobots,
                    geodes + geodeRobots,
                    oreRobots,
                    clayRobots,
                    obsidianRobots,
                    geodeRobots + 1);
        }

        State advanceTime(int minutes) {
            return new State(minute + minutes, ore + oreRobots * minutes, clay + clayRobots * minutes, obsidian + obsidianRobots * minutes, geodes + geodeRobots * minutes, oreRobots, clayRobots, obsidianRobots, geodeRobots);
        }
    }

    record Blueprint(
            int oreRobotCostOre,
            int clayRobotCostOre,
            int obsidianRobotCostOre,
            int obsidianRobotCostClay,
            int geodeRobotCostOre,
            int geodeRobotCostObsidian,
            int maxOreCost
    ) {
        static Blueprint parse(String s) {
            var parts = s.split(" ");

            var oreRobotCostOre = Integer.parseInt(parts[6]);
            var clayRobotCostOre = Integer.parseInt(parts[12]);
            var obsidianRobotCostOre = Integer.parseInt(parts[18]);
            var obsidianRobotCostClay = Integer.parseInt(parts[21]);
            var geodeRobotCostOre = Integer.parseInt(parts[27]);
            var geodeRobotCostObsidian = Integer.parseInt(parts[30]);

            return new Blueprint(
                    oreRobotCostOre,
                    clayRobotCostOre,
                    obsidianRobotCostOre,
                    obsidianRobotCostClay,
                    geodeRobotCostOre,
                    geodeRobotCostObsidian,
                    Math.max(Math.max(oreRobotCostOre, clayRobotCostOre), obsidianRobotCostOre));
        }
    }
}
