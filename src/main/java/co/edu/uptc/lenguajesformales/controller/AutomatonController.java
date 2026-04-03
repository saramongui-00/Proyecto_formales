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
        ArrayList<String>inputs = view.getInputs();
        inputs.forEach(i -> System.out.println(i));
    }

    public void generateAutomaton(){
        if(view.generateAutomaton()){
            AutomatonDTO tempAut = view.getAutomaton();
            automaton = new Automaton(tempAut.getType(), tempAut.getStates(), tempAut.getAlphabet(),
                    transitionMapper(tempAut.getTransitions()), tempAut.getInitialState(), tempAut.getFinalStates());
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
