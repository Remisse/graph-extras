import com.github.graphextras.graphs.GraphMakers;
import com.google.common.graph.*;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.doubles.DoubleDoublePair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GraphMakersTest {

    private static MutableGraph<DoubleDoublePair> simpleGraph;

    private final static DoubleDoublePair p1 = DoubleDoubleImmutablePair.of(0.0, 0.0);
    private final static DoubleDoublePair p2 = DoubleDoubleImmutablePair.of(5.0, 0.0);
    private final static DoubleDoublePair p3 = DoubleDoubleImmutablePair.of(0.0, 1.0);
    private final static DoubleDoublePair p4 = DoubleDoubleImmutablePair.of(4.0, 0.0);
    private final static DoubleDoublePair p5 = DoubleDoubleImmutablePair.of(2.0, 0.0);
    private final static DoubleDoublePair p6 = DoubleDoubleImmutablePair.of(3.0, 3.0);
    private final static DoubleDoublePair p7 = DoubleDoubleImmutablePair.of(4.0, 4.0);

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
        AtomicReference<ValueGraph<DoubleDoublePair, Double>> valueGraph = new AtomicReference<>();
        assertDoesNotThrow(() -> valueGraph.set(GraphMakers.simpleToImmutableValueGraph(simpleGraph)));
        assertEquals(simpleGraph.nodes(), valueGraph.get().nodes());
        assertEquals(simpleGraph.edges(), valueGraph.get().edges());
    }
}
