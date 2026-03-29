package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {

    public MenuButton(String ImagePath){
        setIcon(new ImageIcon(getClass().getResource(ImagePath)));
        setPreferredSize(new Dimension(70,70));
        setBackground(Color.LIGHT_GRAY);
    }

}
