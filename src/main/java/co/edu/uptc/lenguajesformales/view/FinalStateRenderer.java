package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.Automaton;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class FinalStateRenderer implements Renderer.Vertex<String, String> {

    private Automaton automaton;

    public FinalStateRenderer(Automaton automaton) {
        this.automaton = automaton;
    }

    @Override
    public void paintVertex(RenderContext<String, String> rc,
                            Layout<String, String> layout,
                            String vertex) {

        GraphicsDecorator g = rc.getGraphicsContext();

        Point2D p = layout.apply(vertex);

        int size = 30;

        Shape circle = new Ellipse2D.Double(p.getX() - size/2, p.getY() - size/2, size, size);
        g.draw(circle);

        if (automaton.getFinalStates().contains(vertex)) {
            Shape inner = new Ellipse2D.Double(p.getX() - size/2 + 4, p.getY() - size/2 + 4, size-8, size-8);
            g.draw(inner);
        }
    }
}