package co.edu.uptc.lenguajesformales.dto;

import co.edu.uptc.lenguajesformales.model.AutomatonType;
import co.edu.uptc.lenguajesformales.model.Transition;

import java.util.ArrayList;
import java.util.List;

public class Automaton {
    private AutomatonType type;
    private List<String> states;
    private List<String> alphabet;
    private List<co.edu.uptc.lenguajesformales.model.Transition> transitions;
    private String initialState;
    private List<String> finalStates;

    public Automaton() {
        states =new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        finalStates = new ArrayList<>();
    }

    public AutomatonType getType() {
        return type;
    }

    public void setType(AutomatonType type) {
        this.type = type;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(List<String> finalStates) {
        this.finalStates = finalStates;
    }
}
