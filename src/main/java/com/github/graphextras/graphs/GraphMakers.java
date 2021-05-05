package com.github.graphextras.graphs;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.google.common.graph.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Collection of utility methods for generating graphs.
 */
public final class GraphMakers {

    private GraphMakers() {
    }

    /**
     * Creates a grid-like mutable graph from a set of 2D points.
     * <p>
     * A node will be instantiated for each point in the set with the given
     * {@code nodeFunc} function (e.g. {@code (x, y) -> Pair.of(x, y)}).
     * An edge will be created for every two nodes whose euclidean distance
     * is lower than or equal to the given {@code spacing}.
     * </p>
     *
     * @param points the set of 2D points
     * @param spacing the maximum distance between two points for which an edge
     *                will be created
     * @param nodeFunc function for instantiating nodes from a pair
     *                 of 2D coordinates
     * @param edgeFunc function for creating edges when given two nodes
     * @param <N> type of node
     * @param <E> type of edge
     * @return a grid in the form of a {@link MutableNetwork}.
     */
    public static <N, E> MutableNetwork<N, E> mutableGrid(@Nonnull final Set<double[]> points,  final double spacing,
            @Nonnull final BiFunction<Double, Double, N> nodeFunc, @Nonnull final BiFunction<N, N, E> edgeFunc) {
        requireNonNull(nodeFunc);
        requireNonNull(edgeFunc);
        checkArgument(!requireNonNull(points).isEmpty(), "An empty set of points was supplied.");
        checkArgument(points.stream().allMatch(a -> a.length == 2), "Points must be two-dimensional.");

        final MutableNetwork<N, E> grid = NetworkBuilder.undirected().build();
        points.forEach(p -> grid.addNode(nodeFunc.apply(p[0], p[1])));
        points.forEach(first ->
            points.forEach(second -> {
                if (!Arrays.equals(first, second)) {
                    final N node1 = nodeFunc.apply(first[0], first[1]);
                    final N node2 = nodeFunc.apply(second[0],second[1]);
                    final double dx = first[0] - second[0];
                    final double dy = first[1] - second[1];
                    final double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance <= spacing && !grid.hasEdgeConnecting(node1, node2)) {
                        grid.addEdge(node1, node2, edgeFunc.apply(node1, node2));
                    }
                }
            })
        );
        return grid;
    }

    /**
     * Variant of {@link GraphMakers::mutableGrid} that outputs an immutable
     * graph.
     *
     * @param points the set of 2D points
     * @param spacing the maximum distance between two points for which an edge
     *                will be created
     * @param nodeFunc function for instantiating nodes from a pair
     *                 of 2D coordinates
     * @param edgeFunc function for creating edges when given two nodes
     * @param <N> type of node
     * @param <E> type of edge
     * @return a grid in the form of an {@link ImmutableNetwork}.
     */
    public static <N, E> ImmutableNetwork<N, E> immutableGrid(@Nonnull final Set<double[]> points, final double spacing,
            @Nonnull final BiFunction<Double, Double, N> nodeFunc, @Nonnull final BiFunction<N, N, E> edgeFunc) {
        return ImmutableNetwork.copyOf(mutableGrid(points, spacing, nodeFunc, edgeFunc));
    }
}
