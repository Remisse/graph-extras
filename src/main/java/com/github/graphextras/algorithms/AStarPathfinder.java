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

    // Open set. Nodes are ordered by their fScore.
    private final PriorityQueue<ObjectDoubleImmutablePair<N>> fringe = new PriorityQueue<>(
            Comparator.comparingDouble(ObjectDoublePair::valueDouble));
    // Closed set.
    private final Set<N> visited = new HashSet<>();
    // Parents tree. Used to reconstruct the path once the algorithm will
    // end its search.
    private final Map<N, N> parents = new HashMap<>();
    private final ObjectDoubleMap<N> gScore = new ObjectDoubleHashMap<>();

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

    private void initialize(final N source, final N destination) {
        visited.clear();
        gScore.clear();
        fringe.clear();

        parents.put(source, source);
        gScore.put(source, 0.0);
        fringe.add(ObjectDoubleImmutablePair.of(source, heuristic(source, destination)));
    }

    @Override
    public List<N> findPath(@Nonnull final ValueGraph<N, Double> graph, @Nonnull final N source,
            @Nonnull final N destination) {
        Objects.requireNonNull(graph);
        initialize(Objects.requireNonNull(source), Objects.requireNonNull(destination));

        while (!fringe.isEmpty()) {
            final N current = fringe.poll().left();

            if (current.equals(destination)) {
                return reconstructPath(parents, destination);
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
        return Collections.emptyList();
    }
}
