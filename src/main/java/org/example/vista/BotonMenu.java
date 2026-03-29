package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class BotonMenu extends JToggleButton {

    public BotonMenu(String rutaImagen){
        setIcon(new ImageIcon(getClass().getResource(rutaImagen)));
        setPreferredSize(new Dimension(70,70));
        setBackground(Color.LIGHT_GRAY);
    }
}
