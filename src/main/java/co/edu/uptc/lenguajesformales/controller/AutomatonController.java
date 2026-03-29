package co.edu.uptc.lenguajesformales.controller;

import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.view.MainWindow;

public class AutomatonController {

    private Automaton automaton;
    private MainWindow view;
    
    public AutomatonController(Automaton automaton, MainWindow view) {
        this.automaton = automaton;
        this.view = view;
    }

    public void start(){

        view.showMessage("AUTOMATON SYSTEM");
        
    }
    
}
