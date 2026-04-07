package co.edu.uptc.lenguajesformales.model;


/**
 * Representa una transición de un autómata finito determinista (DFA).
 * Define el estado de origen, el símbolo leído y el estado destino
 */
public class Transition {
    
    private String fromState;
    private String symbol;
    private String toState;
    
    /**
     * Constructor de la transición.
     * @param fromState Estado origen.
     * @param symbol Símbolo de entrada.
     * @param toState Estado destino.
     */
    public Transition(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    /**
     * Obtiene el estado de origen.
     * @return Estado de origen.
     */
    public String getFromState() {
        return fromState;
    }

    /**
     * Obtiene el símbolo.
     * @return Símbolo de entrada.
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
     * @return Cadena con la representación de la transición.
     */
    @Override
    public String toString() {
        return "Transition [fromState=" + fromState + ", symbol=" + symbol + ", toState=" + toState + "]";
    }

}
