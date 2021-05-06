package com.github.graphextras.algorithms;

import com.google.common.graph.Network;

import javax.annotation.Nonnull;
import java.util.function.ToDoubleFunction;

import static java.util.Objects.requireNonNull;

/**
 * Base class for {@link HeuristicPathfinder} algorithms.
 *
 * @param <N> type of node.
 */
abstract class AbstractHeuristicPathfinder<N, E> extends AbstractPathfinder<N, E> implements HeuristicPathfinder<N> {

    private HeuristicFunction<N> heuristicFunc;

    protected AbstractHeuristicPathfinder(@Nonnull final Network<N, E> graph,
            @Nonnull final ToDoubleFunction<E> edgeWeight, @Nonnull final HeuristicFunction<N> heuristicFunc) {
        super(graph, edgeWeight);
        this.heuristicFunc = requireNonNull(heuristicFunc);
    }

    /**
     * Applies the heuristic function for estimating the distance between
     * a node and the destination.
     *
     * @return estimated distance from the given {@code node} to the {@code target}
     * using the currently assigned heuristic function.
     */
    protected double heuristic(final N node, final N target) {
        return heuristicFunc.apply(node, target);
    }

    @Override
    public void setHeuristic(@Nonnull final HeuristicFunction<N> newHeuristic) {
        this.heuristicFunc = requireNonNull(newHeuristic);
    }
}
