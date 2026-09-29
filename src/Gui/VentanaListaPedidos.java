package Gui;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class VentanaListaPedidos extends JFrame {

    private static final String[] COLUMNAS = {"ID", "Dirección", "Tipo", "Estado"};

    private final PedidoDAO pedidoDAO;
    private final DefaultTableModel modeloTabla;

    public VentanaListaPedidos(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;

        setTitle("Listado de pedidos");
        setSize(520, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton botonRefrescar = new JButton("Refrescar");
        botonRefrescar.addActionListener(e -> cargarPedidos());

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonRefrescar);
        add(panelBoton, BorderLayout.SOUTH);

        cargarPedidos();
        setVisible(true);
    }

    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
        try {
            for (Pedido pedido : pedidoDAO.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                        pedido.getId(), pedido.getDireccion(), pedido.getTipo(), pedido.getEstado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los pedidos desde la base de datos:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
