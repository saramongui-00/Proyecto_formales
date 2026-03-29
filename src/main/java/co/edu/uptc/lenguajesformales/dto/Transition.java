package co.edu.uptc.lenguajesformales.dto;

public class Transition {

    private String fromState;
    private String symbol;
    private String toState;

    public Transition(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    public String getFromState() { return fromState; }
    public String getSymbol() { return symbol; }
    public String getToState() { return toState; }
}