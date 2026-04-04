package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AutomatonTraceDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;

    public AutomatonTraceDialog(int rowCount, List<String> detailedTrace){
        table = new JTable();
        scroll = new JScrollPane(table);
        configureTable(rowCount, detailedTrace);
        conf();
        setVisible(true);
    }

    public void conf(){
        setSize(500,400);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(scroll);
    }

    public void configureTable(int rowCount, List<String> detailedTrace){
        String[] column = {"Pasos"};
        model = new DefaultTableModel(column, rowCount);
        table.setModel(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTrace(detailedTrace);
    }

    public void setTrace(List<String> detailedTrace){
        for(int i = 0; i < detailedTrace.size(); i++){
            model.setValueAt(detailedTrace.get(i), i, 0);
        }
    }

}
