package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import com.google.common.base.Function;

import java.awt.Color;
import java.awt.Paint;

public class FinalStateRenderer implements Function<String, Paint> {

    private AutomatonDTO automaton;

    public FinalStateRenderer(AutomatonDTO automaton) {
        this.automaton = automaton;
    }

    @Override
    public Paint apply(String state) {

        // Estado inicial → verde
        if (state.equals(automaton.getInitialState())) {
            return Color.GREEN;
        }

        // Estados finales → rojo
        if (automaton.getFinalStates().contains(state)) {
            return Color.RED;
        }

        // Estados normales → blanco
        return Color.WHITE;
    }
}