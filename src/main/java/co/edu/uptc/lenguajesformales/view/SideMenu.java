package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel del menú lateral con botones para navegar entre las funciones principales.
 */
public class SideMenu extends JPanel {

    private MenuButton createAutomatonBtn;
    private MenuButton evaluateAutomatonBtn;
    private MenuButton saveAutomatonBtn;

    /**
     * Constructor que inicializa los botones del menú con sus íconos y listener.
     * @param listener Listener para los botones.
     */
    public SideMenu(ActionListener listener){
        createAutomatonBtn = new MenuButton("/crear_automata.png");
        evaluateAutomatonBtn = new MenuButton("/probar_automata.png");
        saveAutomatonBtn = new MenuButton("/guardar.png");
        addActionListener(listener);
        conf();
    }

    /**
     * Configura el layout, tamaño y agrega los botones al panel.
     */
    public void conf(){
        setButtonsToolTipText();
        setButtonsActionCommand();
        setPreferredSize(new Dimension(80,200));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(createAutomatonBtn);
        add(evaluateAutomatonBtn);
        add(saveAutomatonBtn);
    }

    /**
     * Asigna textos de ayuda a cada botón.
     */
    public void setButtonsToolTipText(){
        createAutomatonBtn.setToolTipText("Crear Autómata");
        evaluateAutomatonBtn.setToolTipText("Evaluar Autómata");
        saveAutomatonBtn.setToolTipText("Exportar/Importar Autómata");
    }

    /**
     * Asigna comandos de acción a cada botón para identificar el evento.
     */
    public void setButtonsActionCommand(){
        createAutomatonBtn.setActionCommand("createAutomatonBtn");
        evaluateAutomatonBtn.setActionCommand("evaluateAutomatonBtn");
        saveAutomatonBtn.setActionCommand("saveAutomatonBtn");
    }

    /**
     * Agrega el mismo ActionListener a todos los botones del menú.
     * @param listener Listener asignado.
     */
    public void addActionListener(ActionListener listener){
        createAutomatonBtn.addActionListener(listener);
        evaluateAutomatonBtn.addActionListener(listener);
        saveAutomatonBtn.addActionListener(listener);

    }

}