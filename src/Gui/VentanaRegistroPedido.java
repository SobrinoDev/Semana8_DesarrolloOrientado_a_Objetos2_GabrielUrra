package Gui;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaRegistroPedido extends JFrame {

    private final PedidoDAO pedidoDAO;

    private final JTextField campoDireccion = new JTextField();
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});

    public VentanaRegistroPedido(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;

        setTitle("Registrar pedido");
        setSize(340, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(campoDireccion);
        panelFormulario.add(new JLabel("Tipo:"));
        panelFormulario.add(comboTipo);

        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener(e -> guardarPedido());

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonGuardar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void guardarPedido() {
        String direccion = campoDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = (String) comboTipo.getSelectedItem();
        Pedido pedido = new Pedido(direccion, tipo, "PENDIENTE");

        try {
            pedidoDAO.guardar(pedido);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el pedido en la base de datos:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Pedido #" + pedido.getId() + " (" + tipo + ") registrado correctamente.",
                "Pedido registrado", JOptionPane.INFORMATION_MESSAGE);

        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        campoDireccion.requestFocus();
    }
}
