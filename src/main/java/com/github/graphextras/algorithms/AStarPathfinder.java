package com.github.graphextras.algorithms;

import com.carrotsearch.hppc.ObjectDoubleHashMap;
import com.carrotsearch.hppc.ObjectDoubleMap;
import com.google.common.graph.Network;
import it.unimi.dsi.fastutil.objects.ObjectDoubleImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectDoublePair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.*;

import static com.github.graphextras.algorithms.Pathfinders.reconstructPath;
import static java.util.Objects.requireNonNull;

/**
 * Implements the A* search algorithm.
 * <br>
 * See {@link Heuristics} for a set of predefined heuristic functions.
 *
 * @param <N> type of node
 * @param <E> type of edge
 */
public final class AStarPathfinder<N, E> extends AbstractHeuristicPathfinder<N, E> {

    /*
     * Open set. Nodes are ordered by their fScore.
     */
    private final PriorityQueue<ObjectDoubleImmutablePair<N>> fringe = new PriorityQueue<>(
            Comparator.comparingDouble(ObjectDoublePair::valueDouble));
    /*
     * Closed set.
     */
    private final Set<N> visited = new HashSet<>();
    /*
     * Parents tree. Used to reconstruct the path when the algorithm
     * ends its search.
     */
    private final Map<N, N> parents = new HashMap<>();
    /*
     * Map containing gScores for all nodes.
     */
    private final ObjectDoubleMap<N> gScore = new ObjectDoubleHashMap<>();

    /**
     * Instantiates a new {@link AStarPathfinder} object.
     *
     * @param graph the graph on which the searches will be performed.
     * @param edgeWeight function for extracting the weights of the given
     *                   graph's edges
     * @param heuristicFunc function for estimating the distance between a
     *                      node and the destination.
     */
    public AStarPathfinder(@Nonnull final Network<N, E> graph, @Nonnull final ToDoubleFunction<E> edgeWeight,
            @Nonnull final ToDoubleBiFunction<N, N> heuristicFunc) {
        super(graph, edgeWeight, heuristicFunc);
    }

    /**
     * Clears the contents of preexisting collections and
     * performs preliminary initializations.
     *
     * @param source the source node
     * @param destination the destination node
     */
    private void initialize(final N source, final N destination) {
        visited.clear();
        gScore.clear();
        fringe.clear();

        parents.put(source, source);
        gScore.put(source, 0.0);
        fringe.add(ObjectDoubleImmutablePair.of(source, heuristic(source, destination)));
    }

    @Override
    public List<N> findPath(@Nonnull final N source, @Nonnull final N destination) {
        initialize(requireNonNull(source), requireNonNull(destination));

        while (!fringe.isEmpty()) {
            final N current = fringe.poll().left();

            if (current.equals(destination)) {
                return reconstructPath(parents, destination);
            }
            if (!visited.contains(current)){
                visited.add(current);
                getGraph().successors(current).forEach(successor -> {
                    final double tentativeGScore = gScore.get(current)
                            + weightOf(getGraph().edgeConnecting(current, successor).orElseThrow());

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
