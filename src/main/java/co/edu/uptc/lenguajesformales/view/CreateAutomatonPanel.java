package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.Transition;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

public class CreateAutomatonPanel extends JPanel {

    private JPanel tAutomatonPanel;
    private JLabel tAutomatonLbl;
    private JComboBox<String> tAutonatonCB;


    private JTable table;
    private DefaultTableModel model;

    private JButton addStateBtn;
    private JButton removeStateBtn;
    private JButton addSymbolBtn;
    private JButton removeSymbolBtn;
    private JButton generateAutomaton;

    private JPanel buttons;
    private JScrollPane scroll;

    public CreateAutomatonPanel() {
        initComponents();
        configureTable();
        configureEvents();
    }

    private void initComponents() {
        initTAutomaton();
        initTable();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Crear Autómata"));
        add(tAutomatonPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public void initTAutomaton(){
        tAutomatonPanel = new JPanel();
        tAutomatonLbl = new JLabel("Tipo de autómata");
        tAutonatonCB = new JComboBox<>();
        tAutonatonCB.addItem("AFN");
        tAutonatonCB.addItem("AFD");
        tAutomatonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tAutomatonPanel.add(tAutomatonLbl);
        tAutomatonPanel.add(tAutonatonCB);
    }

    public void initTable(){
        table = new JTable();
        scroll = new JScrollPane(table);

        addStateBtn = new JButton("Crear Estado");
        removeStateBtn = new JButton("Eliminar estado");
        addSymbolBtn = new JButton("Crear Símbolo");
        removeSymbolBtn = new JButton("Eliminar Símbolo");
        generateAutomaton = new JButton("Generar Autómata");
        generateAutomaton.setBackground(Color.green);

        buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(addStateBtn);
        buttons.add(removeStateBtn);
        buttons.add(addSymbolBtn);
        buttons.add(removeSymbolBtn);
        buttons.add(Box.createRigidArea(new Dimension(40, 20)));
        buttons.add(generateAutomaton);


    }

    private void configureTable() {

        String[] baseColumns = {"Estado inicial (->)", "Estados finales (*)", "Estados (Q)"};
        model = new DefaultTableModel(null, baseColumns) {

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 1) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        table.setModel(model);
        table.setRowHeight(25);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configureEvents() {
        model.addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                int row = e.getFirstRow();
                Boolean value = (Boolean) model.getValueAt(row, 0);

                if (value != null && value) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (i != row) model.setValueAt(false, i, 0);
                    }
                }
            }
        });

        addStateBtn.addActionListener(e -> addState());
        removeStateBtn.addActionListener(e -> removeState());
        addSymbolBtn.addActionListener(e -> addSymbol());
        removeSymbolBtn.addActionListener(e -> removeSymbol());
    }


    private void addState() {
        Object[] row = new Object[model.getColumnCount()];
        row[0] = model.getRowCount() == 0;
        row[1] = false;
        row[2] = "";

        model.addRow(row);

        int lastRow = model.getRowCount() - 1;
        table.editCellAt(lastRow, 2);
        table.requestFocus();
    }

    private void removeState() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        boolean wasInitial = (Boolean) model.getValueAt(row, 0);
        model.removeRow(row);
        if (model.getRowCount() > 0 && wasInitial) {
            model.setValueAt(true, 0, 0);
        }
    }

    private void addSymbol() {

        String header = "Σ" + (model.getColumnCount() - 2);

        model.addColumn(header);

        TableColumn col = table.getColumnModel().getColumn(model.getColumnCount()-1);
        col.setHeaderValue(header);
    }

    private void removeSymbol() {

        int col = table.getSelectedColumn();
        if (col < 3) return;

        TableColumn column = table.getColumnModel().getColumn(col);
        table.removeColumn(column);
        model.setColumnCount(model.getColumnCount() - 1);
    }

    public ArrayList<String> getStates() {
        ArrayList<String> states = new ArrayList<>();

        for (int row = 0; row < model.getRowCount(); row++) {
            Object value = model.getValueAt(row, 2);
            if (value != null && !value.toString().trim().isEmpty()) {
                states.add(value.toString().trim());
            }
        }
        return states;
    }

    public String getInitialState() {
        for (int row = 0; row < model.getRowCount(); row++) {
            Boolean isInitial = (Boolean) model.getValueAt(row, 0);
            if (isInitial != null && isInitial) {
                return model.getValueAt(row, 2).toString().trim();
            }
        }
        return null;
    }

    public ArrayList<String> getFinalStates() {
        ArrayList<String> finals = new ArrayList<>();

        for (int row = 0; row < model.getRowCount(); row++) {
            Boolean isFinal = (Boolean) model.getValueAt(row, 1);
            if (isFinal != null && isFinal) {
                finals.add(model.getValueAt(row, 2).toString().trim());
            }
        }
        return finals;
    }

    public ArrayList<String> getAlphabet() {
        ArrayList<String> alphabet = new ArrayList<>();

        for (int col = 3; col < model.getColumnCount(); col++) {
            alphabet.add(model.getColumnName(col));
        }
        return alphabet;
    }

    public ArrayList<Transition> getTransitions() {

        ArrayList<Transition> transitions = new ArrayList<>();

        for (int row = 0; row < model.getRowCount(); row++) {

            String fromState = model.getValueAt(row, 2).toString().trim();

            for (int col = 3; col < model.getColumnCount(); col++) {

                String symbol = model.getColumnName(col);
                Object cell = model.getValueAt(row, col);

                if (cell == null) continue;

                String content = cell.toString().trim();
                if (content.isEmpty()) continue;

                String[] destinations = content.split(",");

                for (String dest : destinations) {
                    transitions.add(new Transition(fromState, symbol, dest.trim()));
                }
            }
        }
        return transitions;
    }
}