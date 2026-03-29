package org.example.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelEstados extends JPanel {

    private JTable tablaEstados;
    private DefaultTableModel modelo;
    private JButton btnAgregar;
    private JButton btnEliminar;

    public PanelEstados() {
        initComponents();
        configurarTabla();
        configurarEventos();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Estados (Q)"));

        tablaEstados = new JTable();
        btnAgregar = new JButton("Agregar estado");
        btnEliminar = new JButton("Eliminar estado");

        JScrollPane scroll = new JScrollPane(tablaEstados);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarTabla() {

        String[] columnas = {"Estado", "Inicio", "Final"};

        modelo = new DefaultTableModel(null, columnas) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;
                    case 1: return Boolean.class; // radio lógico
                    case 2: return Boolean.class; // checkbox
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        tablaEstados.setModel(modelo);
        tablaEstados.setRowHeight(25);
        tablaEstados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarEventos() {

        // 🔘 Solo un estado inicial permitido
        modelo.addTableModelListener(e -> {
            if (e.getColumn() == 1) {
                int fila = e.getFirstRow();
                Boolean valor = (Boolean) modelo.getValueAt(fila, 1);

                if (valor != null && valor) {
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        if (i != fila) {
                            modelo.setValueAt(false, i, 1);
                        }
                    }
                }
            }
        });

        // ➕ Agregar fila vacía
        btnAgregar.addActionListener(e -> {
            modelo.addRow(new Object[]{"", false, false});

            int ultimaFila = modelo.getRowCount() - 1;
            tablaEstados.editCellAt(ultimaFila, 0);
            tablaEstados.requestFocus();
        });

        // ❌ Eliminar fila seleccionada
        btnEliminar.addActionListener(e -> {

            int filaSeleccionada = tablaEstados.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un estado para eliminar",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            modelo.removeRow(filaSeleccionada);
        });
    }
}