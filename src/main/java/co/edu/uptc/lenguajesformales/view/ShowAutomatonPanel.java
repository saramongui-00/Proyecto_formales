package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.Automaton;
import co.edu.uptc.lenguajesformales.dto.Transition;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShowAutomatonPanel extends JPanel {

    private VisualizationViewer<String, String> viewer;
    private DirectedSparseMultigraph<String, String> graph;

    public ShowAutomatonPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Autómata generado"));
    }

    // Método principal que la vista usará
    public void setAutomaton(Automaton automaton) {

        removeAll();
        graph = new DirectedSparseMultigraph<>();

        // 1️⃣ agregar estados (vértices)
        for (String state : automaton.getStates()) {
            graph.addVertex(state);
        }

        // 2️⃣ agrupar transiciones (para unir etiquetas repetidas)
        Map<String, StringBuilder> edgesMap = new HashMap<>();

        for (Transition t : automaton.getTransitions()) {
            String key = t.getFromState() + "->" + t.getToState();

            edgesMap.putIfAbsent(key, new StringBuilder());
            edgesMap.get(key).append(t.getSymbol()).append(",");
        }

        // 3️⃣ agregar aristas al grafo
        int edgeId = 0;
        for (String key : edgesMap.keySet()) {

            String[] parts = key.split("->");
            String from = parts[0];
            String to = parts[1];

            String label = edgesMap.get(key).toString();
            label = label.substring(0, label.length() - 1); // quitar última coma

            graph.addEdge(label + "_" + edgeId++, from, to);
        }

        // 4️⃣ layout circular automático
        CircleLayout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(500, 400));

        viewer = new VisualizationViewer<>(layout);
        viewer.setPreferredSize(new Dimension(600, 450));

        // etiquetas de estados
        viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        viewer.getRenderContext().setEdgeLabelTransformer(edge -> edge.split("_")[0]);

        // 5️⃣ pintar estados especiales
        viewer.getRenderContext().setVertexFillPaintTransformer(state -> {
            if (state.equals(automaton.getInitialState()))
                return Color.GREEN;
            if (automaton.getFinalStates().contains(state))
                return Color.ORANGE;
            return Color.LIGHT_GRAY;
        });

        // 6️⃣ dibujar borde doble para finales
        viewer.getRenderer().setVertexRenderer(new FinalStateRenderer(automaton));

        add(viewer, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}