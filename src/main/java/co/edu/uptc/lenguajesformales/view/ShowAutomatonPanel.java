package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class ShowAutomatonPanel extends JPanel {

    public ShowAutomatonPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Autómata"));
    }

    public void setAutomaton(AutomatonDTO automaton) {
        DirectedSparseMultigraph<String, String> graph = new DirectedSparseMultigraph<>();

        for (String state : automaton.getStates()) {
            graph.addVertex(state);
        }

        Map<String, Map<String, Integer>> transitionCount = new HashMap<>();
        Map<String, String> transitionSymbols = new HashMap<>();

        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String key = t.getFromState() + "->" + t.getToState();
            transitionCount.putIfAbsent(t.getFromState() + "->" + t.getToState(), new HashMap<>());
            transitionCount.get(t.getFromState() + "->" + t.getToState())
                    .put(t.getSymbol(), transitionCount.get(t.getFromState() + "->" + t.getToState())
                            .getOrDefault(t.getSymbol(), 0) + 1);
            transitionSymbols.put(key, t.getSymbol());
        }

        int edgeId = 0;
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String edgeName = t.getSymbol() + "_" + edgeId++;
            graph.addEdge(edgeName, t.getFromState(), t.getToState());
        }

        Set<String> processedSelfLoops = new HashSet<>();
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            if (t.getFromState().equals(t.getToState())) {
                String state = t.getFromState();
                if (!processedSelfLoops.contains(state)) {
                    int count = 0;
                    for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t2 : automaton.getTransitions()) {
                        if (t2.getFromState().equals(state) && t2.getToState().equals(state)) {
                            count++;
                        }
                    }

                    if (count == 1) {
                        String dummyEdge = "dummy_" + state + "_" + edgeId++;
                        graph.addEdge(dummyEdge, state, state);
                    }
                    processedSelfLoops.add(state);
                }
            }
        }

        Layout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(500, 400));
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(600, 450));

        vv.getRenderContext().setVertexLabelTransformer(v -> v);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        vv.getRenderContext().setEdgeLabelTransformer(e -> {
            if (e.toString().startsWith("dummy_")) {
                return "";
            }
            return e.toString().split("_")[0];
        });

        vv.getRenderContext().setEdgeDrawPaintTransformer(e -> {
            if (e.toString().startsWith("dummy_")) {
                return new Color(255, 255, 255, 0);
            }
            return Color.BLACK;
        });

        vv.getRenderContext().setVertexFillPaintTransformer(
                new FinalStateRenderer(automaton)
        );

        vv.getRenderContext().setVertexShapeTransformer(v ->
                new Ellipse2D.Double(-20, -20, 40, 40)
        );

        removeAll();
        setLayout(new BorderLayout());
        add(vv, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}