package com.github.graphextras.algorithms;

import javax.annotation.Nonnull;
import java.util.function.ToDoubleFunction;

/**
 * Collection of heuristic functions to be used with pathfinding algorithms.
 */
public final class Heuristics {

    private static final double SQRT_2 = .41421356237;

    private Heuristics() {
    }

    /**
     * Manhattan distance.
     *
     * @param x function for retrieving the x coordinate from the given nodes
     * @param y function for retrieving the y coordinate from the given nodes
     * @param <N> type of node
     * @return a function for computing the Manhattan distance.
     */
    public static <N> HeuristicFunction<N> manhattanDistance(@Nonnull final ToDoubleFunction<N> x,
            @Nonnull final ToDoubleFunction<N> y) {
        return (node1, node2) -> Math.abs(x.applyAsDouble(node2) - x.applyAsDouble(node1))
                                + Math.abs(y.applyAsDouble(node2) - y.applyAsDouble(node1));
    }

    /**
     * Octile distance.
     *
     * @param x function for retrieving the x coordinate from the given nodes
     * @param y function for retrieving the y coordinate from the given nodes
     * @param <N> type of node
     * @return a function for computing the octile distance.
     */
    public static <N> HeuristicFunction<N> octileDistance(@Nonnull final ToDoubleFunction<N> x,
            @Nonnull final ToDoubleFunction<N> y) {
        return (node1, node2) -> {
            final double dx = Math.abs(x.applyAsDouble(node2) - x.applyAsDouble(node1));
            final double dy = Math.abs(y.applyAsDouble(node2) - y.applyAsDouble(node1));
            return SQRT_2 * Math.min(dx, dy) + Math.abs(dx - dy);
        };
    }

    /**
     * Chebyshev distance.
     *
     * @param x function for retrieving the x coordinate from the given nodes
     * @param y function for retrieving the y coordinate from the given nodes
     * @param <N> type of node
     * @return a function for computing the Chebyshev distance.
     */
    public static <N> HeuristicFunction<N> chebyshevDistance(@Nonnull final ToDoubleFunction<N> x,
            @Nonnull final ToDoubleFunction<N> y) {
        return (node1, node2) -> {
            final double dx = Math.abs(x.applyAsDouble(node2) - x.applyAsDouble(node1));
            final double dy = Math.abs(y.applyAsDouble(node2) - y.applyAsDouble(node1));
            return Math.max(dx, dy);
        };
    }

    /**
     * Euclidean distance.
     *
     * @param x function for retrieving the x coordinate from the given nodes
     * @param y function for retrieving the y coordinate from the given nodes
     * @param <N> type of node
     * @return a function for computing the euclidean distance.
     */
    public static <N> HeuristicFunction<N> euclideanDistance(@Nonnull final ToDoubleFunction<N> x,
            @Nonnull final ToDoubleFunction<N> y) {
        return (node1, node2) -> {
            final double dx = x.applyAsDouble(node2) - x.applyAsDouble(node1);
            final double dy = y.applyAsDouble(node2) - y.applyAsDouble(node1);
            return Math.sqrt(dx * dx + dy * dy);
        };
    }
}
