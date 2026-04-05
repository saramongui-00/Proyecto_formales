package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SideMenu extends JPanel {

    private MenuButton createAutomatonBtn;
    private MenuButton evaluateAutomatonBtn;
    private MenuButton saveAutomatonBtn;

    public SideMenu(ActionListener listener){
        createAutomatonBtn = new MenuButton("/crear_automata.png");
        evaluateAutomatonBtn = new MenuButton("/probar_automata.png");
        saveAutomatonBtn = new MenuButton("/guardar.png");
        addActionListener(listener);
        conf();
    }

    public void conf(){
        setButtonsToolTipText();
        setButtonsActionCommand();
        setPreferredSize(new Dimension(80,200));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(createAutomatonBtn);
        add(evaluateAutomatonBtn);
        add(saveAutomatonBtn);
    }

    public void setButtonsToolTipText(){
        createAutomatonBtn.setToolTipText("Crear Autómata");
        evaluateAutomatonBtn.setToolTipText("Evaluar Autómata");
        saveAutomatonBtn.setToolTipText("Exportar/Importar Autómata");
    }

    public void setButtonsActionCommand(){
        createAutomatonBtn.setActionCommand("createAutomatonBtn");
        evaluateAutomatonBtn.setActionCommand("evaluateAutomatonBtn");
        saveAutomatonBtn.setActionCommand("saveAutomatonBtn");
    }

    public void addActionListener(ActionListener listener){
        createAutomatonBtn.addActionListener(listener);
        evaluateAutomatonBtn.addActionListener(listener);
        saveAutomatonBtn.addActionListener(listener);

    }



}
