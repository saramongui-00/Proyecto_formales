package co.edu.uptc.lenguajesformales.dto;

/**
 * DTO que representa una transición en un autómata finito.
 */
public class TransitionDTO {
    private String fromState;
    private String symbol;
    private String toState;

    /**
     * Constructor de la transición.
     * @param fromState Estado origen.
     * @param symbol Símbolo de transición.
     * @param toState Estado destino.
     */
    public TransitionDTO(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    /**
     * Obtiene el estado origen.
     * @return Estado origen.
     */
    public String getFromState() {
        return fromState;
    }

    /**
     * Obtiene el símbolo de la transición.
     * @return Símbolo de transición.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Obtiene el estado destino.
     * @return Estado destino.
     */
    public String getToState() {
        return toState;
    }

    /**
     * Representación en texto de la transición.
     * @return Cadena con los datos de la transición.
     */
    @Override
    public String toString() {
        return "Transition [fromState=" + fromState + ", symbol=" + symbol + ", toState=" + toState + "]";
    }

}
