package com.github.graphextras.algorithms;

import com.google.common.graph.Network;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * Implements the Iterative Deepening A* algorithm.
 * <br>
 * See {@link Heuristics} for a set of predefined heuristic functions.
 * <p>
 * Please note that, depending on the graph's size, this algorithm can be extremely slow.
 * It should only be used if it is critical that memory consumption be reduced to a
 * minimum.
 * </p>
 *
 * @param <N> type of node
 * @param <E> type of edge
 */
public final class IDAStarPathfinder<N, E> extends AbstractHeuristicPathfinder<N, E> {

    private static final double FOUND = -1.0;

    /**
     * Instantiates a new {@code IDAStarPathfinder} with the given
     * heuristic function.
     *
     * @param graph the graph on which the searches will be performed.
     * @param edgeWeight function for extracting the weights of the given
     *                   graph's edges
     * @param heuristicFunc function for estimating the distance between a
     *                      node and the destination.
     */
    public IDAStarPathfinder(@Nonnull final Network<N, E> graph, @Nonnull final ToDoubleFunction<E> edgeWeight,
            @Nonnull final HeuristicFunction<N> heuristicFunc) {
        super(graph, edgeWeight, heuristicFunc);
    }

    @Override
    public List<N> findPath(@Nonnull final N source, @Nonnull final N destination) {
        final Deque<N> path = new ArrayDeque<>();
        double threshold = heuristic(source, destination);

        path.addLast(source);
        do {
            threshold = idaSearch(path, destination, 0.0, threshold);
            if (threshold == FOUND) {
                return new ArrayList<>(path);
            }
        } while (threshold < Double.MAX_VALUE);
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
            for (final N successor : getGraph().successors(current)) {
                if (!path.contains(successor)) {
                    path.addLast(successor);
                    double pathCost = idaSearch(
                            path,
                            destination,
                            currentDepth + weightOf(getGraph().edgeConnecting(current, successor).orElseThrow()),
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
