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

        automaton.setType(AutomatonType.AFN);

        // Estados
        automaton.addState("q0");
        automaton.addState("q1");
        automaton.addState("q2");
        automaton.addState("q3");

        // Alfabeto
        automaton.addSymbol("a");
        automaton.addSymbol("b");

        // Inicial
        automaton.setInitialState("q0");

        // Final
        automaton.addFinalState("q3");

        // Transiciones
        automaton.addTransition(new Transition("q0", "a", "q0"));
        automaton.addTransition(new Transition("q0", "b", "q0"));
        automaton.addTransition(new Transition("q0", "a", "q1"));
        automaton.addTransition(new Transition("q1", "b", "q2"));
        automaton.addTransition(new Transition("q2", "a", "q3"));
        automaton.addTransition(new Transition("q3", "a", "q3"));
        automaton.addTransition(new Transition("q3", "b", "q3"));
        
        if (!automaton.validateAFN()) {
            throw new IllegalStateException("AFN inválido");
        }
    }

    // PRUEBAS

    private void testEvaluate() {
        view.showMessage("\nEVALUATE");

        showResult("aba");
        showResult("ab");
        showResult("");
        showResult("aaaaaaababaaa");
        showResult("bababbababa");
        showResult("bbbbaaaa");
        showResult("bbb");
        showResult("aaaaaaab");
    }

    private void showResult(String input) {
        try {
            boolean result = automaton.evaluateAFN(input);
            view.showMessage(input + " -> " + result);
        } catch (IllegalStateException e) {
            view.showMessage("Error al evaluar '" + input + "': " + e.getMessage());
        }
    }

    private void testTrace() {
        view.showMessage("\nTRACE");
        try {
            List<String> trace = automaton.evaluateAFDWithDetailedTrace("aba");
            for (String step : trace) {
                view.showMessage(step);
            }
        } catch (IllegalStateException e) {
            view.showMessage("Error en TRACE: " + e.getMessage());
        }
    }

    private void testBatch() {
        view.showMessage("\nBATCH");
        List<String> inputs = List.of("aba", "ab", "", "aaaaaaababaaa", "bababbababa", "bbbbaaaa", "bbb");
        try {
            Map<String, Boolean> results = automaton.evaluateBatchAutomaton(inputs);
            for (Map.Entry<String, Boolean> entry : results.entrySet()) {
                view.showMessage(entry.getKey() + " -> " + entry.getValue());
            }
        } catch (IllegalStateException e) {
            view.showMessage("Error en BATCH: " + e.getMessage());
        }
    }

    
}
