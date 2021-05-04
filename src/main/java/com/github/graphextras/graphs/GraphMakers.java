package com.github.graphextras.graphs;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.graph.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.ToDoubleFunction;

/**
 * Collection of utility methods for generating graphs.
 */
public final class GraphMakers {

    private static final double DEFAULT_WEIGHT = 1.0;
    private static final double EPSILON = 0.000001;

    private GraphMakers() {
    }

    /**
     * Creates a grid-like mutable graph from a set of 2D points.
     * <p>
     * A node will be instantiated for each point in the set with the given
     * {@code pointCreator} function (e.g. {@code (x, y) -> Pair.of(x, y)}).
     * An edge will be created for every two nodes whose euclidean distance
     * is equal to the given {@code spacing}.
     * </p>
     *
     * @param points the set of 2D points
     * @param spacing the distance between two points for which an edge will be created
     * @param pointCreator the function used for instantiating nodes from a pair
     *                     of 2D coordinates
     * @param <N> type of node
     * @return a grid in the form of a {@link MutableValueGraph}.
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
                if (Math.abs(distance - spacing) <= EPSILON) {
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
     * Creates a grid-like immutable graph from a set of 2D points.
     * <p>
     * A node will be instantiated for each point in the set with the given
     * {@code pointCreator} function (e.g. {@code (x, y) -> Pair.of(x, y)}).
     * An edge will be created for every two nodes whose euclidean distance
     * is equal to the given {@code spacing}.
     * </p>
     *
     * @param points the set of 2D points
     * @param spacing the distance between two points for which an edge will be created
     * @param pointCreator the function used for instantiating nodes from a pair
     *                     of 2D coordinates
     * @param <N> type of node
     * @return a grid in the form of an {@link ImmutableValueGraph}.
     */
    public static <N> ImmutableValueGraph<N, Double> immutableGridFrom(@Nonnull final Set<double[]> points,
            final double spacing, @Nonnull final BiFunction<Double, Double, N> pointCreator) {
        return ImmutableValueGraph.copyOf(mutableGridFrom(points, spacing, pointCreator));
    }

    /**
     * Converts a {@link Graph} to a {@link MutableValueGraph} whose edges will
     * all have weight equal to 1.0.
     *
     * @param simpleGraph the graph to be converted
     * @param <N> type of node
     * @return a {@link MutableValueGraph} with the same sets of nodes and edges of
     * the given {@link Graph}.
     */
    public static <N> MutableValueGraph<N, Double> simpleToMutableValueGraph(@Nonnull final Graph<N> simpleGraph) {
        MutableValueGraph<N, Double> graph;

        if (simpleGraph.isDirected()) {
            graph = ValueGraphBuilder.directed().allowsSelfLoops(simpleGraph.allowsSelfLoops()).build();
        } else {
            graph = ValueGraphBuilder.undirected().allowsSelfLoops(simpleGraph.allowsSelfLoops()).build();
        }

        simpleGraph.nodes().forEach(graph::addNode);
        simpleGraph.edges().forEach(e -> graph.putEdgeValue(e.nodeU(), e.nodeV(), DEFAULT_WEIGHT));

        return graph;
    }

    /**
     * Converts a {@link Graph} to an {@link ImmutableValueGraph} whose edges will
     * all have weight equal to 1.0.
     *
     * @param simpleGraph the graph to be converted
     * @param <N> type of node
     * @return an {@link ImmutableValueGraph} with the same sets of nodes and edges of
     * the given {@link Graph}.
     */
    public static <N> ImmutableValueGraph<N, Double> simpleToImmutableValueGraph(@Nonnull final Graph<N> simpleGraph) {
        return ImmutableValueGraph.copyOf(simpleToMutableValueGraph(simpleGraph));
    }

    /**
     * Converts a {@link Network} to a {@link MutableValueGraph} whose edges
     * will have a weight assigned by the given {@code weightFunc}.
     *
     * @param network the network to be converted
     * @param <N> type of node
     * @return a {@link MutableValueGraph} with the same sets of nodes and edges of
     * the given {@link Network}.
     */
    public static <N, E> MutableValueGraph<N, Double> networkToMutableValueGraph(@Nonnull final Network<N, E> network,
            @Nonnull final ToDoubleFunction<E> weightFunc) {
        MutableValueGraph<N, Double> graph;

        if (network.isDirected()) {
            graph = ValueGraphBuilder.directed().allowsSelfLoops(network.allowsSelfLoops()).build();
        } else {
            graph = ValueGraphBuilder.undirected().allowsSelfLoops(network.allowsSelfLoops()).build();
        }

        network.nodes().forEach(graph::addNode);
        network.nodes().forEach(node -> {
            network.successors(node).forEach(successor -> {
                graph.putEdgeValue(
                        node,
                        successor,
                        weightFunc.applyAsDouble(network.edgeConnecting(node, successor).orElseThrow()));
            });
        });
        return graph;
    }

    /**
     * Converts a {@link Network} to an {@link ImmutableValueGraph} whose edges
     * will have a weight assigned by the given {@code weightFunc}.
     *
     * @param network the network to be converted
     * @param <N> type of node
     * @return an {@link ImmutableValueGraph} with the same sets of nodes and edges of
     * the given {@link Network}.
     */
    public static <N, E> ImmutableValueGraph<N, Double> networkToImmutableValueGraph(@Nonnull final Network<N, E> network,
            @Nonnull final ToDoubleFunction<E> weightFunc) {
        return ImmutableValueGraph.copyOf(networkToMutableValueGraph(network, weightFunc));
    }
}
