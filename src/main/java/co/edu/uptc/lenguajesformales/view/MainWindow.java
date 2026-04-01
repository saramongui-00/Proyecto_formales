package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {
    private SideMenu sideMenu;
    private JPanel centralPanel;

    private CreateAutomatonPanel createAutomatonPanel;
    private EvaluateAutomatonPanel evaluateAutomatonPanel;

    private ShowAutomatonPanel showAutomatonPanel;

    public MainWindow(){
        sideMenu = new SideMenu(this);
        centralPanel = new JPanel();
        showAutomatonPanel = new ShowAutomatonPanel();
        createAutomatonPanel = new CreateAutomatonPanel(this);
        evaluateAutomatonPanel = new EvaluateAutomatonPanel();
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

    public void saveAutomatonAlert(){
        String[]options = {"Exportar", "Importar"};
        JOptionPane.showOptionDialog(this, "Guardar automata", "Importar/Exportar automata", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    public void generateAutomaton(){
        showAutomatonPanel.setAutomaton(createAutomatonPanel.createAutomaton());
        repaint();
        showAutomatonPanel.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "createAutomatonBtn": changeCreateAutomatonPanel();
                break;
            case "evaluateAutomatonBtn": changeEvaluateAutomatonPanel();
                break;
            case "saveAutomatonBtn":saveAutomatonAlert();
                break;
            case "generateAutomatonBtn": generateAutomaton();
                break;
        }

    }
}
