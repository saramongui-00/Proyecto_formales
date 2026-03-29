package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private JPanel panelCentral;
    private PanelCrearAutomata panelCrearAutomata;
    private JPanel panelMostrarAutomata;
    private MenuLateral menuLateral;

    public VentanaPrincipal(){
        panelCentral = new JPanel();
        panelCrearAutomata = new PanelCrearAutomata();
        panelMostrarAutomata = new JPanel();
        menuLateral = new MenuLateral();

        conf();
    }

    public void conf(){
        panelCentral.setLayout(new BoxLayout(panelCentral,BoxLayout.X_AXIS));

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(menuLateral, BorderLayout.WEST);


        panelMostrarAutomata.setBackground(Color.green);
        panelCentral.add(panelCrearAutomata);
        panelCentral.add(panelMostrarAutomata);

        add(panelCentral);


        setVisible(true);

    }

}
