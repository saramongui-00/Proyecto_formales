package co.edu.uptc.lenguajesformales.view;



import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class ShowAutomatonPanel extends JPanel {

    private VisualizationViewer<String,String> viewer;

    public ShowAutomatonPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Autómata"));
    }

    public void setAutomaton(AutomatonDTO automaton) {

        // Grafo dirigido
        DirectedSparseMultigraph<String, String> graph = new DirectedSparseMultigraph<>();

        // Agregar estados
        for (String state : automaton.getStates()) {
            graph.addVertex(state);
        }

        // Agregar transiciones (edgeId único)
        int edgeId = 0;
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String edgeLabel = t.getSymbol();
            graph.addEdge(edgeLabel + "_" + edgeId++, t.getFromState(), t.getToState());
        }

        // Layout circular (se ve mejor que FR para pocos nodos)
        Layout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(500, 400));

        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(600, 450));

        // Etiquetas de nodos
        vv.getRenderContext().setVertexLabelTransformer(v -> v);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        // Etiquetas de aristas (símbolos)
        vv.getRenderContext().setEdgeLabelTransformer(e -> e.split("_")[0]);

        // Renderer de colores (tu FinalStateRenderer)
        vv.getRenderContext().setVertexFillPaintTransformer(
                new FinalStateRenderer(automaton)
        );

        // Tamaño de nodos
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