package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;

/**
 * Botón de opción con texto y color de texto blanco predefinido.
 */
public class OptionButton extends JButton {

    /**
     * Constructor que recibe el texto del botón.
     * @param text Texto del botón.
     */
    public OptionButton(String text){
        setText(text);
        setForeground(Color.WHITE);
    }

}
