package co.edu.uptc.lenguajesformales.model;


/*
Representa una transición de un autómata finito determinista (DFA).
Define el estado de origen, el símbolo leído y el estado destino
*/
public class Transition {
    
    private String fromState;
    private String symbol;
    private String toState;
    
    /*
    Constructor de la transición.
    
    fromstate -> Estado origen
    symbol    -> Símbolo de entrada
    toState   -> Estado destino
    */
    public Transition(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getToState() {
        return toState;
    }

    /*
    Representación en texto de la transición.
    */
    @Override
    public String toString() {
        return "Transition [fromState=" + fromState + ", symbol=" + symbol + ", toState=" + toState + "]";
    }

}
