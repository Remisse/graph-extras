package com.github.graphextras.algorithms;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiFunction;

/**
 * Provides heuristic functions for graphs using {@link Pair}s of doubles
 * as nodes.
 */
public final class TwoDimensionalHeuristics {

    private TwoDimensionalHeuristics() {
    }

    /**
     * Manhattan distance.
     *
     * @return a function for computing the Manhattan distance.
     */
    public static BiFunction<Pair<Double, Double>, Pair<Double, Double>, Double> manhattanDistance() {
        return (s, t) -> Math.abs(t.getLeft() - s.getLeft()) + Math.abs(t.getRight() - s.getRight());
    }

    /**
     * Octile distance.
     *
     * @return a function for computing the octile distance.
     */
    public static BiFunction<Pair<Double, Double>, Pair<Double, Double>, Double> octileDistance() {
        return (s, t) -> {
            final double dx = Math.abs(s.getLeft() - t.getLeft());
            final double dy = Math.abs(s.getRight() - t.getRight());
            return Math.max(dx, dy) + .41421356237 * Math.min(dx, dy);
        };
    }

    /**
     * Chebyshev distance.
     *
     * @return a function for computing the Chebyshev distance.
     */
    public static BiFunction<Pair<Double, Double>, Pair<Double, Double>, Double> chebyshevDistance() {
        return (s, t) -> {
            final double dx = Math.abs(s.getLeft() - t.getLeft());
            final double dy = Math.abs(s.getRight() - t.getRight());
            return Math.max(dx, dy);
        };
    }

    /**
     * Euclidean distance.
     *
     * @return a function for computing the euclidean distance.
     */
    public static BiFunction<Pair<Double, Double>, Pair<Double, Double>, Double> euclideanDistance() {
        return (s, t) -> {
            final double dx = s.getLeft() - t.getLeft();
            final double dy = s.getLeft() - t.getRight();
            return Math.sqrt(dx * dx + dy * dy);
        };
    }
}
