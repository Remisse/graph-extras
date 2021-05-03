package com.github.graphextras.algorithms;

import com.google.common.graph.ValueGraph;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiFunction;

import static com.github.graphextras.algorithms.Algorithms.reconstructPath;

/**
 * Implements the A* search algorithm.
 *
 * @param <N> type of node
 * @param <V> type of value associated to nodes. It must be a {@link Number}.
 */
public final class AStarPathfinder<N, V extends Number> extends AbstractHeuristicPathfinder<N, V> {

    private final Map<N, N> parents = new HashMap<>();

    /**
     * Instantiates a new {@link AStarPathfinder} object with
     * the given heuristic function.
     *
     * @param heuristicFunc a {@link BiFunction} for computing
     *                      node heuristic.
     */
    public AStarPathfinder(@Nonnull final BiFunction<N, N, V> heuristicFunc) {
        super(heuristicFunc);
    }

    @Override
    public List<N> findPath(@Nonnull final ValueGraph<N, V> graph, @Nonnull final N source,
                            @Nonnull final N destination) {
        // The open set. Made of nodes and their relative fScore (f(N) = g(N) + h(N))
        final PriorityQueue<Pair<N, Double>> fringe = new PriorityQueue<>(
                graph.nodes().size(),
                Comparator.comparingDouble(Pair::getRight)
        );
        final Map<N, Double> gScore = new HashMap<>();

        // Initializing
        parents.put(source, source);
        gScore.put(source, 0.0);
        fringe.add(Pair.of(source, getHeuristic().apply(source, destination).doubleValue()));

        while (!fringe.isEmpty()) {
            final N current = fringe.poll().getLeft();

            if (current.equals(destination)) {
                fringe.clear(); // Path found, clear the queue to exit loop
            } else {
                graph.successors(current).forEach(successor -> {
                    final double tentativeGScore
                            = gScore.get(current) + graph.edgeValue(current, successor).orElseThrow().doubleValue();

                    if (tentativeGScore < gScore.getOrDefault(successor, Double.MAX_VALUE)) {
                        parents.put(successor, current);
                        gScore.put(successor, tentativeGScore);
                        fringe.add(Pair.of(
                                successor,
                                tentativeGScore + getHeuristic().apply(successor, destination).doubleValue())
                        );
                    }
                });
            }
        }
        return reconstructPath(parents, destination);
    }
}
