package com.github.graphextras.algorithms;

import java.util.function.BiFunction;

/**
 * Provides heuristic functions for graphs using arrays of doubles
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
    public static BiFunction<double[], double[], Double> manhattanDistance() {
        return (s, t) -> Math.abs(t[0] - s[0]) + Math.abs(t[1] - s[1]);
    }

    /**
     * Octile distance.
     *
     * @return a function for computing the octile distance.
     */
    public static BiFunction<double[], double[], Double> octileDistance() {
        return (s, t) -> {
            final double dx = Math.abs(s[0] - t[0]);
            final double dy = Math.abs(s[1] - t[1]);
            return Math.max(dx, dy) + .41421356237 * Math.min(dx, dy);
        };
    }

    /**
     * Chebyshev distance.
     *
     * @return a function for computing the Chebyshev distance.
     */
    public static BiFunction<double[], double[], Double> chebyshevDistance() {
        return (s, t) -> {
            final double dx = Math.abs(s[0] - t[0]);
            final double dy = Math.abs(s[1] - t[1]);
            return Math.max(dx, dy);
        };
    }

    /**
     * Euclidean distance.
     *
     * @return a function for computing the euclidean distance.
     */
    public static BiFunction<double[], double[], Double> euclideanDistance() {
        return (s, t) -> {
            final double dx = s[0] - t[0];
            final double dy = s[1] - t[1];
            return Math.sqrt(dx * dx + dy * dy);
        };
    }
}
