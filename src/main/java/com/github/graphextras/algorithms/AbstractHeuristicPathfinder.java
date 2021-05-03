package com.github.graphextras.algorithms;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.BiFunction;

public abstract class AbstractHeuristicPathfinder<N, V extends Number> implements HeuristicPathfinder<N, V> {

    private BiFunction<N, N, V> heuristicFunc;

    protected AbstractHeuristicPathfinder(@Nonnull BiFunction<N, N, V> heuristicFunc) {
        this.heuristicFunc = Objects.requireNonNull(heuristicFunc);
    }

    /**
     * Provides subclasses access to the function for computing node heuristic.
     *
     * @return the heuristic function currently used by this pathfinder.
     */
    protected BiFunction<N, N, V> getHeuristic() {
        return this.heuristicFunc;
    }

    @Override
    public void setHeuristic(@Nonnull BiFunction<N, N, V> newHeuristic) {
        this.heuristicFunc = Objects.requireNonNull(newHeuristic);
    }
}
