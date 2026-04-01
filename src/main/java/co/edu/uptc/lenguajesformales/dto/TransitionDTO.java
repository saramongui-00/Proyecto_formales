package co.edu.uptc.lenguajesformales.dto;

public class TransitionDTO {
    private String fromState;
    private String symbol;
    private String toState;

    public TransitionDTO(String fromState, String symbol, String toState) {
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

    @Override
    public String toString() {
        return "Transition [fromState=" + fromState + ", symbol=" + symbol + ", toState=" + toState + "]";
    }

}
