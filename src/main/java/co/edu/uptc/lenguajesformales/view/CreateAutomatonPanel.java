package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class CreateAutomatonPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> tAutonatonCB;

    private OptionButton addStateBtn;
    private OptionButton removeStateBtn;
    private OptionButton addSymbolBtn;
    private OptionButton removeSymbolBtn;
    private OptionButton generateAutomatonBtn;

    private JPanel top;
    private JPanel bottom;

    public CreateAutomatonPanel(ActionListener listener) {
        initComponents();
        conf();
        initPanels();
        buttonsConf();
        configureTable();
        configureEvents(listener);
    }

    private void initComponents() {
        top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tAutonatonCB = new JComboBox<>(new String[]{"DFA","NFA"});
        table = new JTable();
        addStateBtn = new OptionButton("Crear Estado");
        removeStateBtn = new OptionButton("Eliminar estado");
        addSymbolBtn = new OptionButton("Crear Símbolo");
        removeSymbolBtn = new OptionButton("Eliminar Símbolo");
        generateAutomatonBtn = new OptionButton("Generar Autómata");
        bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    public void conf(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Crear Autómata"));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void initPanels(){
        top.add(new JLabel("Tipo de autómata"));
        top.add(tAutonatonCB);

        bottom.add(addStateBtn);
        bottom.add(removeStateBtn);
        bottom.add(Box.createRigidArea(new Dimension(30,30)));
        bottom.add(addSymbolBtn);
        bottom.add(removeSymbolBtn);
        bottom.add(Box.createRigidArea(new Dimension(60,30)));
        bottom.add(generateAutomatonBtn);
    }

    public void buttonsConf(){
        generateAutomatonBtn.setBackground(Global.green);
        addStateBtn.setBackground(Global.blue);
        removeStateBtn.setBackground(Global.red);
        addSymbolBtn.setBackground(Global.blue);
        removeSymbolBtn.setBackground(Global.red);
    }

    private void configureTable() {

        model = new DefaultTableModel(null,new String[]{"Estado inicial (->)","Estados finales (*)","Estados (Q)"}) {
            public Class<?> getColumnClass(int column) {
                if(column==0 || column==1) return Boolean.class;
                return String.class;
            }
            public boolean isCellEditable(int r,int c){ return true; }
        };

        table.setModel(model);
        table.setRowHeight(25);

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int col = table.columnAtPoint(e.getPoint());
                    if(col < 3) return;
                    renameColumn(col);
                }
            }
        });

        model.addTableModelListener(e -> {
            if(e.getType()!= TableModelEvent.UPDATE) return;
            int row = e.getFirstRow();
            int col = e.getColumn();
            if(col==0){
               alwaysInitState(row);
            }
            if(col==2){
               editRow(row);
            }
        });
    }

    public void alwaysInitState(int row){
        if(Boolean.TRUE.equals(model.getValueAt(row,0))){
            for(int i=0;i<model.getRowCount();i++)
                if(i!=row) model.setValueAt(false,i,0);
        }else{
            if(getInitialState()==null){
                model.setValueAt(true,row,0);
                showError("Debe existir un estado inicial.");
            }
        }
    }

    public void editRow(int row){
        String edited = model.getValueAt(row,2).toString().trim();

        for(int i=0;i<model.getRowCount();i++){
            if(i==row) continue;
            if(edited.equals(model.getValueAt(i,2)) && !edited.isEmpty()){
                showError("Estado duplicado.");
                removeState();
                return;
            }
        }
    }

    private void configureEvents(ActionListener listener) {
        generateAutomatonBtn.setActionCommand("generateAutomatonBtn");
        generateAutomatonBtn.addActionListener(listener);
        addStateBtn.addActionListener(e -> addState());
        removeStateBtn.addActionListener(e -> removeState());
        addSymbolBtn.addActionListener(e -> addSymbol());
        removeSymbolBtn.addActionListener(e -> removeSymbol());
    }

    private void addState() {
        Object[] row = new Object[model.getColumnCount()];
        row[0] = model.getRowCount()==0;
        row[1] = false;
        row[2] = "";
        model.addRow(row);
    }

    private void removeState() {
        int row = table.getSelectedRow();
        if(row==-1) return;
        boolean wasInitial = Boolean.TRUE.equals(model.getValueAt(row,0));
        model.removeRow(row);
        if(wasInitial && model.getRowCount()>0){
            model.setValueAt(true,0,0);
        }
    }

    private void addSymbol() {
        String symbol = JOptionPane.showInputDialog(this,"Ingrese símbolo:");
        if(symbol==null) return;
        symbol = symbol.trim();
        if(symbol.isEmpty()){ showError("Símbolo vacío"); return; }

        for(int i=3;i<table.getColumnModel().getColumnCount();i++){
            String existing = table.getColumnModel().getColumn(i).getHeaderValue().toString();
            if(existing.equals(symbol)){ showError("Símbolo duplicado"); return; }
        }

        model.addColumn(symbol);
        for(int r=0;r<model.getRowCount();r++)
            model.setValueAt(null,r,model.getColumnCount()-1);
    }

    private void renameColumn(int col) {
        String newName = JOptionPane.showInputDialog(this,"Editar símbolo:");
        if(newName==null || newName.trim().isEmpty()) return;
        newName=newName.trim();

        for(int i=3;i<table.getColumnModel().getColumnCount();i++){
            if(i==col) continue;
            if(table.getColumnModel().getColumn(i).getHeaderValue().toString().equals(newName)){
                showError("Símbolo duplicado.");
                return;
            }
        }

        table.getColumnModel().getColumn(col).setHeaderValue(newName);
        table.getTableHeader().repaint();
    }

    private void removeSymbol() {
        int col = table.getSelectedColumn();
        if(col < 3){
            showError("Seleccione una columna de símbolo.");
            return;
        }

        TableColumn column = table.getColumnModel().getColumn(col);
        table.removeColumn(column);
        model.setColumnCount(model.getColumnCount()-1);
    }

    public void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
    }


    public String getAutomatonType(){
        return tAutonatonCB.getSelectedItem().toString();
    }

    public ArrayList<String> getStates() {
        ArrayList<String> states = new ArrayList<>();
        for(int i=0;i<model.getRowCount();i++){
            String state = model.getValueAt(i,2).toString();
            if(!state.isEmpty()){
                states.add(state);
            }
        }
        return states;
    }

    public String getInitialState() {
        for(int i=0;i<model.getRowCount();i++)
            if(Boolean.TRUE.equals(model.getValueAt(i,0)))
                return model.getValueAt(i,2).toString();
        return null;
    }

    public ArrayList<String> getFinalStates() {
        ArrayList<String> finals = new ArrayList<>();
        for(int i=0;i<model.getRowCount();i++)
            if(Boolean.TRUE.equals(model.getValueAt(i,1)))
                finals.add(model.getValueAt(i,2).toString());
        return finals;
    }

    public ArrayList<String> getAlphabet() {
        ArrayList<String> alphabet = new ArrayList<>();
        for (int col = 3; col < table.getColumnModel().getColumnCount(); col++) {
            String header = table.getColumnModel().getColumn(col).getHeaderValue().toString().trim();
            if (!header.isEmpty()) alphabet.add(header);
        }
        return alphabet;
    }

    public ArrayList<TransitionDTO> getTransitions() {
        ArrayList<TransitionDTO> transitions = new ArrayList<>();

        for(int row = 0; row < model.getRowCount(); row++) {

            Object fromCell = model.getValueAt(row, 2);
            if(fromCell == null) continue;

            String from = fromCell.toString().trim();
            if(from.isEmpty()) continue;

            for(int col = 3; col < model.getColumnCount(); col++) {

                Object cell = model.getValueAt(row, col);
                if(cell == null) continue;

                String content = cell.toString().trim();
                if(content.isEmpty()) continue;

                String symbol = table.getColumnModel()
                        .getColumn(col)
                        .getHeaderValue()
                        .toString()
                        .trim();

                String[] dests = content.split(",");

                for(String d : dests) {
                    String dest = d.trim();
                    if(dest.isEmpty()) continue;
                    if(!getStates().contains(d)) continue;

                    transitions.add(new TransitionDTO(from, symbol, dest));
                }
            }
        }

        return transitions;
    }

    public AutomatonDTO createAutomaton(){
        return new AutomatonDTO(getAutomatonType(), getStates(), getAlphabet(), getTransitions(), getInitialState(), getFinalStates());
    }

    public void loadAutomaton(AutomatonDTO automaton) {

        if (automaton == null) return;

        clearTable();
        tAutonatonCB.setSelectedItem(automaton.getType());
        loadAlphabetColumns(automaton.getAlphabet());
        loadStatesRows(automaton.getStates());
        markInitialState(automaton.getInitialState());
        markFinalStates(automaton.getFinalStates());
        loadTransitions(automaton.getTransitions());
    }

    private void clearTable() {
        model.setRowCount(0);
        model.setColumnCount(3);
    }

    private void loadAlphabetColumns(List<String> alphabet) {
        for (String symbol : alphabet) {
            model.addColumn(symbol);
        }
    }

    private void loadStatesRows(List<String> states) {

        for (String state : states) {
            Object[] row = new Object[model.getColumnCount()];
            row[0] = false;
            row[1] = false;
            row[2] = state;

            model.addRow(row);
        }
    }

    private void markInitialState(String initialState) {
        if (initialState == null) return;

        for (int i = 0; i < model.getRowCount(); i++) {
            String state = model.getValueAt(i, 2).toString();
            if (state.equals(initialState)) {
                model.setValueAt(true, i, 0);
                return;
            }
        }
    }

    private void markFinalStates(List<String> finals) {

        for (int i = 0; i < model.getRowCount(); i++) {
            String state = model.getValueAt(i, 2).toString();
            if (finals.contains(state)) {
                model.setValueAt(true, i, 1);
            }
        }
    }

    private void loadTransitions(List<TransitionDTO> transitions) {

        for (TransitionDTO t : transitions) {

            int row = findRowByState(t.getFromState());
            int col = findColumnBySymbol(t.getSymbol());

            if (row == -1 || col == -1) continue;

            Object currentCell = model.getValueAt(row, col);

            if (currentCell == null || currentCell.toString().isEmpty()) {
                model.setValueAt(t.getToState(), row, col);
            } else {
                String updated = currentCell.toString() + "," + t.getToState();
                model.setValueAt(updated, row, col);
            }
        }
    }

    private int findRowByState(String state) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 2).toString().equals(state))
                return i;
        }
        return -1;
    }

    private int findColumnBySymbol(String symbol) {
        for (int col = 3; col < model.getColumnCount(); col++) {
            String header = table.getColumnModel()
                    .getColumn(col)
                    .getHeaderValue()
                    .toString();

            if (header.equals(symbol))
                return col;
        }
        return -1;
    }


}