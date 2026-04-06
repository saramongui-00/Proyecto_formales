package co.edu.uptc.lenguajesformales.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Diálogo que muestra el trazo de ejecución de un autómata en una tabla
public class AutomatonTraceDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;

    // Constructor que inicializa el diálogo con el número de filas y el trazo detallado
    public AutomatonTraceDialog(int rowCount, List<String> detailedTrace){
        table = new JTable();
        scroll = new JScrollPane(table);
        configureTable(rowCount, detailedTrace);
        conf();
        setVisible(true);
    }

    // Configura las propiedades básicas del diálogo como tamaño, layout y componentes
    public void conf(){
        setSize(500,400);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(scroll);
    }

    // Configura la tabla creando el modelo con una columna y el número de filas especificado
    public void configureTable(int rowCount, List<String> detailedTrace){
        String[] column = {"Pasos"};
        model = new DefaultTableModel(column, rowCount);
        table.setModel(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTrace(detailedTrace);
    }

    // Llena la tabla con los pasos del trazo de ejecución del autómata
    public void setTrace(List<String> detailedTrace){
        for(int i = 0; i < detailedTrace.size(); i++){
            model.setValueAt(detailedTrace.get(i), i, 0);
        }
    }

}