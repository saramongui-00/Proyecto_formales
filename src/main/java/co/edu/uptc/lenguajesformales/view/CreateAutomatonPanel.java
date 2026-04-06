package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

// Panel para la creación de autómatas con una tabla interactiva
public class CreateAutomatonPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private OptionButton addStateBtn;
    private OptionButton removeStateBtn;
    private OptionButton addSymbolBtn;
    private OptionButton removeSymbolBtn;
    private OptionButton generateAutomatonBtn;

    private JPanel top;
    private JPanel bottom;

    // Constructor que inicializa el panel con el listener para eventos
    public CreateAutomatonPanel(ActionListener listener) {
        initComponents();
        conf();
        initPanels();
        buttonsConf();
        configureTable();
        configureEvents(listener);
    }

    // Inicializa los componentes del panel
    private void initComponents() {
        top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        table = new JTable();
        addStateBtn = new OptionButton("Crear Estado");
        removeStateBtn = new OptionButton("Eliminar estado");
        addSymbolBtn = new OptionButton("Crear Símbolo");
        removeSymbolBtn = new OptionButton("Eliminar Símbolo");
        generateAutomatonBtn = new OptionButton("Generar Autómata");
        bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    // Configura el layout y bordes del panel principal
    public void conf(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Crear Autómata"));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // Agrega los botones al panel inferior con espaciadores
    public void initPanels(){
        bottom.add(addStateBtn);
        bottom.add(removeStateBtn);
        bottom.add(Box.createRigidArea(new Dimension(30,30)));
        bottom.add(addSymbolBtn);
        bottom.add(removeSymbolBtn);
        bottom.add(Box.createRigidArea(new Dimension(60,30)));
        bottom.add(generateAutomatonBtn);
    }

    // Asigna colores de fondo a los botones
    public void buttonsConf(){
        generateAutomatonBtn.setBackground(Global.green);
        addStateBtn.setBackground(Global.blue);
        removeStateBtn.setBackground(Global.red);
        addSymbolBtn.setBackground(Global.blue);
        removeSymbolBtn.setBackground(Global.red);
    }

    // Configura la tabla con sus columnas y listeners
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

        // Permite renombrar columnas de símbolos con doble clic
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int col = table.columnAtPoint(e.getPoint());
                    if(col < 3) return;
                    renameColumn(col);
                }
            }
        });

        // Listener para validar cambios en la tabla
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

    // Valida que solo exista un estado inicial en la tabla
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

    // Valida que no haya estados duplicados al editar una celda
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

    // Configura los eventos de los botones
    private void configureEvents(ActionListener listener) {
        generateAutomatonBtn.setActionCommand("generateAutomatonBtn");
        generateAutomatonBtn.addActionListener(listener);
        addStateBtn.addActionListener(e -> addState());
        removeStateBtn.addActionListener(e -> removeState());
        addSymbolBtn.addActionListener(e -> addSymbol());
        removeSymbolBtn.addActionListener(e -> removeSymbol());
    }

    // Agrega una nueva fila (estado) a la tabla
    private void addState() {
        Object[] row = new Object[model.getColumnCount()];
        row[0] = model.getRowCount()==0;
        row[1] = false;
        row[2] = "";
        model.addRow(row);
    }

    // Elimina el estado seleccionado de la tabla
    private void removeState() {
        int row = table.getSelectedRow();
        if(row==-1) return;
        boolean wasInitial = Boolean.TRUE.equals(model.getValueAt(row,0));
        model.removeRow(row);
        if(wasInitial && model.getRowCount()>0){
            model.setValueAt(true,0,0);
        }
    }

    // Agrega un nuevo símbolo como columna en la tabla
    private void addSymbol() {
        String symbol = JOptionPane.showInputDialog(this,"Ingrese símbolo:");
        if(symbol==null) return;

        symbol = symbol.trim();
        if (isInvalidDfaSymbol(symbol)) {
            showError("En un DFA no se permite epsilon ni símbolos vacíos.");
            return;
        }

        for(int i=3;i<table.getColumnModel().getColumnCount();i++){
            String existing = table.getColumnModel().getColumn(i).getHeaderValue().toString();
            if(existing.equals(symbol)){ showError("Símbolo duplicado"); return; }
        }

        model.addColumn(symbol);
        for(int r=0;r<model.getRowCount();r++)
            model.setValueAt(null,r,model.getColumnCount()-1);
    }

    // Renombra una columna de símbolo
    private void renameColumn(int col) {
        String newName = JOptionPane.showInputDialog(this,"Editar símbolo:");
        if(newName==null) return;
        newName=newName.trim();

        if (isInvalidDfaSymbol(newName)) {
            showError("En un DFA no se permite epsilon ni símbolos vacíos.");
            return;
        }

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

    // Valida que el símbolo sea válido para un DFA (sin epsilon ni vacío)
    private boolean isInvalidDfaSymbol(String symbol) {
        return symbol.isEmpty() || "ε".equals(symbol) || "epsilon".equalsIgnoreCase(symbol);
    }

    // Elimina la columna de símbolo seleccionada
    private void removeSymbol() {
        int viewCol = table.getSelectedColumn();
        if(viewCol < 3){
            showError("Seleccione una columna de símbolo.");
            return;
        }

        int modelCol = table.convertColumnIndexToModel(viewCol);

        Vector<String> columnNames = new Vector<>();
        for (int col = 0; col < model.getColumnCount(); col++) {
            if (col != modelCol) {
                columnNames.add(model.getColumnName(col));
            }
        }

        Vector<Vector<Object>> rows = new Vector<>();
        for (int row = 0; row < model.getRowCount(); row++) {
            Vector<Object> newRow = new Vector<>();
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (col != modelCol) {
                    newRow.add(model.getValueAt(row, col));
                }
            }
            rows.add(newRow);
        }

        model.setDataVector(rows, columnNames);
        table.clearSelection();
        table.setRowHeight(25);
    }

    // Muestra un mensaje de error en un diálogo
    public void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    // Retorna la lista de estados del autómata
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

    // Retorna el estado inicial del autómata
    public String getInitialState() {
        for(int i=0;i<model.getRowCount();i++)
            if(Boolean.TRUE.equals(model.getValueAt(i,0)))
                return model.getValueAt(i,2).toString();
        return null;
    }

    // Retorna la lista de estados finales del autómata
    public ArrayList<String> getFinalStates() {
        ArrayList<String> finals = new ArrayList<>();
        for(int i=0;i<model.getRowCount();i++)
            if(Boolean.TRUE.equals(model.getValueAt(i,1)))
                finals.add(model.getValueAt(i,2).toString());
        return finals;
    }

    // Retorna el alfabeto (símbolos) del autómata
    public ArrayList<String> getAlphabet() {
        ArrayList<String> alphabet = new ArrayList<>();
        for (int col = 3; col < table.getColumnModel().getColumnCount(); col++) {
            String header = table.getColumnModel().getColumn(col).getHeaderValue().toString().trim();
            if (!header.isEmpty()) alphabet.add(header);
        }
        return alphabet;
    }

    // Retorna la lista de transiciones del autómata
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

    // Crea y retorna un objeto AutomatonDTO con los datos del panel
    public AutomatonDTO createAutomaton(){
        return new AutomatonDTO(getStates(), getAlphabet(), getTransitions(), getInitialState(), getFinalStates());
    }

    // Carga un autómata existente en el panel
    public void loadAutomaton(AutomatonDTO automaton) {

        if (automaton == null) return;

        clearTable();
        loadAlphabetColumns(automaton.getAlphabet());
        loadStatesRows(automaton.getStates());
        markInitialState(automaton.getInitialState());
        markFinalStates(automaton.getFinalStates());
        loadTransitions(automaton.getTransitions());
    }

    // Limpia toda la tabla
    private void clearTable() {
        model.setRowCount(0);
        model.setColumnCount(3);
    }

    // Carga las columnas del alfabeto en la tabla
    private void loadAlphabetColumns(List<String> alphabet) {
        for (String symbol : alphabet) {
            model.addColumn(symbol);
        }
    }

    // Carga las filas de estados en la tabla
    private void loadStatesRows(List<String> states) {

        for (String state : states) {
            Object[] row = new Object[model.getColumnCount()];
            row[0] = false;
            row[1] = false;
            row[2] = state;

            model.addRow(row);
        }
    }

    // Marca el estado inicial en la tabla
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

    // Marca los estados finales en la tabla
    private void markFinalStates(List<String> finals) {

        for (int i = 0; i < model.getRowCount(); i++) {
            String state = model.getValueAt(i, 2).toString();
            if (finals.contains(state)) {
                model.setValueAt(true, i, 1);
            }
        }
    }

    // Carga las transiciones en la tabla
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

    // Busca la fila correspondiente a un estado
    private int findRowByState(String state) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 2).toString().equals(state))
                return i;
        }
        return -1;
    }

    // Busca la columna correspondiente a un símbolo
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