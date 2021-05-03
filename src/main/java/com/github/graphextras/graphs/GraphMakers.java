package com.github.graphextras.graphs;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.graph.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.Precision;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * Provides misc utility methods for generating graphs.
 */
public final class GraphMakers {

    private static final double DEFAULT_WEIGHT = 1.0;
    private static final double EPSILON = 0.000001;

    private GraphMakers() {
    }

    /**
     *
     * @param points
     * @param spacing
     * @param pointCreator
     * @param <N>
     * @return
     */
    public static <N> MutableValueGraph<N, Double> mutableGridFrom(@Nonnull final Set<double[]> points,
           final double spacing, @Nonnull final BiFunction<Double, Double, N> pointCreator) {
        Objects.requireNonNull(pointCreator);
        checkArgument(!Objects.requireNonNull(points).isEmpty(), "An empty set of points was supplied.");

        final MutableValueGraph<N, Double> graph = ValueGraphBuilder.undirected().build();
        points.forEach(p -> graph.addNode(pointCreator.apply(p[0], p[1])));
        points.forEach(first ->
            points.forEach(second -> {
                final double dx = first[0] - second[0];
                final double dy = first[1] - second[1];
                final double distance = Math.sqrt(dx * dx + dy * dy);
                if (Precision.compareTo(distance, spacing, EPSILON) == 0) {
                    graph.putEdgeValue(
                        pointCreator.apply(first[0], first[1]),
                        pointCreator.apply(second[0], second[1]),
                        distance
                    );
                }
            }));
        return graph;
    }

    /**
     *
     * @param points
     * @param spacing
     * @param pointCreator
     * @param <N>
     * @return
     */
    public static <N> ImmutableValueGraph<N, Double> immutableGridFrom(@Nonnull final Set<double[]> points,
            final double spacing, @Nonnull final BiFunction<Double, Double, N> pointCreator) {
        return ImmutableValueGraph.copyOf(mutableGridFrom(points, spacing, pointCreator));
    }

    /**
     *
     * @param simpleGraph
     * @param <N>
     * @return
     */
    public static <N> ImmutableValueGraph<N, Double> simpleToImmutableValueGraph(@Nonnull final Graph<N> simpleGraph) {
        MutableValueGraph<N, Double> graph;

        if (simpleGraph.isDirected()) {
            graph = ValueGraphBuilder.directed().build();
        } else {
            graph = ValueGraphBuilder.undirected().build();
        }

        simpleGraph.nodes().forEach(graph::addNode);
        simpleGraph.edges().forEach(e -> graph.putEdgeValue(e.nodeU(), e.nodeV(), DEFAULT_WEIGHT));

        return ImmutableValueGraph.copyOf(graph);
    }
}
