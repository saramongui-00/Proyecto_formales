package co.edu.uptc.lenguajesformales.controller;


import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;
import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.model.Transition;
import co.edu.uptc.lenguajesformales.view.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutomatonController implements ActionListener {
    private Automaton automaton;
    private MainWindow view;

    public AutomatonController(){
        automaton = new Automaton();
        view = new MainWindow(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "createAutomatonBtn": view.changeCreateAutomatonPanel();
                break;
            case "evaluateAutomatonBtn": view.changeEvaluateAutomatonPanel();
                break;
            case "saveAutomatonBtn":view.saveAutomatonAlert();
                break;
            case "generateAutomatonBtn": generateAutomaton();
                break;
            case "evaluateBtn": evaluateInputs();
                break;
            case "traceBtn":
                break;
            case "epsilonBtn":
                break;

        }
    }

    public void evaluateInputs(){
        ArrayList<String> inputs = view.getInputs();
        Map<String, Boolean> results = automaton.evaluateBatchAutomaton(inputs);
        view.showEvaluationResults(results);
    }

    public void generateAutomaton() {

        if (!view.generateAutomaton()) return;

        AutomatonDTO dto = view.getAutomaton();

        Automaton newAutomaton = new Automaton(
                dto.getType(),
                dto.getStates(),
                dto.getAlphabet(),
                transitionMapper(dto.getTransitions()),
                dto.getInitialState(),
                dto.getFinalStates()
        );

        boolean isValid = false;

        switch (dto.getType()) {
            case "DFA":
                isValid = newAutomaton.validateDFA();
                if (!isValid) {
                    view.showError("DFA no válido");
                }
                break;

            case "NFA":
                isValid = newAutomaton.validateNFA();
                if (!isValid) {
                    view.showError("NFA no válido");
                }
                break;
        }

        if (isValid) {
            this.automaton = newAutomaton;
            view.drawAutomaton();
        }
    }

    public List<Transition> transitionMapper(List<TransitionDTO> dtoList){
        return dtoList.stream()
                .map(dto -> new Transition(
                        dto.getFromState(),
                        dto.getSymbol(),
                        dto.getToState()))
                .toList();
    }
}
