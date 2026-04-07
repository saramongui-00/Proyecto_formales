package co.edu.uptc.lenguajesformales.dto;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO que representa los datos de un autómata finito determinista.
 */
public class AutomatonDTO {
    private List<String> states;
    private List<String> alphabet;
    private List<TransitionDTO> transitions;
    private String initialState;
    private List<String> finalStates;

    /**
     * Constructor vacío que inicializa las listas internas.
     */
    public AutomatonDTO() {
        states =new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        finalStates = new ArrayList<>();
    }

    /**
     * Constructor con parámetros.
     * @param states Lista de estados.
     * @param alphabet Alfabeto.
     * @param transitions Lista de transiciones.
     * @param initialState Estado inicial.
     * @param finalStates Lista de estados finales.
     */
    public AutomatonDTO(List<String> states, List<String> alphabet, List<TransitionDTO> transitions, String initialState, List<String> finalStates) {
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
     * @return Lista de símbolos.
     */
    public List<String> getAlphabet() {
        return alphabet;
    }

    /**
     * Establece el alfabeto.
     * @param alphabet Lista de símbolos.
     */
    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Obtiene las transiciones.
     * @return Lista de transiciones.
     */
    public List<TransitionDTO> getTransitions() {
        return transitions;
    }

    /**
     * Establece las transiciones.
     * @param transitions Lista de transiciones.
     */
    public void setTransitions(List<TransitionDTO> transitions) {
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
}
