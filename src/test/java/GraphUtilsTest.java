import com.github.graphextras.commons.GraphUtils;
import com.google.common.graph.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GraphUtilsTest {

    private static MutableGraph<Pair<Double, Double>> simpleGraph;

    private final static Pair<Double, Double> p1 = Pair.of(0.0, 0.0);
    private final static Pair<Double, Double> p2 = Pair.of(5.0, 0.0);
    private final static Pair<Double, Double> p3 = Pair.of(0.0, 1.0);
    private final static Pair<Double, Double> p4 = Pair.of(4.0, 0.0);
    private final static Pair<Double, Double> p5 = Pair.of(2.0, 0.0);
    private final static Pair<Double, Double> p6 = Pair.of(3.0, 3.0);
    private final static Pair<Double, Double> p7 = Pair.of(4.0, 4.0);

    @BeforeAll
    static void setup() {
        simpleGraph = GraphBuilder.directed().build();
        simpleGraph.putEdge(p1, p2);
        simpleGraph.putEdge(p2, p3);
        simpleGraph.putEdge(p3, p4);
        simpleGraph.putEdge(p5, p6);
        simpleGraph.putEdge(p6, p5);
        simpleGraph.putEdge(p7, p5);
        simpleGraph.putEdge(p3, p7);
    }

    @Test
    void conversion() {
        AtomicReference<ValueGraph<Pair<Double, Double>, Double>> valueGraph = new AtomicReference<>();
        assertDoesNotThrow(() -> valueGraph.set(GraphUtils.simpleToImmutableValueGraph(simpleGraph)));
        assertEquals(simpleGraph.nodes(), valueGraph.get().nodes());
        assertEquals(simpleGraph.edges(), valueGraph.get().edges());
    }

    @Test
    void randomGraph() {
        var graph = GraphUtils.randomGraph(2500, 0.9,
                Pair::of);
        assertTrue(graph.nodes().size() == 2500, ""+graph.nodes().size());
    }
}
