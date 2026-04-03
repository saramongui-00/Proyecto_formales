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

        // Agregar todos los estados
        for (String state : automaton.getStates()) {
            graph.addVertex(state);
        }

        // Agrupar transiciones por estado origen y destino
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

        // Agregar aristas al grafo
        int edgeId = 0;
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String edgeName = t.getSymbol() + "_" + edgeId++;
            graph.addEdge(edgeName, t.getFromState(), t.getToState());
        }

        // Para self-loops con una sola transición, agregar una arista invisible adicional
        // para forzar que la etiqueta sea visible
        Set<String> processedSelfLoops = new HashSet<>();
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            if (t.getFromState().equals(t.getToState())) {
                String state = t.getFromState();
                if (!processedSelfLoops.contains(state)) {
                    // Contar cuántas transiciones recursivas tiene este estado
                    int count = 0;
                    for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t2 : automaton.getTransitions()) {
                        if (t2.getFromState().equals(state) && t2.getToState().equals(state)) {
                            count++;
                        }
                    }

                    // Si solo hay una transición recursiva, agregamos una arista dummy
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

        // Transformador para etiquetas de aristas
        vv.getRenderContext().setEdgeLabelTransformer(e -> {
            if (e.toString().startsWith("dummy_")) {
                return ""; // Las aristas dummy no muestran etiqueta
            }
            return e.toString().split("_")[0];
        });

        // Transformador para el color de las aristas dummy (hacerlas invisibles o muy claras)
        vv.getRenderContext().setEdgeDrawPaintTransformer(e -> {
            if (e.toString().startsWith("dummy_")) {
                return new Color(255, 255, 255, 0); // Completamente transparente
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