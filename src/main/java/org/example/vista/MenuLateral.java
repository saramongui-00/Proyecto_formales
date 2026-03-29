package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class MenuLateral extends JPanel {
    private BotonMenu crearAutomataBtn;
    private BotonMenu evaluarAutomataBtn;
    private BotonMenu trazabilidadAutomataBtn;
    private BotonMenu guardarAutomataBtn;

    public MenuLateral(){
        crearAutomataBtn = new BotonMenu("/crear_automata.png");
        evaluarAutomataBtn = new BotonMenu("/probar_automata.png");
        trazabilidadAutomataBtn = new BotonMenu("/trazabilidad.png");
        guardarAutomataBtn = new BotonMenu("/guardar.png");
        conf();
    }

    public void conf(){
        setPreferredSize(new Dimension(80,200));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.black);
        add(crearAutomataBtn);
        add(evaluarAutomataBtn);
        add(trazabilidadAutomataBtn);
        add(guardarAutomataBtn);
    }
}
