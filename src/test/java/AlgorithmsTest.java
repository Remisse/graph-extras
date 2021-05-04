import com.github.graphextras.algorithms.*;
import com.google.common.graph.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.graphextras.algorithms.TwoDimensionalHeuristics.euclideanDistance;

@SuppressWarnings("SuspiciousNameCombination")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AlgorithmsTest {

    private static MutableValueGraph<Pair<Double, Double>, Double> undirectedGraph;
    private static MutableValueGraph<Pair<Double, Double>, Double> directedGraph;

    private final Pathfinder<Pair<Double, Double>> aStar = new AStarPathfinder<>(
            euclideanDistance(Pair::getLeft, Pair::getRight));
    private final Pathfinder<Pair<Double, Double>> ida = new IDAStarPathfinder<>(
            euclideanDistance(Pair::getLeft, Pair::getRight));

    private static double weightFunc(Pair<Double, Double> node1, Pair<Double, Double> node2) {
        final double dx = node1.getLeft() - node2.getLeft();
        final double dy = node1.getRight() - node2.getRight();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static final List<Pair<Double, Double>> p = List.of(
        Pair.of(0.0,   0.0),    // 0
        Pair.of(1.0,  -1.0),    // 1
        Pair.of(0.0,  -3.0),    // 2
        Pair.of(0.5,  -5.0),    // 3
        Pair.of(-0.5, -5.0),    // 4
        Pair.of(0.0,  -6.0),    // 5
        Pair.of(3.0,   1.0),    // 6
        Pair.of(4.0,   3.0),    // 7
        Pair.of(3.0,   5.0),    // 8
        Pair.of(0.0,   4.5),    // 9
        Pair.of(0.0,   5.5),    // 10
        Pair.of(0.3,   2.0),    // 11
        Pair.of(-0.5,  2.0),    // 12
        Pair.of(-3.0,  0.2),    // 13
        Pair.of(-3.5,  2.0),    // 14
        Pair.of(-4.0,  2.5),    // 15
        Pair.of(-5.0,  2.5),    // 16
        Pair.of(-1.0,  3.0)     // 17
    );

    @BeforeAll
    static void setup() {
        directedGraph = ValueGraphBuilder.directed().build();
        directedGraph.putEdgeValue(p.get(0), p.get(2),   weightFunc(p.get(0), p.get(2)));
        directedGraph.putEdgeValue(p.get(0), p.get(1),   weightFunc(p.get(0), p.get(1)));
        directedGraph.putEdgeValue(p.get(0), p.get(6),   weightFunc(p.get(0), p.get(6)));
        directedGraph.putEdgeValue(p.get(0), p.get(1),   weightFunc(p.get(0), p.get(1)));
        directedGraph.putEdgeValue(p.get(0), p.get(12),  weightFunc(p.get(0), p.get(12)));
        directedGraph.putEdgeValue(p.get(2), p.get(3),   weightFunc(p.get(2), p.get(3)));
        directedGraph.putEdgeValue(p.get(2), p.get(4),   weightFunc(p.get(2), p.get(4)));
        directedGraph.putEdgeValue(p.get(4), p.get(5),   weightFunc(p.get(4), p.get(5)));
        directedGraph.putEdgeValue(p.get(6), p.get(7),   weightFunc(p.get(6), p.get(7)));
        directedGraph.putEdgeValue(p.get(7), p.get(8),   weightFunc(p.get(7), p.get(8)));
        directedGraph.putEdgeValue(p.get(8), p.get(9),   weightFunc(p.get(8), p.get(9)));
        directedGraph.putEdgeValue(p.get(9), p.get(10),  weightFunc(p.get(9), p.get(10)));
        directedGraph.putEdgeValue(p.get(9), p.get(11),  weightFunc(p.get(9), p.get(11)));
        directedGraph.putEdgeValue(p.get(9), p.get(12),  weightFunc(p.get(9), p.get(12)));
        directedGraph.putEdgeValue(p.get(12), p.get(13), weightFunc(p.get(12), p.get(13)));
        directedGraph.putEdgeValue(p.get(12), p.get(14), weightFunc(p.get(12), p.get(14)));
        directedGraph.putEdgeValue(p.get(12), p.get(15), weightFunc(p.get(12), p.get(15)));
        directedGraph.putEdgeValue(p.get(12), p.get(17), weightFunc(p.get(12), p.get(17)));
        directedGraph.putEdgeValue(p.get(15), p.get(16), weightFunc(p.get(15), p.get(16)));

        undirectedGraph = ValueGraphBuilder.undirected().build();
        directedGraph.edges().forEach(e ->
                undirectedGraph.putEdgeValue(e.nodeU(), e.nodeV(), directedGraph.edgeValue(e.nodeU(), e.nodeV()).orElseThrow()));
    }

    @Test
    void shortPathUndirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(0), p.get(2));
        assertEquals(expected, aStar.findPath(undirectedGraph, p.get(0), p.get(2)));
        assertEquals(expected, ida.findPath(undirectedGraph, p.get(0), p.get(2)));
    }

    @Test
    void shortPathDirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(0), p.get(2));
        assertEquals(expected, aStar.findPath(directedGraph, p.get(0), p.get(2)));
        assertEquals(expected, ida.findPath(directedGraph, p.get(0), p.get(2)));
    }

    @Test
    void slightlyLongerPathUndirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(6), p.get(0), p.get(12), p.get(15), p.get(16));
        assertEquals(expected, aStar.findPath(undirectedGraph, p.get(6), p.get(16)));
        assertEquals(expected, ida.findPath(undirectedGraph, p.get(6), p.get(16)));
    }

    @Test
    void slightlyLongerPathDirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(6), p.get(7), p.get(8), p.get(9), p.get(12), p.get(15), p.get(16));
        assertEquals(expected, aStar.findPath(directedGraph, p.get(6), p.get(16)));
        assertEquals(expected, ida.findPath(directedGraph, p.get(6), p.get(16)));
    }

    @Test
    void pathExistingOnlyInUndirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(3), p.get(2), p.get(0), p.get(6));
        assertEquals(expected, aStar.findPath(undirectedGraph, p.get(3), p.get(6)));
        assertEquals(expected, ida.findPath(undirectedGraph, p.get(3), p.get(6)));
    }

    @Test
    void nonExistingPathInDirected() {
        assertEquals(Collections.emptyList(), aStar.findPath(directedGraph, p.get(3), p.get(6)));
        assertEquals(Collections.emptyList(), ida.findPath(directedGraph, p.get(3), p.get(6)));
    }

    @Test
    void equalSourceAndDestinationUndirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(3));
        assertEquals(expected, aStar.findPath(undirectedGraph, p.get(3), p.get(3)));
        assertEquals(expected, ida.findPath(undirectedGraph, p.get(3), p.get(3)));
    }

    @Test
    void equalSourceAndDestinationDirected() {
        final List<Pair<Double, Double>> expected = List.of(p.get(3));
        assertEquals(expected, aStar.findPath(directedGraph, p.get(3), p.get(3)));
        assertEquals(expected, ida.findPath(directedGraph, p.get(3), p.get(3)));
    }
}
