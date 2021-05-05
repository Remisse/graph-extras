package com.github.graphextras.algorithms;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Collection of utility methods used by certain pathfinding algorithms.
 */
final class Pathfinders {

    private Pathfinders() {
    }

    /**
     * Reconstructs the path by walking the given parents tree
     * from destination to source.
     *
     * @param parents the parents tree
     * @param target the destination node
     * @return the path from source to destination
     */
    public static <N> List<N> reconstructPath(@Nonnull final Map<N, N> parents, @Nonnull final N target) {
        if (parents.get(target) == null) {
            return Collections.emptyList();
        }
        List<N> path = new ArrayList<>(List.of(target));
        N parent = parents.get(target);
        while (!parent.equals(path.get(path.size() - 1))) {
            path.add(parent);
            parent = parents.get(parent);
        }
        Collections.reverse(path);
        return path;
    }
}
