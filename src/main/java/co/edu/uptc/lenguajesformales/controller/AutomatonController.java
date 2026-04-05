package co.edu.uptc.lenguajesformales.controller;


import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;
import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.model.Transition;
import co.edu.uptc.lenguajesformales.persistence.AutomatonPersistence;
import co.edu.uptc.lenguajesformales.view.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutomatonController implements ActionListener {
    private Automaton automaton;
    private MainWindow view;
    private AutomatonPersistence persistence;

    public AutomatonController(){
        automaton = new Automaton();
        view = new MainWindow(this);
        persistence = new AutomatonPersistence();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "createAutomatonBtn": view.changeCreateAutomatonPanel();
                break;
            case "evaluateAutomatonBtn": view.changeEvaluateAutomatonPanel();
                break;
            case "saveAutomatonBtn":saveAutomaton();
                break;
            case "generateAutomatonBtn": generateAutomaton();
                break;
            case "evaluateBtn": evaluateInputs();
                break;
            case "traceBtn": traceAutomaton();
                break;
            case "epsilonBtn":
                view.addEpsilonInput();
                break;

        }
    }

    public void traceAutomaton(){
        try {
            String inputToTrace = view.getInputToTrace();
            List<String> detailedTrace = automaton.evaluateAFDWithDetailedTrace(inputToTrace);
            view.initTraceDialog(detailedTrace.size(), detailedTrace);
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void saveAutomaton(){
        int option = view.saveAutomatonAlert();
        switch (option){
            case 0/*Exportar*/:exportAutomaton();
                break;
            case 1/*Importar*/: importAutomaton();
                break;
            default:
                break;
        }
    }

    public void exportAutomaton(){
        try {
            if(automaton.getStates().isEmpty()){
                view.showError("Tiene que crear el autómata");
            }else {
                String filePath = view.exportAutomaton();
                persistence.exportToJson(automaton, filePath);
                view.showMessage("Automata exportado exitosamente");
            }
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void importAutomaton(){
        try {
            String filePath = view.importAutomaton();
            automaton = persistence.importFromJson(filePath);
            view.loadAutomaton(new AutomatonDTO(automaton.getType().toString(), automaton.getStates(), automaton.getAlphabet(),
                    transitionDTOMapper(automaton.getTransitions()), automaton.getInitialState(), automaton.getFinalStates()));
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void evaluateInputs(){
        try {
            if(automaton.getStates().isEmpty()){
                view.showError("Tiene que crear el autómata");
            }else{
                ArrayList<String> inputs = view.getInputs();
                Map<String, Boolean> results = automaton.evaluateBatchAutomaton(inputs);
                view.showEvaluationResults(results);
            }
        } catch (Exception e) {
           view.showError(e.getMessage());
        }

    }

    public void generateAutomaton() {

        if (!view.generateAutomaton()) return;

        AutomatonDTO dto = view.getAutomaton();

        Automaton newAutomaton = new Automaton(dto.getType(),dto.getStates(),dto.getAlphabet(),transitionMapper(dto.getTransitions()),
                dto.getInitialState(),dto.getFinalStates());

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

    public List<TransitionDTO> transitionDTOMapper(List<Transition> dtoList){
        return dtoList.stream()
                .map(dto -> new TransitionDTO(
                        dto.getFromState(),
                        dto.getSymbol(),
                        dto.getToState()))
                .toList();
    }
}
