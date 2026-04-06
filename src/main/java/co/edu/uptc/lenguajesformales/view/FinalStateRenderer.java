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

// Renderizador personalizado para los estados de un autómata, colorea estados iniciales y finales
public class FinalStateRenderer extends BasicVertexRenderer<String, String> implements Function<String, Paint> {

    private final AutomatonDTO automaton;

    // Constructor que recibe el autómata para conocer sus estados inicial y finales
    public FinalStateRenderer(AutomatonDTO automaton) {
        this.automaton = automaton;
    }

    // Define el color de cada estado: verde para inicial, rojo para final, blanco para los demás
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

    // Dibuja un círculo interior adicional para los estados finales
    @Override
    protected void paintShapeForVertex(RenderContext<String, String> rc, String vertex, Shape shape) {
        super.paintShapeForVertex(rc, vertex, shape);

        // Solo dibuja el círculo interior si el estado es final
        if (!automaton.getFinalStates().contains(vertex)) {
            return;
        }

        Rectangle2D bounds = shape.getBounds2D();
        double inset = 4.0;
        // Evita dibujar si el círculo sería muy pequeño
        if (bounds.getWidth() <= inset * 2 || bounds.getHeight() <= inset * 2) {
            return;
        }

        // Crea un círculo interior más pequeño que el estado
        Shape innerCircle = new Ellipse2D.Double(
                bounds.getX() + inset,
                bounds.getY() + inset,
                bounds.getWidth() - inset * 2,
                bounds.getHeight() - inset * 2
        );

        // Guarda el estado anterior del contexto gráfico
        Paint previousPaint = rc.getGraphicsContext().getPaint();
        Stroke previousStroke = rc.getGraphicsContext().getStroke();

        // Dibuja el círculo interior negro con borde fino
        rc.getGraphicsContext().setPaint(Color.BLACK);
        rc.getGraphicsContext().setStroke(new BasicStroke(1.5f));
        rc.getGraphicsContext().draw(innerCircle);

        // Restaura el estado anterior del contexto gráfico
        rc.getGraphicsContext().setPaint(previousPaint);
        rc.getGraphicsContext().setStroke(previousStroke);
    }
}