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
        if (state.equals(automaton.getInitialState())) {
            return Color.GREEN;
        }
        if (automaton.getFinalStates().contains(state)) {
            return Color.RED;
        }
        return Color.WHITE;
    }
}