package com.wetjens.aoc2022.library.graphs;

import java.util.*;

public final class Dijkstra {

    public static <N, E extends Edge<N>> List<E> shortestPath(Graph<N, E> graph, N source, N target) {
        var unvisited = new HashSet<N>();
        var dist = new HashMap<N, Integer>();
        var prev = new HashMap<N, E>();

        graph.nodes().forEach(node -> {
            dist.put(node, Integer.MAX_VALUE);
            unvisited.add(node);
        });

        dist.put(source, 0);

        while (!unvisited.isEmpty()) {
            var current = unvisited.stream()
                    .min(Comparator.comparingInt(dist::get))
                    .orElseThrow(() -> new IllegalStateException("no more unvisited nodes"));

            if (current.equals(target)) {
                // found
                var path = new LinkedList<E>();
                // backtrack
                var from = target;
                while (!from.equals(source)) {
                    var edge = prev.get(from);
                    path.addFirst(edge);
                    from = edge.from();
                }
                return path;
            }

            unvisited.remove(current);

            graph.edges(current)
//                    .filter(edge -> unvisited.contains(edge.to()))
                    .forEach(edge -> {
                        var to = edge.to();

                        var alt = dist.get(current) + edge.cost();

                        if (alt < dist.get(to)) {
                            dist.put(to, alt);
                            prev.put(to, edge);
                        }
                    });
        }

        throw new IllegalStateException("no path from " + source + " to " + target);
    }

}
