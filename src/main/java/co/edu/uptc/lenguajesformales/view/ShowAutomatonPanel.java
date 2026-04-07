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
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

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

        // Mapas para contar transiciones y almacenar símbolos
        Map<String, Map<String, Integer>> transitionCount = new HashMap<>();
        Map<String, String> transitionSymbols = new HashMap<>();

        // Cuenta las transiciones por par de estados y símbolo
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String key = t.getFromState() + "->" + t.getToState();
            transitionCount.putIfAbsent(t.getFromState() + "->" + t.getToState(), new HashMap<>());
            transitionCount.get(t.getFromState() + "->" + t.getToState())
                    .put(t.getSymbol(), transitionCount.get(t.getFromState() + "->" + t.getToState())
                            .getOrDefault(t.getSymbol(), 0) + 1);
            transitionSymbols.put(key, t.getSymbol());
        }

        // Agrega las transiciones como aristas del grafo
        int edgeId = 0;
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String edgeName = t.getSymbol() + "_" + edgeId++;
            graph.addEdge(edgeName, t.getFromState(), t.getToState());
        }

        // Procesa auto-transiciones (bucles) agregando aristas dummy si es necesario
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

                    // Si solo hay una auto-transición, agrega una arista dummy para mejor visualización
                    if (count == 1) {
                        String dummyEdge = "dummy_" + state + "_" + edgeId++;
                        graph.addEdge(dummyEdge, state, state);
                    }
                    processedSelfLoops.add(state);
                }
            }
        }

        // Configura el layout circular del grafo
        Layout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(500, 400));
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(600, 450));

        // Configura el mouse para poder mover y seleccionar elementos
        DefaultModalGraphMouse<String, String> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(graphMouse);

        // Muestra las etiquetas de los vértices (estados)
        vv.getRenderContext().setVertexLabelTransformer(v -> v);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        // Muestra las etiquetas de las aristas (símbolos), oculta las aristas dummy
        vv.getRenderContext().setEdgeLabelTransformer(e -> {
            if (e.toString().startsWith("dummy_")) {
                return "";
            }
            return e.toString().split("_")[0];
        });

        // Hace invisibles las aristas dummy
        vv.getRenderContext().setEdgeDrawPaintTransformer(e -> {
            if (e.toString().startsWith("dummy_")) {
                return new Color(255, 255, 255, 0);
            }
            return Color.BLACK;
        });

        // Renderizador personalizado para colorear estados iniciales y finales
        FinalStateRenderer finalStateRenderer = new FinalStateRenderer(automaton);
        vv.getRenderContext().setVertexFillPaintTransformer(finalStateRenderer);

        // Define la forma de los vértices como círculos
        vv.getRenderContext().setVertexShapeTransformer(v ->
                new Ellipse2D.Double(-20, -20, 40, 40)
        );

        // Asigna el renderizador personalizado a los vértices
        vv.getRenderer().setVertexRenderer(finalStateRenderer);

        // Limpia el panel y agrega el visualizador
        removeAll();
        setLayout(new BorderLayout());
        add(vv, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}