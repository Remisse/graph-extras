import com.github.graphextras.algorithms.*;
import com.github.graphextras.algorithms.TwoDimensionalHeuristics;
import com.google.common.graph.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.graphextras.algorithms.TwoDimensionalHeuristics.euclideanDistance;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AlgorithmsTest {

    private static MutableValueGraph<Pair<Double, Double>, Double> undirectedGraph;
    private static MutableValueGraph<Pair<Double, Double>, Double> directedGraph;

    private final Pathfinder<Pair<Double, Double>, Double> aStar = new AStarPathfinder<>(euclideanDistance());
    private final Pathfinder<Pair<Double, Double>, Double> ida = new IDAStarPathfinder<>(euclideanDistance());

    private static final List<Pair<Double, Double>> p = List.of(
        Pair.of(0.0, 0.0),     // 0
        Pair.of(1.0, -1.0),    // 1
        Pair.of(0.0, -3.0),    // 2
        Pair.of(0.5, -5.0),    // 3
        Pair.of(-0.5, -5.0),   // 4
        Pair.of(0.0, -6.0),    // 5
        Pair.of(3.0, 1.0),     // 6
        Pair.of(4.0, 3.0),     // 7
        Pair.of(3.0, 5.0),     // 8
        Pair.of(0.0, 4.5),     // 9
        Pair.of(0.0, 5.5),     // 10
        Pair.of(0.3, 2.0),     // 11
        Pair.of(-0.5, 2.0),    // 12
        Pair.of(-3.0, 0.2),    // 13
        Pair.of(-3.5, 2.0),    // 14
        Pair.of(-4.0, 2.5),    // 15
        Pair.of(-5.0, 2.5),    // 16
        Pair.of(-1.0, 3.0)     // 17
    );

    @BeforeAll
    static void setup() {
        directedGraph = ValueGraphBuilder.directed().build();
        directedGraph.putEdgeValue(p.get(0), p.get(2), euclideanDistance().apply(p.get(0), p.get(2)));
        directedGraph.putEdgeValue(p.get(0), p.get(1), euclideanDistance().apply(p.get(0), p.get(1)));
        directedGraph.putEdgeValue(p.get(0), p.get(6), euclideanDistance().apply(p.get(0), p.get(6)));
        directedGraph.putEdgeValue(p.get(0), p.get(1), euclideanDistance().apply(p.get(0), p.get(1)));
        directedGraph.putEdgeValue(p.get(0), p.get(12), euclideanDistance().apply(p.get(0), p.get(12)));
        directedGraph.putEdgeValue(p.get(2), p.get(3), euclideanDistance().apply(p.get(2), p.get(3)));
        directedGraph.putEdgeValue(p.get(2), p.get(4), euclideanDistance().apply(p.get(2), p.get(4)));
        directedGraph.putEdgeValue(p.get(4), p.get(5), euclideanDistance().apply(p.get(4), p.get(5)));
        directedGraph.putEdgeValue(p.get(6), p.get(7), euclideanDistance().apply(p.get(6), p.get(7)));
        directedGraph.putEdgeValue(p.get(7), p.get(8), euclideanDistance().apply(p.get(7), p.get(8)));
        directedGraph.putEdgeValue(p.get(8), p.get(9), euclideanDistance().apply(p.get(8), p.get(9)));
        directedGraph.putEdgeValue(p.get(9), p.get(10), euclideanDistance().apply(p.get(9), p.get(10)));
        directedGraph.putEdgeValue(p.get(9), p.get(11), euclideanDistance().apply(p.get(9), p.get(11)));
        directedGraph.putEdgeValue(p.get(9), p.get(12), euclideanDistance().apply(p.get(9), p.get(12)));
        directedGraph.putEdgeValue(p.get(12), p.get(13), euclideanDistance().apply(p.get(12), p.get(13)));
        directedGraph.putEdgeValue(p.get(12), p.get(14), euclideanDistance().apply(p.get(12), p.get(14)));
        directedGraph.putEdgeValue(p.get(12), p.get(15), euclideanDistance().apply(p.get(12), p.get(15)));
        directedGraph.putEdgeValue(p.get(12), p.get(17), euclideanDistance().apply(p.get(12), p.get(17)));
        directedGraph.putEdgeValue(p.get(15), p.get(16), euclideanDistance().apply(p.get(15), p.get(16)));

        undirectedGraph = ValueGraphBuilder.undirected().build();
        directedGraph.edges().forEach(e -> undirectedGraph.putEdgeValue(e, euclideanDistance().apply(e.nodeU(), e.nodeV())));
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

    /*
    @Test
    void pathExistingOnlyInUndirected() {
        final List<double[]> expected = List.of(p4, p3, p7);
        assertEquals(expected, aStar.findPath(undirectedGraph, p4, p7));
        assertEquals(expected, ida.findPath(undirectedGraph, p4, p7));
    }

    @Test
    void noExistingPathDirected() {
        assertEquals(Collections.emptyList(), aStar.findPath(directedGraph, p4, p7));
        assertEquals(Collections.emptyList(), ida.findPath(directedGraph, p4, p7));
    }

    @Test
    void equalSourceAndDestinationUndirected() {
        final List<double[]> expected = List.of(p4);
        assertEquals(expected, aStar.findPath(undirectedGraph, p4, p4));
        assertEquals(expected, ida.findPath(undirectedGraph, p4, p4));
    }

    @Test
    void equalSourceAndDestinationDirected() {
        final List<double[]> expected = List.of(p4);
        assertEquals(expected, aStar.findPath(directedGraph, p4, p4));
        assertEquals(expected, ida.findPath(directedGraph, p4, p4));
    }
 */
}
