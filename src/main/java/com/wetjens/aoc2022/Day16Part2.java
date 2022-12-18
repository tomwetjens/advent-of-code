package com.wetjens.aoc2022;

import com.wetjens.aoc2022.library.graphs.Dijkstra;
import com.wetjens.aoc2022.library.graphs.Edge;
import com.wetjens.aoc2022.library.graphs.Graph;
import com.wetjens.aoc2022.library.graphs.Node;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16Part2 {

    static final int MAX_MINUTES = 26;

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("inputs/day16/input.txt"));

        var startTime = Instant.now();

        var networkOfPipes = NetworkOfPipes.parse(lines);

        var distances = networkOfPipes.precalculateDistances();
        var state = new State(new BitSet(), networkOfPipes.start(), 1);

        var paths = pathsToOpenValves(networkOfPipes.flowRates(), distances, state, new HashMap<>());
        System.out.println(paths.size() + " paths");

        var bestPath = paths.stream()
                .max(Comparator.comparingInt(Path::releasedPressure))
                .orElseThrow();

        System.out.println("Best path: " + bestPath);

        var bestCombined = StreamEx.of(paths)
                .flatMapToInt(a -> paths.stream()
                        .filter(b -> b != a && b.hasNoOverlap(a))
                        .mapToInt(b -> a.releasedPressure + b.releasedPressure))
                .max().orElse(0);

        System.out.println("Answer: " + bestCombined);

        var endTime = Instant.now();
        System.out.println("Total time: " + Duration.between(startTime, endTime));
    }

    record Pair(Path left, Path right, int totalReleasedPressure) {

    }

    record Path(int releasedPressure, Set<Integer> opened) {
        static final Path EMPTY = new Path(0, Collections.emptySet());

        Path add(int releasedPressure, int valve) {
            var copy = new HashSet<>(this.opened);
            copy.add(valve);
            return new Path(this.releasedPressure + releasedPressure, copy);
        }

        boolean hasNoOverlap(Path other) {
            return Collections.disjoint(opened, other.opened);
        }
    }

    static Set<Path> pathsToOpenValves(
            int[] flowRates,
            int[][] distances,
            State state,
            Map<State, Set<Path>> cache) {
        var cached = cache.get(state);
        if (cached != null)
            return cached;

        var minutesLeft = MAX_MINUTES - state.minute;

        var result = IntStreamEx.range(0, distances[state.current].length)
                .filter(to -> to != state.current && flowRates[to] > 0 && state.isNotOpen(to))
                .filter(to -> minutesLeft - distances[state.current][to] > 0)
                .flatMapToObj(to -> {
                    var dist = distances[state.current][to];
                    var releasedPressure = (minutesLeft - dist) * flowRates[to];

                    var subpaths = pathsToOpenValves(flowRates, distances, state.moveAndOpen(to, dist), cache);

                    return Stream.concat(Stream.of(new Path(releasedPressure, Collections.singleton(to))), subpaths.stream()
                            .map(subpath -> subpath.add(releasedPressure, to)));
                })
                .collect(Collectors.toSet());

        if (result.isEmpty()) {
            result = Set.of(Path.EMPTY);
        }

        cache.put(state, result);

        return result;
    }

    record State(BitSet valves, int current, int minute) {
        State move(int to, int elapsedMinutes) {
            return new State(valves, to, minute + elapsedMinutes);
        }

        State moveAndOpen(int to, int elapsedMinutes) {
            var copy = (BitSet) valves.clone();
            copy.set(to);
            return new State(copy, to, minute + 1 + elapsedMinutes);
        }

        boolean isNotOpen(int valve) {
            return !valves.get(valve);
        }
    }

    record NetworkOfPipes(List<Valve> valves, Map<Valve, List<Tunnel>> tunnels) implements Graph<Valve, Tunnel> {

        static NetworkOfPipes parse(List<String> lines) {
            var valves = lines.stream()
                    .map(line -> line.split("[ ,=;]+"))
                    .map(parts -> new Valve(parts[1], Integer.parseInt(parts[5])))
                    .collect(Collectors.toMap(Valve::label, Function.identity()));

            var tunnels = lines.stream()
                    .map(line -> line.split("[ ,=;]+"))
                    .flatMap(parts -> {
                        var from = valves.get(parts[1]);
                        return IntStream.range(10, parts.length)
                                .mapToObj(i -> new Tunnel(from, valves.get(parts[i])));
                    })
                    .collect(Collectors.groupingBy(Tunnel::from));

            return new NetworkOfPipes(new ArrayList<>(valves.values()), tunnels);
        }

        int[][] precalculateDistances() {
            return valves.stream()
                    .map(source -> valves.stream()
                            .mapToInt(target -> Dijkstra.shortestPath(this, source, target).size())
                            .toArray())
                    .toArray(int[][]::new);
        }

        @Override
        public Stream<Valve> nodes() {
            return valves.stream();
        }

        @Override
        public Stream<Tunnel> edges(Valve node) {
            return tunnels.get(node).stream();
        }

        int start() {
            return (int) StreamEx.of(valves)
                    .indexOf(valve -> valve.label().equals("AA"))
                    .orElseThrow();
        }

        int[] flowRates() {
            return valves.stream().mapToInt(Valve::flowRate).toArray();
        }
    }

    record Valve(String label, int flowRate) implements Node {
    }

    record Tunnel(Valve from, Valve to) implements Edge<Valve> {
        @Override
        public int cost() {
            return 1;
        }
    }
}
