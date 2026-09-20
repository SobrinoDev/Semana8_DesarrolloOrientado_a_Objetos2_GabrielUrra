package Gui;

import Gestion_Envios.ControladorDeEnvios;
import Gestion_Pedidos.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaPedidos extends JFrame {

    private static final String[] COLUMNAS = {
            "ID", "Dirección", "Tipo", "Distancia (km)", "Tiempo estimado (min)", "Estado"
    };

    private final ControladorDeEnvios controlador;
    private final DefaultTableModel modeloTabla;

    public VentanaListaPedidos(ControladorDeEnvios controlador) {
        this.controlador = controlador;

        setTitle("Listado de pedidos");
        setSize(640, 360);
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
        for (Pedido pedido : controlador.obtenerPedidos()) {
            modeloTabla.addRow(new Object[]{
                    String.format("%03d", pedido.getIdPedido()),
                    pedido.getDireccionEntrega(),
                    pedido.getClass().getSimpleName(),
                    pedido.getDistanciaKm(),
                    pedido.calcularTiempoEntrega(),
                    pedido.getEstado()
            });
        }
    }
}
