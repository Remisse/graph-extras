package com.github.graphextras.algorithms;

import com.google.common.graph.Network;

import javax.annotation.Nonnull;
import java.util.function.ToDoubleFunction;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Base class for pathfinding algorithms.
 *
 * @param <N> type of node
 * @param <E> type of edge
 */
abstract class AbstractPathfinder<N, E> implements Pathfinder<N> {

    private final Network<N, E> graph;
    private final ToDoubleFunction<E> edgeWeight;

    protected AbstractPathfinder(@Nonnull final Network<N, E> graph, @Nonnull final ToDoubleFunction<E> edgeWeight) {
        checkArgument(!requireNonNull(graph).allowsParallelEdges(),
                "Graphs allowing parallel edges are not supported.");
        this.graph = graph;
        this.edgeWeight = requireNonNull(edgeWeight);
    }

    /**
     * Returns the graph with which this pathfinder was instantiated.
     *
     * @return the graph with which this pathfinder was instantiated
     */
    protected Network<N, E> getGraph() {
        return graph;
    }

    /**
     * Retrieves the given edge's weight by using the function
     * provided when this pathfinder was instantiated.
     *
     * @param edge the edge whose weight is to be retrieved
     * @return the weight of the given edge.
     */
    protected double weightOf(final E edge) {
        return edgeWeight.applyAsDouble(edge);
    }
}
