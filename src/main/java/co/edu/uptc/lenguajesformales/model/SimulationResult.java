package co.edu.uptc.lenguajesformales.model;

/*Represents the result of evaluating a string */
public class SimulationResult {
    
    private String input;
    private boolean accepted;
   
    public SimulationResult(String input, boolean accepted) {
        this.input = input;
        this.accepted = accepted;
    }

    public String getInput() {
        return input;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public String toString() {
        return "Input=" + input + "\" -> "+ (accepted ? "ACCEPTED" : "REJECTED")+ "]";
    }
    
}
