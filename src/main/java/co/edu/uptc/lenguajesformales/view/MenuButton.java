package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;

/**
 * Botón personalizado para el menú lateral con ícono y tamaño fijo.
 */
public class MenuButton extends JButton {

    /**
     * Crea un botón de menú con un ícono y tamaño prefijado.
     * @param ImagePath Ruta del recurso de imagen para el ícono.
     */
    public MenuButton(String ImagePath){
        setIcon(new ImageIcon(getClass().getResource(ImagePath)));
        setPreferredSize(new Dimension(70,70));
        setBackground(Color.LIGHT_GRAY);
    }

}
