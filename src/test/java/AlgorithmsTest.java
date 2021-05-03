import com.github.graphextras.algorithms.*;
import com.github.graphextras.algorithms.TwoDimensionalHeuristics;
import com.google.common.graph.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AlgorithmsTest {

    private static MutableValueGraph<double[], Double> undirectedGraph;
    private static MutableValueGraph<double[], Double> directedGraph;

    private final Pathfinder<double[], Double> aStar = new AStarPathfinder<>(TwoDimensionalHeuristics.euclideanDistance());
    private final Pathfinder<double[], Double> ida = new IDAStarPathfinder<>(TwoDimensionalHeuristics.euclideanDistance());

    private static final double[] p1 = {0.0, 0.0};
    private static final double[] p2 = {5.0, 0.0};
    private static final double[] p3 = {0.0, 1.0};
    private static final double[] p4 = {4.0, 0.0};
    private static final double[] p5 = {2.0, 0.0};
    private static final double[] p6 = {3.0, 3.0};
    private static final double[] p7 = {4.0, 4.0};

    @BeforeAll
    static void setup() {
        directedGraph = ValueGraphBuilder.directed().build();
        directedGraph.putEdgeValue(p1, p2, 1.0);
        directedGraph.putEdgeValue(p2, p3, 1.0);
        directedGraph.putEdgeValue(p3, p4, 1.0);
        directedGraph.putEdgeValue(p5, p6, 1.0);
        directedGraph.putEdgeValue(p6, p5, 1.0);
        directedGraph.putEdgeValue(p7, p5, 1.0);
        directedGraph.putEdgeValue(p3, p7, 1.0);

        undirectedGraph = ValueGraphBuilder.undirected().build();
        directedGraph.edges().forEach(e -> undirectedGraph.putEdgeValue(e, 1.0));
    }

    @Test
    void shortPathUndirected() {
        final List<double[]> expected = List.of(p1, p2, p3);
        assertEquals(expected, aStar.findPath(undirectedGraph, p1, p3));
        assertEquals(expected, ida.findPath(undirectedGraph, p1, p3));
    }

    @Test
    void shortPathDirected() {
        final List<double[]> expected = List.of(p1, p2, p3);
        assertEquals(expected, aStar.findPath(directedGraph, p1, p3));
        assertEquals(expected, ida.findPath(directedGraph, p1, p3));
    }

    @Test
    void slightlyLongerPathUndirected() {
        final List<double[]> expected = List.of(p1, p2, p3, p7, p5);
        assertEquals(expected, aStar.findPath(undirectedGraph, p1, p5));
        assertEquals(expected, ida.findPath(undirectedGraph, p1, p5));
    }

    @Test
    void slightlyLongerPathDirected() {
        final List<double[]> expected = List.of(p1, p2, p3, p7, p5);
        assertEquals(expected, aStar.findPath(directedGraph, p1, p5));
        assertEquals(expected, ida.findPath(directedGraph, p1, p5));
    }

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
}
