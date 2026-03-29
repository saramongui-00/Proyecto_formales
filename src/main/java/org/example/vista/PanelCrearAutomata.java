package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class PanelCrearAutomata extends JPanel {
    private PanelTipoAutomata panelTipoAutomata;
    private PanelEstados panelEstados;
    private PanelAlfabeto panelAlfabeto;

    public PanelCrearAutomata(){
       panelTipoAutomata = new PanelTipoAutomata();
       panelEstados = new PanelEstados();
       panelAlfabeto = new PanelAlfabeto();

        conf();
    }

    public void conf(){
        setLayout(new GridLayout(3, 1, 5, 5));
        add(panelTipoAutomata);
        add(panelEstados);
        add(panelAlfabeto);
    }
}
