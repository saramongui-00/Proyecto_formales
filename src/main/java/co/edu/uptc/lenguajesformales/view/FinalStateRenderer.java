package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import com.google.common.base.Function;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class FinalStateRenderer extends BasicVertexRenderer<String, String> implements Function<String, Paint> {

    private final AutomatonDTO automaton;

    public FinalStateRenderer(AutomatonDTO automaton) {
        this.automaton = automaton;
    }

    @Override
    public Paint apply(String state) {
        if (state.equals(automaton.getInitialState())) {
            return Color.GREEN;
        }
        if (automaton.getFinalStates().contains(state)) {
            return Color.RED;
        }
        return Color.WHITE;
    }

    @Override
    protected void paintShapeForVertex(RenderContext<String, String> rc, String vertex, Shape shape) {
        super.paintShapeForVertex(rc, vertex, shape);

        if (!automaton.getFinalStates().contains(vertex)) {
            return;
        }

        Rectangle2D bounds = shape.getBounds2D();
        double inset = 4.0;
        if (bounds.getWidth() <= inset * 2 || bounds.getHeight() <= inset * 2) {
            return;
        }

        Shape innerCircle = new Ellipse2D.Double(
                bounds.getX() + inset,
                bounds.getY() + inset,
                bounds.getWidth() - inset * 2,
                bounds.getHeight() - inset * 2
        );

        Paint previousPaint = rc.getGraphicsContext().getPaint();
        Stroke previousStroke = rc.getGraphicsContext().getStroke();

        rc.getGraphicsContext().setPaint(Color.BLACK);
        rc.getGraphicsContext().setStroke(new BasicStroke(1.5f));
        rc.getGraphicsContext().draw(innerCircle);

        rc.getGraphicsContext().setPaint(previousPaint);
        rc.getGraphicsContext().setStroke(previousStroke);
    }
}