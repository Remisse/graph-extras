package com.github.graphextras.algorithms;

/**
 * Interface for algorithms that make use of heuristics.
 *
 * @param <N> type of node
 */
public interface HeuristicPathfinder<N> extends Pathfinder<N> {

    /**
     * Sets a new function for computing estimated distances with regards
     * to the destination.
     *
     * @param newHeuristic the new function for estimating distances.
     */
    void setHeuristic(HeuristicFunction<N> newHeuristic);
}
