package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EvaluateAutomatonPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;

    private JButton evaluateBtn;
    private JButton traceBtn;
    private JButton epsilonBtn;

    private JPanel buttonsPanel;

    public EvaluateAutomatonPanel() {
        initComponents();
        configureTable();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Evaluar entradas"));

        table = new JTable();
        scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        evaluateBtn = new JButton("Evaluar entradas");
        traceBtn = new JButton("Mirar trazabilidad");
        epsilonBtn = new JButton("Epsilon");

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(evaluateBtn);
        buttonsPanel.add(traceBtn);
        buttonsPanel.add(epsilonBtn);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void configureTable() {

        String[] columns = {"Entrada", "Resultado"};

        model = new DefaultTableModel(columns, 11) {

            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna Entrada es editable (excepto encabezado)
                return column == 0 && row > 0;
            }
        };

        table.setModel(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Primera fila como ejemplo/placeholder
        model.setValueAt("", 0, 0);
        model.setValueAt("", 0, 1);
    }

    // getters para que luego conectes lógica

    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JButton getEvaluateBtn() { return evaluateBtn; }
    public JButton getTraceBtn() { return traceBtn; }
    public JButton getEpsilonBtn() { return epsilonBtn; }
}