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

    protected AbstractHeuristicPathfinder(@Nonnull ToDoubleBiFunction<N, N> heuristicFunc) {
        this.heuristicFunc = Objects.requireNonNull(heuristicFunc);
    }

    /**
     * Provides subclasses access to the function for computing node heuristic.
     *
     * @return the heuristic value of the given {@code node} with respect to
     * the {@code target}.
     */
    protected double heuristic(final N node, final N target) {
        return heuristicFunc.applyAsDouble(node, target);
    }

    @Override
    public void setHeuristic(@Nonnull ToDoubleBiFunction<N, N> newHeuristic) {
        this.heuristicFunc = Objects.requireNonNull(newHeuristic);
    }
}
