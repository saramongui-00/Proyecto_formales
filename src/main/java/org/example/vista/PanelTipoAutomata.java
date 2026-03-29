package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class PanelTipoAutomata extends JPanel {
    private JComboBox<String> tAutomataCB;

    public PanelTipoAutomata(){
        setBorder(BorderFactory.createTitledBorder("Tipo autómata"));
        tAutomataCB = new JComboBox<>();
        tAutomataCB.addItem("AFN");
        tAutomataCB.addItem("AFD");
        add(tAutomataCB);
        setPreferredSize(new Dimension(150, 60));
    }


}
