package co.edu.uptc.lenguajesformales.model;

import java.util.ArrayList;
import java.util.List;

public class Automaton {
    
    private AutomatonType type;
    private List<String> states;
    private List<String> alphabet;
    private List<Transition> transitions;
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

    public void addState(String state){
        if(!states.contains(state)){
            states.add(state);
        }
    }

    public void addSymbol(String symbol){
        if(!alphabet.contains(symbol)){
            alphabet.add(symbol);
        }
    }
    
    public void addTransition(Transition transition){
        transitions.add(transition);
    }

    public void addFinalState(String state){
        if(!finalStates.contains(state)){
            finalStates.add(state);
        }
    }

    public List<Transition> getTransitions(String state, String symbol){
        List<Transition> result= new ArrayList<>();
        for(Transition t: transitions){
            if(t.getFromState().equals(state) && t.getSymbol().equals(symbol)){
                result.add(t);
            }
        }
        return result;
    }

    /*Check if a state exists */
    public boolean containsState(String state){
        return states.contains(state);
    }

    /*Check if a symbol exists */
    public boolean containsSymbol(String symbol){
        return alphabet.contains(symbol);
    }

    /*Check is a state is final */
    public boolean isFinalState(String state){
        return finalStates.contains(state);
    }

    /*Prints the automaton for testing*/
    public void print() {
        System.out.println("\nAUTOMATON");
        System.out.println("Type: " + type);
        System.out.println("States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Initial State: " + initialState);
        System.out.println("Final States: " + finalStates);
        System.out.println("Transitions:");

        for (Transition t : transitions) {
            System.out.println("  " + t);
        }
    }
}
