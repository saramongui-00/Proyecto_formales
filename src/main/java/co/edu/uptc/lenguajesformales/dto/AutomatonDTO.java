package co.edu.uptc.lenguajesformales.dto;
import java.util.ArrayList;
import java.util.List;

public class AutomatonDTO {
    private List<String> states;
    private List<String> alphabet;
    private List<TransitionDTO> transitions;
    private String initialState;
    private List<String> finalStates;

    public AutomatonDTO() {
        states =new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        finalStates = new ArrayList<>();
    }

    public AutomatonDTO(List<String> states, List<String> alphabet, List<TransitionDTO> transitions, String initialState, List<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
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

    public List<TransitionDTO> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<TransitionDTO> transitions) {
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
