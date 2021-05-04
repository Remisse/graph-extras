package com.github.graphextras.algorithms;

import com.google.common.graph.ValueGraph;

import java.util.List;

/**
 * Models algorithms for finding shortest paths on Google Guava's {@link ValueGraph}s.
 *
 * @param <N> type of node
 */
public interface Pathfinder<N> {

    /**
     * Finds the shortest path from a given source node to a given destination.
     * @param graph the {@link ValueGraph} on which the search will
     *              be performed
     * @param source the starting node
     * @param destination the destination node
     * @return a list of nodes representing the path from source to destination.
     * If no path is found, an empty list will be returned.
     */
    List<N> findPath(ValueGraph<N, Double> graph, N source, N destination);
}
