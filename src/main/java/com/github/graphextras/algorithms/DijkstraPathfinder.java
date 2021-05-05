package com.github.graphextras.algorithms;

import com.google.common.graph.ValueGraph;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Implements Dijkstra's shortest path algorithm.
 * Delegates the search to {@link AStarPathfinder} by making it use a
 * "zero heuristic" function.
 *
 * @param <N> type of node
 */
public final class DijkstraPathfinder<N> implements Pathfinder<N> {

    private final Pathfinder<N> aStar = new AStarPathfinder<>((s, t) -> 0.0);

    @Override
    public List<N> findPath(@Nonnull final ValueGraph<N, Double> graph, @Nonnull final N source,
            @Nonnull final N destination) {
        return aStar.findPath(graph, source, destination);
    }
}
