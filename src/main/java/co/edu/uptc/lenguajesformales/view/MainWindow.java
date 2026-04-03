package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private SideMenu sideMenu;
    private JPanel centralPanel;

    private CreateAutomatonPanel createAutomatonPanel;
    private EvaluateAutomatonPanel evaluateAutomatonPanel;

    private ShowAutomatonPanel showAutomatonPanel;

    public MainWindow(ActionListener listener){
        sideMenu = new SideMenu(listener);
        centralPanel = new JPanel();
        showAutomatonPanel = new ShowAutomatonPanel();
        createAutomatonPanel = new CreateAutomatonPanel(listener);
        evaluateAutomatonPanel = new EvaluateAutomatonPanel(listener);
        conf();
    }

    public void conf(){
        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.X_AXIS));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(sideMenu, BorderLayout.WEST);
        centralPanel.add(createAutomatonPanel);
        centralPanel.add(showAutomatonPanel);
        add(centralPanel);
        setVisible(true);
    }

    public void changeCreateAutomatonPanel(){
        centralPanel.removeAll();
        centralPanel.add(createAutomatonPanel);
        centralPanel.add(showAutomatonPanel);
        repaint();
    }

    public void changeEvaluateAutomatonPanel(){
        centralPanel.removeAll();
        centralPanel.add(evaluateAutomatonPanel);
        centralPanel.add(showAutomatonPanel);
        centralPanel.repaint();
        revalidate();
        repaint();
    }

    public void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public void saveAutomatonAlert(){
        String[]options = {"Exportar", "Importar", "Cancelar"};
        JOptionPane.showOptionDialog(this, "¿Que acción desea realizar?", "Guardar cambios", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    public AutomatonDTO getAutomaton(){
        return createAutomatonPanel.createAutomaton();
    }

    public boolean generateAutomaton(){
        boolean val = false;
        if (createAutomatonPanel.getStates().isEmpty()) {
            showError("Debe existir al menos un estado");
        } else if (createAutomatonPanel.getAlphabet().isEmpty()) {
            showError("Debe existir al menos un símbolo en el alfabeto");
        } else if (createAutomatonPanel.getTransitions().isEmpty()) {
            showError("Debe existir al menos una transicion");
        } else if(createAutomatonPanel.getFinalStates().isEmpty()) {
            showError("Debe existir al menos un estado final");
        } else{
            showAutomatonPanel.setAutomaton(createAutomatonPanel.createAutomaton());
            val = true;
        }
        return val;
    }


    public ArrayList<String> getInputs(){
        return evaluateAutomatonPanel.getInputs();
    }

}
