package com.github.graphextras.algorithms;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.ToDoubleBiFunction;

/**
 * Base class for {@link HeuristicPathfinder} algorithms.
 *
 * @param <N> type of node.
 */
abstract class AbstractHeuristicPathfinder<N> implements HeuristicPathfinder<N> {

    private ToDoubleBiFunction<N, N> heuristicFunc;

    protected AbstractHeuristicPathfinder(@Nonnull final ToDoubleBiFunction<N, N> heuristicFunc) {
        this.heuristicFunc = Objects.requireNonNull(heuristicFunc);
    }

    /**
     * Provides subclasses access to the function for computing node heuristics.
     *
     * @return estimated distance from the given {@code node} to the {@code target}
     * using the currently assigned heuristic function.
     */
    protected double heuristic(final N node, final N target) {
        return heuristicFunc.applyAsDouble(node, target);
    }

    @Override
    public void setHeuristic(@Nonnull final ToDoubleBiFunction<N, N> newHeuristic) {
        this.heuristicFunc = Objects.requireNonNull(newHeuristic);
    }
}
