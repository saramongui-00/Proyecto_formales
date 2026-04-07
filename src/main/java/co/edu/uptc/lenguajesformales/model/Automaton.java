package co.edu.uptc.lenguajesformales.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Representa un Autómata Finito Determinista (DFA).
 * Garantiza que para cada estado y símbolo exista como máximo una transición.
 */
public class Automaton {
    
    private List<String> states;
    private List<String> alphabet;
    private List<Transition> transitions;
    private String initialState;
    private List<String> finalStates;
    
    /**
     * Constructor vacío.
     */
    public Automaton() {
        states =new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        finalStates = new ArrayList<>();
    }

    /**
     * Constructor con parámetros.
     * @param states Lista de estados del autómata.
     * @param alphabet Alfabeto del autómata.
     * @param transitions Lista de transiciones.
     * @param initialState Estado inicial.
     * @param finalStates Lista de estados finales.
     */
    public Automaton(List<String> states, List<String> alphabet,
                     List<Transition> transitions, String initialState, List<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }


    /**
     * Obtiene la lista de estados.
     * @return Lista de estados.
     */
    public List<String> getStates() {
        return states;
    }

    /**
     * Establece la lista de estados.
     * @param states Lista de estados.
     */
    public void setStates(List<String> states) {
        this.states = states;
    }

    /**
     * Obtiene el alfabeto.
     * @return Lista del alfabeto.
     */
    public List<String> getAlphabet() {
        return alphabet;
    }

    /**
     * Establece el alfabeto.
     * @param alphabet Lista del alfabeto.
     */
    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Obtiene las transiciones.
     * @return Lista de transiciones.
     */
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Establece las transiciones.
     * @param transitions Lista de transiciones.
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Obtiene el estado inicial.
     * @return Estado inicial.
     */
    public String getInitialState() {
        return initialState;
    }

    /**
     * Establece el estado inicial.
     * @param initialState Estado inicial.
     */
    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    /**
     * Obtiene los estados finales.
     * @return Lista de estados finales.
     */
    public List<String> getFinalStates() {
        return finalStates;
    }

    /**
     * Establece los estados finales.
     * @param finalStates Lista de estados finales.
     */
    public void setFinalStates(List<String> finalStates) {
        this.finalStates = finalStates;
    }

    /**
     * Métodos básicos
     */

    /**
     * Agrega un estado si no existe.
     * @param state Estado a agregar.
     */
    public void addState(String state){
        if(!states.contains(state)){
            states.add(state);
        }
    }

    /**
     * Agrega un símbolo al alfabeto.
     * @param symbol Símbolo a agregar.
     */
    public void addSymbol(String symbol){
        String normalizedSymbol = symbol == null ? "" : symbol.trim();
        if(normalizedSymbol.isEmpty() || isEpsilonSymbol(normalizedSymbol)){
            throw new IllegalArgumentException("DFA no permite simbolos epsilon ni vacios");
        }
        if(!alphabet.contains(normalizedSymbol)){
            alphabet.add(normalizedSymbol);
        }
    }
    
    /**
     * Agrega una transición validando que no rompa el determinismo
     * @param transition Transición a agregar.
     */
    public void addTransition(Transition transition){
        //Validar estado origen
        if(!states.contains(transition.getFromState())){
            throw new IllegalArgumentException("El estado origen no existe: " + transition.getFromState());
        }
        
        //Validar estado destino
         if(!states.contains(transition.getToState())){
            throw new IllegalArgumentException("El estado destino no existe: " + transition.getToState());
        }

        //Validar símbolo
        if(!alphabet.contains(transition.getSymbol())){
            throw new IllegalArgumentException("El símbolo no existe en el alfabeto: " + transition.getSymbol());
        }

        //Validar determinismo
        for (Transition t : transitions) {
                if (t.getFromState().equals(transition.getFromState()) &&
                    t.getSymbol().equals(transition.getSymbol())) {
                    throw new IllegalStateException(
                        "Transición no determinista detectada en estado " +
                        transition.getFromState() + " con símbolo " +
                        transition.getSymbol());
                }
            }
        transitions.add(transition);
    }

    /**
     * Agrega estados finales
     * @param state Estado final a agregar.
     */
    public void addFinalState(String state){
        if(!finalStates.contains(state)){
            finalStates.add(state);
        }
    }

