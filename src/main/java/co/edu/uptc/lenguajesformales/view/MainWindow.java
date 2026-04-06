package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

// Ventana principal de la aplicación que contiene todos los paneles
public class MainWindow extends JFrame {
    private SideMenu sideMenu;
    private JSplitPane contentSplitPane;

    private CreateAutomatonPanel createAutomatonPanel;
    private EvaluateAutomatonPanel evaluateAutomatonPanel;

    private ShowAutomatonPanel showAutomatonPanel;
    private JFileChooser exportFile;
    private JFileChooser importFile;
    private AutomatonTraceDialog automatonTraceDialog;

    // Constructor que inicializa la ventana con el listener para eventos
    public MainWindow(ActionListener listener){
        sideMenu = new SideMenu(listener);
        showAutomatonPanel = new ShowAutomatonPanel();
        createAutomatonPanel = new CreateAutomatonPanel(listener);
        evaluateAutomatonPanel = new EvaluateAutomatonPanel(listener);
        importFile = new JFileChooser();
        exportFile = new JFileChooser();
        conf();
    }

    // Configura la ventana principal con layout y componentes
    public void conf(){
        contentSplitPane = createSplitPane(createAutomatonPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(sideMenu, BorderLayout.WEST);
        add(contentSplitPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Crea un panel dividido horizontalmente entre el panel izquierdo y el de visualización
    private JSplitPane createSplitPane(JComponent leftPanel){
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, showAutomatonPanel);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.20);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.20);
        return splitPane;
    }

    // Cambia el panel izquierdo al panel de creación de autómatas
    public void changeCreateAutomatonPanel(){
        contentSplitPane.setLeftComponent(createAutomatonPanel);
        revalidate();
        repaint();
    }

    // Cambia el panel izquierdo al panel de evaluación de entradas
    public void changeEvaluateAutomatonPanel(){
        contentSplitPane.setLeftComponent(evaluateAutomatonPanel);
        revalidate();
        repaint();
    }

    // Muestra un mensaje de error en un diálogo
    public void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    // Muestra un mensaje informativo en un diálogo
    public void showMessage(String message){
        JOptionPane.showMessageDialog(this,message,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    // Muestra un diálogo para preguntar si exportar, importar o cancelar
    public int saveAutomatonAlert(){
        String[]options = {"Exportar", "Importar", "Cancelar"};
        return JOptionPane.showOptionDialog(this, "¿Que acción desea realizar?", "Guardar cambios", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    // Abre diálogo para exportar el autómata a un archivo JSON
    public String exportAutomaton(){
        exportFile.setDialogTitle("Guardar autómata como JSON");

        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Archivos JSON (*.json)", "json");
        exportFile.setFileFilter(filter);
        exportFile.setSelectedFile(new File("automata.json"));

        int result = exportFile.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = exportFile.getSelectedFile();
            if (!file.getAbsolutePath().toLowerCase().endsWith(".json")) {
                file = new File(file.getAbsolutePath() + ".json");
            }
            return file.getAbsolutePath();
        }

        return null;
    }

    // Abre diálogo para importar un autómata desde un archivo JSON
    public String importAutomaton(){
        importFile.setDialogTitle("Select JSON file");

        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("JSON Files (*.json)", "json");
        importFile.setFileFilter(filter);

        int result = importFile.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = importFile.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }

        return null;
    }

    // Retorna el autómata creado en el panel de creación
    public AutomatonDTO getAutomaton(){
        return createAutomatonPanel.createAutomaton();
    }

    // Valida que el autómata tenga todos los elementos necesarios antes de generarlo
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
            val = true;
        }
        return val;
    }

    // Retorna las entradas ingresadas en el panel de evaluación
    public ArrayList<String> getInputs(){
        return evaluateAutomatonPanel.getInputs();
    }

    // Agrega un símbolo epsilon en el panel de evaluación
    public void addEpsilonInput(){
        evaluateAutomatonPanel.addEpsilonInput();
    }

    // Muestra los resultados de evaluación en el panel correspondiente
    public void showEvaluationResults(Map<String, Boolean> results){
        evaluateAutomatonPanel.showEvaluationResults(results);
    }

    // Dibuja el autómata en el panel de visualización
    public void drawAutomaton(){
        showAutomatonPanel.setAutomaton(createAutomatonPanel.createAutomaton());
    }

    // Carga un autómata existente en el panel de creación
    public void loadAutomaton(AutomatonDTO automaton){
        createAutomatonPanel.loadAutomaton(automaton);
    }

    // Retorna la entrada seleccionada para ver su trazabilidad
    public String getInputToTrace(){
        return evaluateAutomatonPanel.getInputToTrace();
    }

    // Inicializa y muestra el diálogo de trazabilidad
    public void initTraceDialog(int rowCount, List<String> detailedTrace){
        automatonTraceDialog = new AutomatonTraceDialog(rowCount, detailedTrace);
    }

}