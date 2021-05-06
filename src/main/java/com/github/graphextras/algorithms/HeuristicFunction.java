package com.github.graphextras.algorithms;

/**
 * Functional interface for modelling heuristic functions.
 *
 * @param <N> type of node
 */
@FunctionalInterface
public interface HeuristicFunction<N> {

    /**
     * Applies this heuristic function to the given nodes.
     *
     * @param node1 first node
     * @param node2 second node
     * @return the estimated distance between the given nodes.
     */
    double apply(N node1, N node2);
}
