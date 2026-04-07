package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel que visualiza gráficamente el autómata usando la librería JUNG.
 */
public class ShowAutomatonPanel extends JPanel {

    /**
     * Constructor que configura el layout y borde del panel.
     */
    public ShowAutomatonPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Autómata"));
    }

    /**
     * Recibe un autómata y lo dibuja en el panel.
     * @param automaton Datos del autómata a mostrar.
     */
    public void setAutomaton(AutomatonDTO automaton) {
        // Crea un grafo dirigido multigrafo
        DirectedSparseMultigraph<String, String> graph = new DirectedSparseMultigraph<>();

        // Agrega todos los estados como vértices del grafo
        for (String state : automaton.getStates()) {
            graph.addVertex(state);
        }

        Map<String, List<String>> selfLoopSymbols = new LinkedHashMap<>();
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            if (t.getFromState().equals(t.getToState())) {
                selfLoopSymbols
                        .computeIfAbsent(t.getFromState(), k -> new ArrayList<>())
                        .add(t.getSymbol());
            }
        }

        Map<String, String> edgeLabels = new HashMap<>();

        int edgeId = 0;

        // Agrega transiciones normales (no auto-transiciones)
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            if (!t.getFromState().equals(t.getToState())) {
                String edgeName = "edge_" + edgeId++;
                graph.addEdge(edgeName, t.getFromState(), t.getToState());
                // Para transiciones entre estados distintos también agrupamos
                // los símbolos si ya existe una arista entre ese par
                edgeLabels.merge(edgeName, t.getSymbol(), (a, b) -> a + ", " + b);
            }
        }


        for (Map.Entry<String, List<String>> entry : selfLoopSymbols.entrySet()) {
            String state = entry.getKey();
            String combinedLabel = String.join(", ", entry.getValue());

            // Arista real con etiqueta combinada
            String realEdge = "selfloop_" + state + "_" + edgeId++;
            graph.addEdge(realEdge, state, state);
            edgeLabels.put(realEdge, combinedLabel);

            // Arista dummy para forzar a JUNG a abrir el bucle visualmente
            String dummyEdge = "dummy_" + state + "_" + edgeId++;
            graph.addEdge(dummyEdge, state, state);
            edgeLabels.put(dummyEdge, null); // sin etiqueta
        }

        // ---------------------------------------------------------------
        // Configurar layout y visualizador
        // ---------------------------------------------------------------
        Layout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(500, 400));
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(600, 450));

        DefaultModalGraphMouse<String, String> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(graphMouse);

        // Etiquetas de vértices
        vv.getRenderContext().setVertexLabelTransformer(v -> v);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        
        vv.getRenderContext().setEdgeLabelTransformer(e -> {
            String label = edgeLabels.get(e);
            return label != null ? label : "";
        });

        // Aristas dummy completamente invisibles
        vv.getRenderContext().setEdgeDrawPaintTransformer(e -> {
            if (e.startsWith("dummy_")) {
                return new Color(255, 255, 255, 0);
            }
            return Color.BLACK;
        });

        // Colorear estados iniciales y finales
        FinalStateRenderer finalStateRenderer = new FinalStateRenderer(automaton);
        vv.getRenderContext().setVertexFillPaintTransformer(finalStateRenderer);

        // Forma circular para los vértices
        vv.getRenderContext().setVertexShapeTransformer(v ->
                new Ellipse2D.Double(-20, -20, 40, 40)
        );

        vv.getRenderer().setVertexRenderer(finalStateRenderer);

        // Reemplaza el contenido del panel
        removeAll();
        setLayout(new BorderLayout());
        add(vv, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}