package com.github.graphextras.algorithms;

import com.carrotsearch.hppc.ObjectDoubleHashMap;
import com.carrotsearch.hppc.ObjectDoubleMap;
import com.google.common.graph.ValueGraph;
import it.unimi.dsi.fastutil.objects.ObjectDoubleImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectDoublePair;

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
        final PriorityQueue<ObjectDoubleImmutablePair<N>> fringe = new PriorityQueue<>(graph.nodes().size(),
                Comparator.comparingDouble(ObjectDoublePair::valueDouble));
        // The closed set.
        final Set<N> visited = new HashSet<>(graph.nodes().size());
        final ObjectDoubleMap<N> gScore = new ObjectDoubleHashMap<>();
        final Map<N, N> parents = new HashMap<>();

        // Initializing
        parents.put(source, source);
        gScore.put(source, 0.0);
        fringe.add(ObjectDoubleImmutablePair.of(source, heuristic(source, destination)));

        while (!fringe.isEmpty()) {
            final N current = fringe.poll().left();

            if (current.equals(destination)) {
                fringe.clear(); // Path found, clear the queue to exit loop
            } else if (!visited.contains(current)){
                visited.add(current);
                graph.successors(current).forEach(successor -> {
                    final double tentativeGScore = gScore.get(current) + graph.edgeValue(current, successor).orElseThrow();

                    if (tentativeGScore < gScore.getOrDefault(successor, Double.MAX_VALUE)) {
                        parents.put(successor, current);
                        gScore.put(successor, tentativeGScore);
                        fringe.add(ObjectDoubleImmutablePair.of(
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
