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
        int edgeId = 0;
        for (co.edu.uptc.lenguajesformales.dto.TransitionDTO t : automaton.getTransitions()) {
            String edgeLabel = t.getSymbol();
            graph.addEdge(edgeLabel + "_" + edgeId++, t.getFromState(), t.getToState());
        }
        Layout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(500, 400));
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(600, 450));
        vv.getRenderContext().setVertexLabelTransformer(v -> v);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setEdgeLabelTransformer(e -> e.split("_")[0]);
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