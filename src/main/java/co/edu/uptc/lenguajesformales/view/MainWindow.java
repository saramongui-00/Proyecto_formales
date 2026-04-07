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

/**
 * Ventana principal de la aplicación que contiene todos los paneles
 */
public class MainWindow extends JFrame {
    private SideMenu sideMenu;
    private JSplitPane contentSplitPane;

    private CreateAutomatonPanel createAutomatonPanel;
    private EvaluateAutomatonPanel evaluateAutomatonPanel;

    private ShowAutomatonPanel showAutomatonPanel;
    private JFileChooser exportFile;
    private JFileChooser importFile;
    private AutomatonTraceDialog automatonTraceDialog;

    /**
     * Constructor que inicializa la ventana con el listener para eventos
     * @param listener Listener para eventos.
     */
    public MainWindow(ActionListener listener){
        sideMenu = new SideMenu(listener);
        showAutomatonPanel = new ShowAutomatonPanel();
        createAutomatonPanel = new CreateAutomatonPanel(listener);
        evaluateAutomatonPanel = new EvaluateAutomatonPanel(listener);
        importFile = new JFileChooser();
        exportFile = new JFileChooser();
        conf();
    }

    /**
     * Configura la ventana principal con layout y componentes
     */
    public void conf(){
        contentSplitPane = createSplitPane(createAutomatonPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(sideMenu, BorderLayout.WEST);
        add(contentSplitPane, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Crea un panel dividido horizontalmente entre el panel izquierdo y el de visualización
     * @param leftPanel Panel izquierdo.
     * @return JSplitPane configurado.
     */
    private JSplitPane createSplitPane(JComponent leftPanel){
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, showAutomatonPanel);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.20);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.20);
        return splitPane;
    }

    /**
     * Cambia el panel izquierdo al panel de creación de autómatas
     */
    public void changeCreateAutomatonPanel(){
        contentSplitPane.setLeftComponent(createAutomatonPanel);
        revalidate();
        repaint();
    }

    /**
     * Cambia el panel izquierdo al panel de evaluación de entradas
     */
    public void changeEvaluateAutomatonPanel(){
        contentSplitPane.setLeftComponent(evaluateAutomatonPanel);
        revalidate();
        repaint();
    }

    /**
     * Muestra un mensaje de error en un diálogo
     * @param message Mensaje de error.
     */
    public void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje informativo en un diálogo
     * @param message Mensaje informativo.
     */
    public void showMessage(String message){
        JOptionPane.showMessageDialog(this,message,"Información",JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un diálogo para preguntar si exportar, importar o cancelar
     * @return Opción seleccionada.
     */
    public int saveAutomatonAlert(){
        String[]options = {"Exportar", "Importar", "Cancelar"};
        return JOptionPane.showOptionDialog(this, "¿Que acción desea realizar?", "Guardar cambios", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    /**
     * Abre diálogo para exportar el autómata a un archivo JSON
     * @return Ruta del archivo seleccionado, o null.
     */
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

    /**
     * Abre diálogo para importar un autómata desde un archivo JSON
     * @return Ruta del archivo seleccionado, o null.
     */
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

    /**
     * Retorna el autómata creado en el panel de creación
     * @return AutomatonDTO creado.
     */
    public AutomatonDTO getAutomaton(){
        return createAutomatonPanel.createAutomaton();
    }

    /**
     * Valida que el autómata tenga todos los elementos necesarios antes de generarlo
     * @return true si es válido, false en caso contrario.
     */
    public boolean generateAutomaton(){
        boolean val = false;
        try {
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
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
        return val;
    }

    /**
     * Retorna las entradas ingresadas en el panel de evaluación
     * @return Lista de entradas.
     */
    public ArrayList<String> getInputs(){
        return evaluateAutomatonPanel.getInputs();
    }

    /**
     * Agrega un símbolo epsilon en el panel de evaluación
     */
    public void addEpsilonInput(){
        evaluateAutomatonPanel.addEpsilonInput();
    }

    /**
     * Muestra los resultados de evaluación en el panel correspondiente
     * @param results Mapa con resultados.
     */
    public void showEvaluationResults(Map<String, Boolean> results){
        evaluateAutomatonPanel.showEvaluationResults(results);
    }

    /**
     * Dibuja el autómata en el panel de visualización
     */
    public void drawAutomaton(){
        showAutomatonPanel.setAutomaton(createAutomatonPanel.createAutomaton());
    }

    /**
     * Carga un autómata existente en el panel de creación
     * @param automaton AutomatonDTO a cargar.
     */
    public void loadAutomaton(AutomatonDTO automaton){
        createAutomatonPanel.loadAutomaton(automaton);
    }

    /**
     * Retorna la entrada seleccionada para ver su trazabilidad
     * @return Entrada seleccionada.
     */
    public String getInputToTrace(){
        return evaluateAutomatonPanel.getInputToTrace();
    }

    /**
     * Inicializa y muestra el diálogo de trazabilidad
     * @param rowCount Número de filas.
     * @param detailedTrace Lista con el trace detallado.
     */
    public void initTraceDialog(int rowCount, List<String> detailedTrace){
        automatonTraceDialog = new AutomatonTraceDialog(rowCount, detailedTrace);
    }

}