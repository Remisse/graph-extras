package com.github.graphextras.algorithms;

import java.util.function.BiFunction;

/**
 * Interface for algorithms that make use of heuristics.
 *
 * @param <N> type of node
 * @param <V> type of value associated to nodes. It must be a {@link Number}.
 */
public interface HeuristicPathfinder<N, V extends Number> extends Pathfinder<N, V> {

    /**
     * Sets a new function for computing node heuristics.
     *
     * @param newHeuristic the new function for computing node heuristics.
     */
    void setHeuristic(BiFunction<N, N, V> newHeuristic);
}
