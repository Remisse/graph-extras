package com.github.graphextras.commons;

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
public final class GraphUtils {

    private static final double DEFAULT_WEIGHT = 1.0;
    private static final double EPSILON = 0.000001;

    private GraphUtils() {
    }

    /**
     *
     * @param nodeCount
     * @param density
     * @param nodeCreator
     * @param <N>
     * @return
     */
    public static <N> ImmutableValueGraph<N, Double> randomGraph(final int nodeCount, final double density,
            @Nonnull final BiFunction<Double, Double, N> nodeCreator) {
        checkArgument(nodeCount > 0);
        checkArgument(density > 0.0 && density <= 1.0, "Density must be a value ranging from" +
                "0.0 to 1.0.");

        final MutableValueGraph<N, Double> graph = ValueGraphBuilder.undirected().allowsSelfLoops(true).build();
        for (int i = 0; i < nodeCount; i++) {
            N tentativeNode;
            do {
                tentativeNode = nodeCreator.apply(
                        ThreadLocalRandom.current().nextDouble(),
                        ThreadLocalRandom.current().nextDouble());
            }
            while (graph.nodes().contains(tentativeNode));
            graph.addNode(tentativeNode);
        }

        graph.nodes().forEach(p1 ->
            graph.nodes().forEach(p2 -> {
                final double probability = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
                if (probability < density) {
                    graph.putEdgeValue(p1, p2, DEFAULT_WEIGHT);
                }
            }));
        return ImmutableValueGraph.copyOf(graph);
    }

    /**
     *
     * @param points
     * @param spacing
     * @param nodeCreator
     * @param <N>
     * @param <V>
     * @return
     */
    public static <N, V extends Number> MutableValueGraph<N, V> mutableGridFrom(@Nonnull final Set<Pair<V, V>> points,
           @Nonnull final V spacing, @Nonnull final BiFunction<V, V, N> nodeCreator) {
        Objects.requireNonNull(nodeCreator);
        checkArgument(!Objects.requireNonNull(points).isEmpty(), "An empty set of points was supplied.");

        final MutableValueGraph<N, V> graph = ValueGraphBuilder.undirected().build();
        points.forEach(p -> graph.addNode(nodeCreator.apply(p.getLeft(), p.getRight())));
        points.forEach(first ->
            points.forEach(second -> {
                final double dx = first.getLeft().doubleValue() - second.getLeft().doubleValue();
                final double dy = first.getRight().doubleValue() - second.getRight().doubleValue();
                if (Precision.compareTo(Math.sqrt(dx * dx + dy * dy), spacing.doubleValue(), EPSILON) == 0) {
                    graph.putEdgeValue(
                            nodeCreator.apply(first.getLeft(), first.getRight()),
                            nodeCreator.apply(second.getLeft(), second.getRight()),
                            spacing
                    );
                }
            }));
        return graph;
    }

    /**
     *
     * @param points
     * @param spacing
     * @param nodeCreator
     * @param <N>
     * @param <V>
     * @return
     */
    public static <N, V extends Number> ImmutableValueGraph<N, V> immutableGridFrom(@Nonnull final Set<Pair<V, V>> points,
            final V spacing, @Nonnull final BiFunction<V, V, N> nodeCreator) {
        return ImmutableValueGraph.copyOf(mutableGridFrom(points, spacing, nodeCreator));
    }

    /**
     *
     * @param simpleGraph
     * @param <N>
     * @return
     */
    public static <N> ValueGraph<N, Double> simpleToImmutableValueGraph(@Nonnull final Graph<N> simpleGraph) {
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
