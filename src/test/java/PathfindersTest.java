import com.github.graphextras.algorithms.*;
import com.google.common.graph.*;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.doubles.DoubleDoublePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.graphextras.algorithms.Heuristics.euclideanDistance;

@SuppressWarnings("SuspiciousNameCombination")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PathfindersTest {

    private static MutableNetwork<DoubleDoublePair, ObjectObjectImmutablePair<DoubleDoublePair, DoubleDoublePair>>
            undirectedGraph;
    private static MutableNetwork<DoubleDoublePair, ObjectObjectImmutablePair<DoubleDoublePair, DoubleDoublePair>>
            directedGraph;

    private static Pathfinder<DoubleDoublePair> aStarUndir;
    private static Pathfinder<DoubleDoublePair> aStarDir;

    private static Pathfinder<DoubleDoublePair> idaUndir;
    private static Pathfinder<DoubleDoublePair> idaDir;

    private static ObjectObjectImmutablePair<DoubleDoublePair, DoubleDoublePair> weightFunc(
            DoubleDoublePair node1, DoubleDoublePair node2) {
        return ObjectObjectImmutablePair.of(node1, node2);
    }

    private static final List<DoubleDoublePair> p = List.of(
        DoubleDoubleImmutablePair.of(0.0,   0.0),    // 0
        DoubleDoubleImmutablePair.of(1.0,  -1.0),    // 1
        DoubleDoubleImmutablePair.of(0.0,  -3.0),    // 2
        DoubleDoubleImmutablePair.of(0.5,  -5.0),    // 3
        DoubleDoubleImmutablePair.of(-0.5, -5.0),    // 4
        DoubleDoubleImmutablePair.of(0.0,  -6.0),    // 5
        DoubleDoubleImmutablePair.of(3.0,   1.0),    // 6
        DoubleDoubleImmutablePair.of(4.0,   3.0),    // 7
        DoubleDoubleImmutablePair.of(3.0,   5.0),    // 8
        DoubleDoubleImmutablePair.of(0.0,   4.5),    // 9
        DoubleDoubleImmutablePair.of(0.0,   5.5),    // 10
        DoubleDoubleImmutablePair.of(0.3,   2.0),    // 11
        DoubleDoubleImmutablePair.of(-0.5,  2.0),    // 12
        DoubleDoubleImmutablePair.of(-3.0,  0.2),    // 13
        DoubleDoubleImmutablePair.of(-3.5,  2.0),    // 14
        DoubleDoubleImmutablePair.of(-4.0,  2.5),    // 15
        DoubleDoubleImmutablePair.of(-5.0,  2.5),    // 16
        DoubleDoubleImmutablePair.of(-1.0,  3.0)     // 17
    );

    @BeforeAll
    static void setup() {
        directedGraph = NetworkBuilder.directed().build();
        directedGraph.addEdge(p.get(0), p.get(2),   weightFunc(p.get(0), p.get(2)));
        directedGraph.addEdge(p.get(0), p.get(1),   weightFunc(p.get(0), p.get(1)));
        directedGraph.addEdge(p.get(0), p.get(6),   weightFunc(p.get(0), p.get(6)));
        directedGraph.addEdge(p.get(0), p.get(1),   weightFunc(p.get(0), p.get(1)));
        directedGraph.addEdge(p.get(0), p.get(12),  weightFunc(p.get(0), p.get(12)));
        directedGraph.addEdge(p.get(2), p.get(3),   weightFunc(p.get(2), p.get(3)));
        directedGraph.addEdge(p.get(2), p.get(4),   weightFunc(p.get(2), p.get(4)));
        directedGraph.addEdge(p.get(4), p.get(5),   weightFunc(p.get(4), p.get(5)));
        directedGraph.addEdge(p.get(6), p.get(7),   weightFunc(p.get(6), p.get(7)));
        directedGraph.addEdge(p.get(7), p.get(8),   weightFunc(p.get(7), p.get(8)));
        directedGraph.addEdge(p.get(8), p.get(9),   weightFunc(p.get(8), p.get(9)));
        directedGraph.addEdge(p.get(9), p.get(10),  weightFunc(p.get(9), p.get(10)));
        directedGraph.addEdge(p.get(9), p.get(11),  weightFunc(p.get(9), p.get(11)));
        directedGraph.addEdge(p.get(9), p.get(12),  weightFunc(p.get(9), p.get(12)));
        directedGraph.addEdge(p.get(12), p.get(13), weightFunc(p.get(12), p.get(13)));
        directedGraph.addEdge(p.get(12), p.get(14), weightFunc(p.get(12), p.get(14)));
        directedGraph.addEdge(p.get(12), p.get(15), weightFunc(p.get(12), p.get(15)));
        directedGraph.addEdge(p.get(12), p.get(17), weightFunc(p.get(12), p.get(17)));
        directedGraph.addEdge(p.get(15), p.get(16), weightFunc(p.get(15), p.get(16)));

        undirectedGraph = NetworkBuilder.undirected().build();
        directedGraph.nodes().forEach(node ->
            directedGraph.successors(node).forEach(successor ->
                undirectedGraph.addEdge(
                        node,
                        successor,
                        directedGraph.edgeConnecting(node, successor).orElseThrow()
                )
            )
        );

        aStarUndir = new AStarPathfinder<>(
                undirectedGraph,
                e -> euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble)
                        .apply(e.left(), e.right()),
                euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble));
        aStarDir = new AStarPathfinder<>(
                directedGraph,
                e -> euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble)
                        .apply(e.left(), e.right()),
                euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble));

        idaUndir = new IDAStarPathfinder<>(
                undirectedGraph,
                e -> euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble)
                        .apply(e.left(), e.right()),
                euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble));
        idaDir = new IDAStarPathfinder<>(
                directedGraph,
                e -> euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble)
                        .apply(e.left(), e.right()),
                euclideanDistance(DoubleDoublePair::leftDouble, DoubleDoublePair::rightDouble));
    }

    @Test
    void shortPathUndirected() {
        final var expected = List.of(p.get(0), p.get(2));
        assertEquals(expected, aStarUndir.findPath(p.get(0), p.get(2)));
        assertEquals(expected, idaUndir.findPath(p.get(0), p.get(2)));
    }

    @Test
    void shortPathDirected() {
        final var expected = List.of(p.get(0), p.get(2));
        assertEquals(expected, aStarDir.findPath(p.get(0), p.get(2)));
        assertEquals(expected, idaDir.findPath(p.get(0), p.get(2)));
    }

    @Test
    void slightlyLongerPathUndirected() {
        final var expected = List.of(p.get(6), p.get(0), p.get(12), p.get(15), p.get(16));
        assertEquals(expected, aStarUndir.findPath(p.get(6), p.get(16)));
        assertEquals(expected, idaUndir.findPath(p.get(6), p.get(16)));
    }

    @Test
    void slightlyLongerPathDirected() {
        final var expected = List.of(p.get(6), p.get(7), p.get(8), p.get(9), p.get(12), p.get(15),
                p.get(16));
        assertEquals(expected, aStarDir.findPath(p.get(6), p.get(16)));
        assertEquals(expected, idaDir.findPath(p.get(6), p.get(16)));
    }

    @Test
    void pathExistingOnlyInUndirected() {
        final var expected = List.of(p.get(3), p.get(2), p.get(0), p.get(6));
        assertEquals(expected, aStarUndir.findPath(p.get(3), p.get(6)));
        assertEquals(expected, idaUndir.findPath(p.get(3), p.get(6)));
    }

    @Test
    void nonExistingPathInDirected() {
        assertEquals(Collections.emptyList(), aStarDir.findPath(p.get(3), p.get(6)));
        assertEquals(Collections.emptyList(), idaDir.findPath(p.get(3), p.get(6)));
    }

    @Test
    void equalSourceAndDestinationUndirected() {
        final var expected = List.of(p.get(3));
        assertEquals(expected, aStarUndir.findPath(p.get(3), p.get(3)));
        assertEquals(expected, idaUndir.findPath(p.get(3), p.get(3)));
    }

    @Test
    void equalSourceAndDestinationDirected() {
        final var expected = List.of(p.get(3));
        assertEquals(expected, aStarDir.findPath(p.get(3), p.get(3)));
        assertEquals(expected, idaDir.findPath(p.get(3), p.get(3)));
    }
}
