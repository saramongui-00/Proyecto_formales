package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JPanel {

    private MenuButton createAutomatonBtn;
    private MenuButton evaluateAutomatonBtn;
    private MenuButton traceabilityAutomatonBtn;
    private MenuButton saveAutomatonBtn;

    public SideMenu(){
        createAutomatonBtn = new MenuButton("/crear_automata.png");
        evaluateAutomatonBtn = new MenuButton("/probar_automata.png");
        traceabilityAutomatonBtn = new MenuButton("/trazabilidad.png");
        saveAutomatonBtn = new MenuButton("/guardar.png");
        conf();
    }

    public void conf(){
        setPreferredSize(new Dimension(80,200));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.black);
        add(createAutomatonBtn);
        add(evaluateAutomatonBtn);
        add(traceabilityAutomatonBtn);
        add(saveAutomatonBtn);
    }
}
