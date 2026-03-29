package co.edu.uptc.lenguajesformales.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (type == AutomatonType.AFD) {
            for (Transition t : transitions) {
                if (t.getFromState().equals(transition.getFromState()) &&
                    t.getSymbol().equals(transition.getSymbol())) {
                    throw new IllegalStateException(
                        "Transición no determinista detectada en estado " +
                        transition.getFromState() + " con símbolo " +
                        transition.getSymbol());
                }
            }
        }
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

    /*Verifica si el estado existe */
    public boolean containsState(String state){
        return states.contains(state);
    }

    /*Verifica que el simbolo exista */
    public boolean containsSymbol(String symbol){
        return alphabet.contains(symbol);
    }

    public boolean isFinalState(String state){
        return finalStates.contains(state);
    }

    public boolean evaluate(String input){
        if (input == null) {
            throw new IllegalArgumentException("La cadena no puede ser null");
        }
        if (type != AutomatonType.AFD) {
            throw new IllegalStateException("El automata no es determinista (AFD)");
        }
        if (!validateDFA()) {
            throw new IllegalStateException("AFD inválido");
        }

        String currentState = initialState;
        for (char c: input.toCharArray()){
            String symbol = String.valueOf(c);
            //Validar símbolo
            if (!alphabet.contains(symbol)) {
                return false;
            }
            List<Transition> possibleTransitions = getTransitions(currentState, symbol);

           /*Si no existe transición para ese símbolo la cadena se rechaza*/ 
            if(possibleTransitions.isEmpty()){
                return false;
            }
            currentState = possibleTransitions.get(0).getToState();
        }
        return isFinalState(currentState);
    }

    public List<String> evaluateWithDetailedTrace(String input) {
        if (input == null) {
            throw new IllegalArgumentException("La cadena no puede ser null");
        }
        if (type != AutomatonType.AFD) {
            throw new IllegalStateException("El automata no es determinista (AFD)");
        }

        if (!validateDFA()) {
            throw new IllegalStateException("AFD inválido");
        }

        List<String> trace = new ArrayList<>();

        String currentState = initialState;

        for (char c : input.toCharArray()) {
            String symbol = String.valueOf(c);

            if (!alphabet.contains(symbol)) {
                trace.add("(" + currentState + ", " + symbol + ") -> SIMBOLO INVÁLIDO");
                return trace;
            }

            List<Transition> possibleTransitions = getTransitions(currentState, symbol);

            if (possibleTransitions.isEmpty()) {
                trace.add("(" + currentState + ", " + symbol + ") -> ERROR");
                return trace;
            }

            String nextState = possibleTransitions.get(0).getToState();

            trace.add("(" + currentState + ", " + symbol + ") -> " + nextState);

            currentState = nextState;
        }

        return trace;
    }

    public boolean validateDFA() {
        //Estado inicial
        if (initialState == null || !states.contains(initialState)) {
            return false;
        }
        //Estados finales
        for (String f : finalStates) {
            if (!states.contains(f)) {
                return false;
            }
        }
        //Transiciones válidas
        for (Transition t : transitions) {
            if (!states.contains(t.getFromState())) 
                return false;
            if (!states.contains(t.getToState())) 
                return false;
            if (!alphabet.contains(t.getSymbol())) 
                return false;
        }

        //Determinismo
        for (String state : states) {
            for (String symbol : alphabet) {
                int count = 0;
                for (Transition t : transitions) {
                    if (t.getFromState().equals(state) &&
                        t.getSymbol().equals(symbol)) {
                        count++;
                    }
                }
                if (count > 1) 
                    return false;
            }
        }
        return true;
    }

    public Map<String, Boolean> evaluateBatch(List<String> inputs) {
        if (inputs == null) {
            throw new IllegalArgumentException("La lista de cadenas no puede ser null");
        }
        if (inputs.size() > 10) {
            throw new IllegalArgumentException("Se permiten máximo 10 cadenas");
        }
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar al menos una cadena");
        }

        Map<String, Boolean> results = new HashMap<>();
        for (String input : inputs) {
            boolean result = evaluate(input);
            results.put(input, result);
        }
        return results;
    }

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
