package com.github.graphextras.algorithms;

import com.google.common.graph.ValueGraph;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.*;

import static com.github.graphextras.algorithms.Algorithms.reconstructPath;

/**
 * Implements the A* search algorithm.
 *
 * @param <N> type of node
 */
public final class AStarPathfinder<N> extends AbstractHeuristicPathfinder<N> {

    private final Map<N, N> parents = new HashMap<>();

    /**
     * Instantiates a new {@link AStarPathfinder} object with
     * the given heuristic function.
     *
     * @param heuristicFunc a {@link BiFunction} for computing
     *                      node heuristic.
     */
    public AStarPathfinder(@Nonnull final ToDoubleBiFunction<N, N> heuristicFunc) {
        super(heuristicFunc);
    }

    @Override
    public List<N> findPath(ValueGraph<N, Double> graph, N source, N destination) {
        // The open set. Nodes are ordered by their fScore.
        final PriorityQueue<Pair<N, Double>> fringe = new PriorityQueue<>(graph.nodes().size(),
                Comparator.comparingDouble(Pair::getRight));
        // The closed set.
        final Set<N> visited = new HashSet<>(graph.nodes().size());
        final Map<N, Double> gScore = new HashMap<>(graph.nodes().size());

        // Initializing
        parents.put(source, source);
        gScore.put(source, 0.0);
        fringe.add(Pair.of(source, heuristic(source, destination)));

        while (!fringe.isEmpty()) {
            final N current = fringe.poll().getLeft();

            if (current.equals(destination)) {
                fringe.clear(); // Path found, clear the queue to exit loop
            } else if (!visited.contains(current)){
                visited.add(current);
                graph.successors(current).forEach(successor -> {
                    final double tentativeGScore = gScore.get(current) + graph.edgeValue(current, successor).orElseThrow();

                    if (tentativeGScore < gScore.getOrDefault(successor, Double.MAX_VALUE)) {
                        parents.put(successor, current);
                        gScore.put(successor, tentativeGScore);
                        fringe.add(Pair.of(
                                successor,
                                tentativeGScore + heuristic(successor, destination))
                        );
                    }
                });
            }
        }
        return reconstructPath(parents, destination);
    }
}
