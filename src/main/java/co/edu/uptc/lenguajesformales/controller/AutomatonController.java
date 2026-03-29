package co.edu.uptc.lenguajesformales.controller;

import java.util.List;
import java.util.Map;

import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.model.AutomatonType;
import co.edu.uptc.lenguajesformales.model.Transition;
import co.edu.uptc.lenguajesformales.view.MainWindow;

public class AutomatonController {

    private Automaton automaton;
    private MainWindow view;
    
    public AutomatonController(Automaton automaton, MainWindow view) {
        this.automaton = automaton;
        this.view = view;
    }

    public void start() {

        view.showMessage("AUTOMATON SYSTEM");

        try{
        setupAutomaton();
        }catch (IllegalStateException e){
            view.showMessage("Error: "+e.getMessage());
            return;
        }

        automaton.print();

        testEvaluate();
        testTrace();
        testBatch();
    }

    // AFD DE PRUEBA
    private void setupAutomaton() {

        automaton.setType(AutomatonType.AFD);

        // Estados
        automaton.addState("q0");
        automaton.addState("q1");
        automaton.addState("q2");
        automaton.addState("q3");
        automaton.addState("q4");

        // Alfabeto
        automaton.addSymbol("0");
        automaton.addSymbol("1");

        // Inicial
        automaton.setInitialState("q0");

        // Final
        automaton.addFinalState("q4");

        // Transiciones
        automaton.addTransition(new Transition("q0", "0", "q1"));
        automaton.addTransition(new Transition("q1", "0", "q2"));
        automaton.addTransition(new Transition("q2", "0", "q2"));
        automaton.addTransition(new Transition("q2", "1", "q3"));
        automaton.addTransition(new Transition("q3", "1", "q3"));
        automaton.addTransition(new Transition("q3", "0", "q4"));
        automaton.addTransition(new Transition("q4", "0", "q2"));
        automaton.addTransition(new Transition("q4", "1", "q3"));
        
        if (!automaton.validateDFA()) {
            throw new IllegalStateException("AFD inválido: no se pueden evaluar cadenas");
        }
    }

    // PRUEBAS

    private void testEvaluate() {
        view.showMessage("\nEVALUATE");

        showResult("00000");
        showResult("00100");
        showResult("1111111");
        showResult("101010");
        showResult("011100010");
        showResult("0011110");
        showResult("00010");
        showResult("");
    }

    private void showResult(String input) {
        try {
            boolean result = automaton.evaluate(input);
            view.showMessage(input + " -> " + result);
        } catch (IllegalStateException e) {
            view.showMessage("Error al evaluar '" + input + "': " + e.getMessage());
        }
    }

    private void testTrace() {
        view.showMessage("\nTRACE");
        try {
            List<String> trace = automaton.evaluateWithDetailedTrace("00010");
            for (String step : trace) {
                view.showMessage(step);
            }
        } catch (IllegalStateException e) {
            view.showMessage("Error en TRACE: " + e.getMessage());
        }
    }

    private void testBatch() {
        view.showMessage("\nBATCH");
        List<String> inputs = List.of("00000", "00100", "1111111", "101010", "011100010", "0011110", "00010");
        try {
            Map<String, Boolean> results = automaton.evaluateBatch(inputs);
            for (Map.Entry<String, Boolean> entry : results.entrySet()) {
                view.showMessage(entry.getKey() + " -> " + entry.getValue());
            }
        } catch (IllegalStateException e) {
            view.showMessage("Error en BATCH: " + e.getMessage());
        }
    }

    
}
