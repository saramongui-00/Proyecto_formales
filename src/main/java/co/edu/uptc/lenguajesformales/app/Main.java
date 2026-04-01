package co.edu.uptc.lenguajesformales.app;

import co.edu.uptc.lenguajesformales.controller.AutomatonController;
import co.edu.uptc.lenguajesformales.model.Automaton;
import co.edu.uptc.lenguajesformales.view.MainWindow;

public class Main {
    
    public static void main(String[] args) {
    
        Automaton model = new Automaton();
        MainWindow view = new MainWindow();

        AutomatonController controller = 
            new AutomatonController(model, view);

        controller.start();
    }
}
