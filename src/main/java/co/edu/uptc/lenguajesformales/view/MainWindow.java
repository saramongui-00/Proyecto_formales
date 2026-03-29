package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import java.awt.*;
import java.util.IdentityHashMap;

public class MainWindow extends JFrame{
    private SideMenu sideMenu;
    private JPanel centralPanel;
    private CreateAutomatonPanel createAutomatonPanel;
    private JPanel showAutomatonPanel;

    public MainWindow(){
        sideMenu = new SideMenu();
        centralPanel = new JPanel();
        showAutomatonPanel = new JPanel();
        createAutomatonPanel = new CreateAutomatonPanel();

        conf();
    }

    public void conf(){
        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.X_AXIS));

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(sideMenu, BorderLayout.WEST);


        showAutomatonPanel.setBackground(Color.green);

        centralPanel.add(createAutomatonPanel);
        centralPanel.add(showAutomatonPanel);

        add(centralPanel);


        setVisible(true);
    }



}