    /**
     * Obtiene la transición única para un estado y símbolo.
     * @param state Estado.
     * @param symbol Símbolo.
     * @return Transición correspondiente, o null si no existe.
     */
    public Transition getTransition(String state, String symbol){
        for(Transition t : transitions){
            if(t.getFromState().equals(state) && t.getSymbol().equals(symbol)){
                return t;
            }
        }
        return null;
    }

    /** Verifica si el estado existe 
     * @param state Estado a verificar.
     * @return true si existe, false en caso contrario.
     */
    public boolean containsState(String state){
        return states.contains(state);
    }

    /** Verifica que el simbolo exista 
     * @param symbol Símbolo a verificar.
     * @return true si existe, false en caso contrario.
     */
    public boolean containsSymbol(String symbol){
        return alphabet.contains(symbol);
    }

    /** Verifica que exista al menos un estado de aceptación 
     * @param state Estado a verificar.
     * @return true si es estado final, false en caso contrario.
     */
    public boolean isFinalState(String state){
        return finalStates.contains(state);
    }


    /** Valida el DFA verificando que tenga estado inicial, final y transiciones únicas por pares 
     * @return true si es válido, false en caso contrario.
     */
    public boolean validateDFA() {
        //Estado inicial
        if (initialState == null || !states.contains(initialState)) {
            return false;
        }
        //Alfabeto válido para DFA 
        for (String symbol : alphabet) {
            if (symbol == null || symbol.trim().isEmpty() || isEpsilonSymbol(symbol.trim())) {
                return false;
            }
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

    private boolean isEpsilonSymbol(String symbol) {
        return "ε".equals(symbol) || "epsilon".equalsIgnoreCase(symbol);
    }

    /**
     * Evalúa una cadena en el DFA 
     * @param input Cadena a evaluar.
     * @return true si es aceptada, false en caso contrario.
     */
    public boolean evaluateAutomaton(String input) {
        if (!validateDFA()) throw new IllegalStateException("DFA inválido");
        return evaluateDFA(input);

    }

    /**
     * Evalúa múltiples cadenas (máximo 10).
     * @param inputs Lista de cadenas a evaluar.
     * @return Mapa con resultados de evaluación.
     */
    public Map<String, Boolean> evaluateBatchAutomaton(List<String> inputs) {
        if (inputs == null || inputs.isEmpty()) throw new IllegalArgumentException("Debe ingresar al menos una cadena");
        if (inputs.size() > 10) throw new IllegalArgumentException("Se permiten máximo 10 cadenas");

        Map<String, Boolean> results = new HashMap<>();
        for (String input : inputs) results.put(input, evaluateAutomaton(input));
        return results;
    }

    /**
     * Lógica principal de evaluación del DFA
     * @param input Cadena a evaluar.
     * @return true si es aceptada, false en caso contrario.
     */
    public boolean evaluateDFA(String input){

        if (input == null) {
            throw new IllegalArgumentException("La cadena no puede ser null");
        }

        String currentState = initialState;

            for (char c: input.toCharArray()){
                String symbol = String.valueOf(c);
                if (!alphabet.contains(symbol)) {
                    return false;
                }

                Transition t = getTransition(currentState, symbol);
                if(t == null){
                    return false;
                }

                currentState = t.getToState();
            }

            return isFinalState(currentState);
        }

    /**
     * Devuelve el recorrido paso a paso del DFA 
     * @param input Cadena a evaluar.
     * @return Lista con el trace del recorrido.
     */
    public List<String> evaluateWithTrace(String input) {

        List<String> trace = new ArrayList<>();
        String currentState = initialState;

        for (char c : input.toCharArray()) {
            String symbol = String.valueOf(c);

            if (!alphabet.contains(symbol)) {
                trace.add("(" + currentState + ", " + symbol + ") -> símbolo inválido");
                return trace;
            }

            Transition t = getTransition(currentState, symbol);

            if (t == null) {
                trace.add("(" + currentState + ", " + symbol + ") -> error");
                return trace;
            }

            trace.add("(" + currentState + ", " + symbol + ") -> " + t.getToState());
            currentState = t.getToState();
        }

        return trace;
    }

    /**
     * Imprime el autómata.
     */
   public void print() {
        System.out.println("\nAUTOMATON DFA");
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
