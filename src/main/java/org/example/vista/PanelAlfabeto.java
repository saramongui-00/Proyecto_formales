package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class PanelAlfabeto extends JPanel {
    private JLabel labelAlfabeto;
    private JTextField alfabetoTF;

    public PanelAlfabeto(){
        labelAlfabeto = new JLabel("Σ = ");
        alfabetoTF = new JTextField(50);
        conf();
    }

    public void conf(){
        setBorder(BorderFactory.createTitledBorder("Alfabeto (Σ)"));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(labelAlfabeto);
        add(alfabetoTF);
    }
}
