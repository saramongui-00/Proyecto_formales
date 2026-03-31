package co.edu.uptc.lenguajesformales.controller;

import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.persistence.AutomatonPersistence;
import co.edu.uptc.lenguajesformales.view.MainWindow;

public class AutomatonController {

    private Automaton automaton;
    private MainWindow view;
    
    public AutomatonController(Automaton automaton, MainWindow view) {
        this.automaton = automaton;
        this.view = view;
    }

    public void exportAutomaton(String filePath) {
        try {
            AutomatonPersistence persistence = new AutomatonPersistence();
            persistence.exportToJson(automaton, filePath);

        } catch (Exception e) {
            System.out.println("Error exportando automata: " + e.getMessage());
        }
    }

    public void importAutomaton(String filePath) {
        try {
            AutomatonPersistence persistence = new AutomatonPersistence();
            automaton = persistence.importFromJson(filePath);
            System.out.println("Autóma importado exitosamente desde " + filePath);
        } catch (Exception e) {
            System.out.println("Error importando automata: " + e.getMessage());
        }
    }

    public Automaton getAutomaton() {
        return automaton;
    }

    public void start(){
        view.showMessage("AUTOMATON SYSTEM");
    }
    
}
