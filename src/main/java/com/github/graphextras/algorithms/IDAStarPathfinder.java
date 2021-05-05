package com.github.graphextras.algorithms;

import com.google.common.graph.ValueGraph;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * Implements the Iterative Deepening A* algorithm.
 * <br>
 * Please note that, depending on the graph's size, this algorithm can be extremely slow.
 * It should only be used if it is critical that memory consumption be reduced to a
 * minimum.
 *
 * @param <N> type of node
 */
public final class IDAStarPathfinder<N> extends AbstractHeuristicPathfinder<N> {

    private static final double FOUND = -1.0;
    private ValueGraph<N, Double> graph;

    /**
     * Instantiates a new {@code IDAStarPathfinder} with the given
     * heuristic function.
     *
     * @param heuristicFunc a {@link BiFunction} for estimating distances.
     */
    public IDAStarPathfinder(@Nonnull final ToDoubleBiFunction<N, N> heuristicFunc) {
        super(heuristicFunc);
    }

    @Override
    public List<N> findPath(@Nonnull final ValueGraph<N, Double> graph, @Nonnull final N source,
            @Nonnull final N destination) {
        this.graph = Objects.requireNonNull(graph);
        final Deque<N> path = new ArrayDeque<>();
        double threshold = heuristic(source, destination);

        path.addLast(source);
        while (threshold < Double.MAX_VALUE) {
            threshold = idaSearch(path, destination, 0.0, threshold);
            if (threshold == FOUND) {
                return new ArrayList<>(path);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Performs a DFS starting from the given path and stops either if the initial branch cost
     * exceeds the given threshold, or if the destination has been found, or when the branch
     * has been fully explored.
     *
     * @param path         the current path being explored
     * @param destination  the destination node
     * @param currentDepth the cost of this branch so far
     * @param threshold    the current cost limit
     * @return either {@code -1.0} if the destination is found on the given path, or the total cost of the
     * given path if it exceeds the threshold, or the lowest cost found among all paths branching
     * from the given one (will become the new threshold).
     */
    private double idaSearch(final Deque<N> path, final N destination, final double currentDepth, final double threshold) {
        final N current = Objects.requireNonNull(path.peekLast());
        final double totalCost = currentDepth + heuristic(current, destination);

        if (current.equals(destination)) {
            return FOUND;
        }

        if (totalCost <= threshold) {
            double minimumCost = Double.MAX_VALUE;
            for (final N successor : graph.successors(current)) {
                if (!path.contains(successor)) {
                    path.addLast(successor);
                    double pathCost = idaSearch(
                            path,
                            destination,
                            currentDepth + graph.edgeValue(current, successor).orElseThrow(),
                            threshold
                    );
                    if (pathCost == FOUND) {
                        return FOUND;
                    }
                    if (pathCost < minimumCost) {
                        minimumCost = pathCost;
                    }
                    path.removeLast();
                }
            }
            /*
             * Path seems promising, increase the threshold.
             */
            return minimumCost;
        }
        /*
         * Threshold exceeded, discard this path.
         */
        return totalCost;
    }
}
