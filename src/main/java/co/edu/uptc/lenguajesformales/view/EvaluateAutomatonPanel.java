package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

// Panel para evaluar cadenas de entrada en un autómata
public class EvaluateAutomatonPanel extends JPanel {

    private static final String EPSILON_SYMBOL = "ε";

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private OptionButton evaluateBtn;
    private OptionButton traceBtn;
    private OptionButton epsilonBtn;
    private OptionButton clearRowsBtn;
    private JPanel buttonsPanel;

    // Constructor que inicializa el panel con el listener para eventos
    public EvaluateAutomatonPanel(ActionListener listener) {
        initComponents();
        conf();
        configureTable();
        addActionListener(listener);
        setActionCommand();
        setButtonsColors();

    }

    // Inicializa los componentes del panel
    private void initComponents() {
        table = new JTable();
        scroll = new JScrollPane(table);
        evaluateBtn = new OptionButton("Evaluar entradas");
        traceBtn = new OptionButton("Mirar trazabilidad");
        epsilonBtn = new OptionButton("Agregar Epsilon");
        clearRowsBtn = new OptionButton("Limpiar filas");
        clearRowsBtn.addActionListener(e -> clearAllRows());
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(evaluateBtn);
        buttonsPanel.add(traceBtn);
        buttonsPanel.add(epsilonBtn);
        buttonsPanel.add(clearRowsBtn);
    }

    // Configura el layout y bordes del panel principal
    public void conf(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Evaluar entradas"));
        add(scroll, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    // Configura la tabla con las columnas "Entrada" y "Resultado"
    private void configureTable() {

        String[] columns = {"Entrada", "Resultado"};

        model = new DefaultTableModel(columns, 10) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo la columna de entrada es editable
            }
        };

        table.setModel(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.setValueAt("", 0, 0);
        model.setValueAt("", 0, 1);
    }

    // Agrega los ActionListeners a los botones
    public void addActionListener(ActionListener listener){
        evaluateBtn.addActionListener(listener);
        traceBtn.addActionListener(listener);
        epsilonBtn.addActionListener(listener);
    }

    // Asigna los comandos de acción a los botones
    public void setActionCommand(){
        evaluateBtn.setActionCommand("evaluateBtn");
        traceBtn.setActionCommand("traceBtn");
        epsilonBtn.setActionCommand("epsilonBtn");
    }

    // Asigna colores de fondo a los botones
    public void setButtonsColors(){
        evaluateBtn.setBackground(Global.green);
        traceBtn.setBackground(Global.blue);
        epsilonBtn.setBackground(Global.orange);
        clearRowsBtn.setBackground(Global.red);
    }

    // Retorna la entrada seleccionada para ver su trazabilidad
    public String getInputToTrace(){
        commitActiveEdit();

        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            throw new IllegalStateException("Seleccione una entrada para ver la trazabilidad");
        }

        Object value = model.getValueAt(selectedRow, 0);
        return normalizeInput(value == null ? "" : value.toString());
    }

    // Retorna todas las entradas ingresadas en la tabla
    public ArrayList<String> getInputs() {
        commitActiveEdit();

        ArrayList<String> inputs = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {

            Object cell = model.getValueAt(i, 0);
            if (cell == null) continue;

            String input = normalizeInput(cell.toString());
            if (!input.isEmpty() || EPSILON_SYMBOL.equals(cell.toString().trim())) {
                inputs.add(input);
            }
        }

        return inputs;
    }

    // Finaliza la edición activa de la tabla para guardar los cambios pendientes
    private void commitActiveEdit() {
        if (table.isEditing()) {
            TableCellEditor editor = table.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }

    // Agrega el símbolo epsilon en la fila seleccionada o en la primera vacía
    public void addEpsilonInput() {
        int row = table.getSelectedRow();

        if (row < 0) {
            row = findFirstEmptyInputRow();
        }

        if (row < 0) {
            model.addRow(new Object[]{"", ""});
            row = model.getRowCount() - 1;
        }

        model.setValueAt(EPSILON_SYMBOL, row, 0);
        table.setRowSelectionInterval(row, row);
        table.requestFocusInWindow();
    }

    // Busca la primera fila con entrada vacía
    private int findFirstEmptyInputRow() {
        for (int i = 0; i < model.getRowCount(); i++) {
            Object cell = model.getValueAt(i, 0);
            if (cell == null || cell.toString().trim().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    // Normaliza la entrada convirtiendo epsilon a cadena vacía
    private String normalizeInput(String rawInput) {
        String input = rawInput == null ? "" : rawInput.trim();
        if (EPSILON_SYMBOL.equals(input) || "epsilon".equalsIgnoreCase(input)) {
            return "";
        }
        return input;
    }

    // Muestra los resultados de la evaluación en la columna "Resultado"
    public void showEvaluationResults(Map<String, Boolean> results) {
        for (int row = 0; row < model.getRowCount(); row++) {
            model.setValueAt("", row, 1);
        }
        for (int row = 0; row < model.getRowCount(); row++) {
            Object value = model.getValueAt(row, 0);
            if (value == null) continue;

            String rawInput = value.toString().trim();
            String input = normalizeInput(rawInput);
            if (input.isEmpty() && !EPSILON_SYMBOL.equals(rawInput)) continue;

            if (results.containsKey(input)) {
                boolean accepted = results.get(input);
                model.setValueAt(accepted ? "Aceptado" : "Rechazado", row, 1);
            }
        }
    }

    // Limpia todas las filas de la tabla
    private void clearAllRows() {
        commitActiveEdit();
        for (int row = 0; row < model.getRowCount(); row++) {
            model.setValueAt("", row, 0);
            model.setValueAt("", row, 1);
        }
        table.clearSelection();
    }
}