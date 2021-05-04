package com.github.graphextras.algorithms;

import com.google.common.graph.ValueGraph;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.ToDoubleBiFunction;

/**
 * Implements the Iterative Deepening A* algorithm.
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
     * @param heuristicFunc a {@link BiFunction} for computing
     *                      node heuristic.
     */
    public IDAStarPathfinder(@Nonnull final ToDoubleBiFunction<N, N> heuristicFunc) {
        super(heuristicFunc);
    }

    @Override
    public List<N> findPath(@Nonnull final ValueGraph<N, Double> graph, final N source, final N destination) {
        this.graph = Objects.requireNonNull(graph);
        final Deque<N> path = new ArrayDeque<>(graph.nodes().size());
        double threshold = heuristic(source, destination);

        path.push(source);
        while (threshold < Double.MAX_VALUE) {
            threshold = idaSearch(path, destination, 0.0, threshold);
            if (threshold == FOUND) {
                final List<N> foundPath = new ArrayList<>(path);
                Collections.reverse(foundPath);
                return foundPath;
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
     * from the given one.
     */
    private double idaSearch(@Nonnull final Deque<N> path, @Nonnull final N destination, final double currentDepth,
            final double threshold) {
        final N current = Objects.requireNonNull(path.peek());
        final double totalCost = currentDepth + heuristic(current, destination);

        if (current.equals(destination)) {
            return FOUND;
        }

        if (totalCost <= threshold) {
            double minimumCost = Double.MAX_VALUE;
            for (N successor : graph.successors(current)) {
                if (!path.contains(successor)) {
                    path.push(successor);
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
                    path.pop();
                }
            }
            return minimumCost;
        }
        return totalCost;
    }
}
