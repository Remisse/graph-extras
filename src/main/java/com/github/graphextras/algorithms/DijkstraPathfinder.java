package com.github.graphextras.algorithms;

import com.google.common.graph.Network;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Implements Dijkstra's shortest path algorithm.
 * Delegates the search to {@link AStarPathfinder} by making it use a
 * "zero heuristic" function.
 *
 * @param <N> type of node
 * @param <E> type of edge
 */
public final class DijkstraPathfinder<N, E> implements Pathfinder<N> {

    private final Pathfinder<N> aStar;

    /**
     * Instantiates a new {@code DijkstraPathfinder} object.
     *
     * @param graph the graph on which the searches will be performed
     * @param edgeWeight function for extracting the weights of the given
     *                   graph's edges
     */
    public DijkstraPathfinder(@Nonnull final Network<N, E> graph, @Nonnull final ToDoubleFunction<E> edgeWeight) {
        aStar = new AStarPathfinder<>(graph, edgeWeight, (s, t) -> 0.0);
    }

    @Override
    public List<N> findPath(N source, N destination) {
        return aStar.findPath(source, destination);
    }
}
