package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class EvaluateAutomatonPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private OptionButton evaluateBtn;
    private OptionButton traceBtn;
    private OptionButton epsilonBtn;
    private JPanel buttonsPanel;

    public EvaluateAutomatonPanel(ActionListener listener) {
        initComponents();
        conf();
        configureTable();
        addActionListener(listener);
        setActionCommand();
        setButtonsColors();

    }

    private void initComponents() {
        table = new JTable();
        scroll = new JScrollPane(table);
        evaluateBtn = new OptionButton("Evaluar entradas");
        traceBtn = new OptionButton("Mirar trazabilidad");
        epsilonBtn = new OptionButton("Agregar Epsilon");
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(evaluateBtn);
        buttonsPanel.add(traceBtn);
        buttonsPanel.add(epsilonBtn);
    }

    public void conf(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Evaluar entradas"));
        add(scroll, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void configureTable() {

        String[] columns = {"Entrada", "Resultado"};

        model = new DefaultTableModel(columns, 10) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table.setModel(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.setValueAt("", 0, 0);
        model.setValueAt("", 0, 1);
    }

    public void addActionListener(ActionListener listener){
        evaluateBtn.addActionListener(listener);
        traceBtn.addActionListener(listener);
        epsilonBtn.addActionListener(listener);
    }

    public void setActionCommand(){
        evaluateBtn.setActionCommand("evaluateBtn");
        traceBtn.setActionCommand("traceBtn");
        epsilonBtn.setActionCommand("epsilonBtn");
    }

    public void setButtonsColors(){
        evaluateBtn.setBackground(Global.green);
        traceBtn.setBackground(Global.blue);
        epsilonBtn.setBackground(Global.orange);
    }

    public ArrayList<String> getInputs() {
        ArrayList<String> inputs = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {

            Object cell = model.getValueAt(i, 0);
            if (cell == null) continue;   // ← evitar NullPointer

            String input = cell.toString().trim();
            if (!input.isEmpty()) {
                inputs.add(input);
            }
        }

        return inputs;
    }

    public void showEvaluationResults(Map<String, Boolean> results) {
        for (int row = 0; row < model.getRowCount(); row++) {
            model.setValueAt("", row, 1);
        }
        for (int row = 0; row < model.getRowCount(); row++) {
            Object value = model.getValueAt(row, 0);
            if (value == null) continue;

            String input = value.toString().trim();
            if (input.isEmpty()) continue;

            if (results.containsKey(input)) {
                boolean accepted = results.get(input);
                model.setValueAt(accepted ? "Aceptado" : "Rechazado", row, 1);
            }
        }
    }
}