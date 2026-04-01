package co.edu.uptc.lenguajesformales.view;

import co.edu.uptc.lenguajesformales.dto.AutomatonDTO;
import co.edu.uptc.lenguajesformales.dto.TransitionDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CreateAutomatonPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> tAutonatonCB;

    private JButton addStateBtn;
    private JButton removeStateBtn;
    private JButton addSymbolBtn;
    private JButton removeSymbolBtn;
    private JButton generateAutomatonBtn;

    public CreateAutomatonPanel(ActionListener listener) {
        initComponents();
        configureTable();
        configureEvents(listener);
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Crear Autómata"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tAutonatonCB = new JComboBox<>(new String[]{"AFN","AFD"});
        top.add(new JLabel("Tipo de autómata"));
        top.add(tAutonatonCB);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        addStateBtn = new JButton("Crear Estado");
        removeStateBtn = new JButton("Eliminar estado");
        addSymbolBtn = new JButton("Crear Símbolo");
        removeSymbolBtn = new JButton("Eliminar Símbolo");
        generateAutomatonBtn = new JButton("Generar Autómata");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(addStateBtn);
        bottom.add(removeStateBtn);
        bottom.add(addSymbolBtn);
        bottom.add(removeSymbolBtn);
        bottom.add(generateAutomatonBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    private void configureTable() {

        model = new DefaultTableModel(null,
                new String[]{"->","*","Estados"}) {

            public Class<?> getColumnClass(int column) {
                if(column==0 || column==1) return Boolean.class;
                return String.class;
            }
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
    }

    private void configureEvents(ActionListener listener) {
        generateAutomatonBtn.setActionCommand("generateAutomatonBtn");
        generateAutomatonBtn.addActionListener(listener);
        addStateBtn.addActionListener(e -> addState());
        removeStateBtn.addActionListener(e -> removeState());
        addSymbolBtn.addActionListener(e -> addSymbol());
        removeSymbolBtn.addActionListener(e -> removeSymbol());
        generateAutomatonBtn.addActionListener(e -> generateAutomaton());
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
        if(row!=-1) model.removeRow(row);
    }


    private void addSymbol() {

        String symbol = JOptionPane.showInputDialog(this,"Ingrese símbolo:");
        if(symbol==null) return;
        symbol = symbol.trim();

        if(symbol.isEmpty()){
            showError("Símbolo vacio");
            return;
        }

        for(int i=3;i<model.getColumnCount();i++){
            if(model.getColumnName(i).equals(symbol)){
                showError("El símbolo " + symbol + " ya existe");
                return;
            }
        }

        model.addColumn(symbol);

        for(int row=0; row<model.getRowCount(); row++){
            model.setValueAt(null,row,model.getColumnCount()-1);
        }
    }

    private void renameColumn(int col) {

        String newName = JOptionPane.showInputDialog(this, "Editar símbolo:");
        if (newName == null) return;

        newName = newName.trim();
        if (newName.isEmpty()) return;

        for (int i = 3; i < table.getColumnModel().getColumnCount(); i++) {
            if (i == col) continue;

            String existing = table.getColumnModel()
                    .getColumn(i)
                    .getHeaderValue()
                    .toString()
                    .trim();

            if (existing.equals(newName)) {
                showError("El símbolo '" + newName + "' ya existe en el alfabeto.");
                return;
            }
        }

        // renombrar encabezado
        table.getColumnModel().getColumn(col).setHeaderValue(newName);
        table.getTableHeader().repaint();
    }

    private void removeSymbol() {
        int col = table.getSelectedColumn();
        if(col<3) return;
        TableColumn column = table.getColumnModel().getColumn(col);
        table.removeColumn(column);
        model.setColumnCount(model.getColumnCount()-1);
    }

    public void generateAutomaton(){
        ArrayList<TransitionDTO>transitions = getTransitions();
        transitions.forEach(t -> System.out.println("from: "+t.getFromState() +
                                                                "to:" + t.getToState() +
                                                                "symbol:" + t.getSymbol()));


    }

    public boolean validateDuplicatedStates(){
        ArrayList<String>states = getStates();
        return states.stream().distinct().count() < states.size();
    }

    public boolean validateDuplicatedSymbols(){
        ArrayList<String>alphabet = getAlphabet();
        return alphabet.stream().distinct().count() < alphabet.size();
    }

    public void showError(String message){
        JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    //GETTERS

    public String getAutomatonType(){
        return tAutonatonCB.getSelectedItem().toString();
    }

    public ArrayList<String> getStates() {
        ArrayList<String> states = new ArrayList<>();
        for(int i=0;i<model.getRowCount();i++)
            states.add(model.getValueAt(i,2).toString());
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
            String header = table.getColumnModel()
                    .getColumn(col)
                    .getHeaderValue()
                    .toString()
                    .trim();

            if (!header.isEmpty()) {
                alphabet.add(header);
            }
        }
        return alphabet;
    }

    public ArrayList<TransitionDTO> getTransitions() {
        ArrayList<TransitionDTO> transitions = new ArrayList<>();
        for(int row=0;row<model.getRowCount();row++){
            String from = model.getValueAt(row,2).toString();
            for(int col=3;col<model.getColumnCount();col++){
                Object cell = model.getValueAt(row,col);
                if(cell==null) continue;
                String[] dests = cell.toString().split(",");
                for(String d : dests)
                    transitions.add(new TransitionDTO(from,model.getColumnName(col),d.trim()));
            }
        }
        return transitions;
    }

    public AutomatonDTO createAutomaton(){
        return new AutomatonDTO(getAutomatonType(), getStates(), getAlphabet(), getTransitions(), getInitialState(), getFinalStates());
    }


}