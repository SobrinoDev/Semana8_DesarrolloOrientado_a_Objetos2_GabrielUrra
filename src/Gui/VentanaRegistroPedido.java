package Gui;

import Gestion_Envios.ControladorDeEnvios;
import Gestion_Pedidos.Pedido;
import Gestion_Pedidos.PedidoComida;
import Gestion_Pedidos.PedidoEncomienda;
import Gestion_Pedidos.PedidoExpress;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private final ControladorDeEnvios controlador;

    private final JTextField campoId = new JTextField();
    private final JTextField campoDireccion = new JTextField();
    private final JTextField campoDistancia = new JTextField();
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Comida", "Encomienda", "Express"});

    public VentanaRegistroPedido(ControladorDeEnvios controlador) {
        this.controlador = controlador;

        setTitle("Registrar pedido");
        setSize(360, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(campoId);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(campoDireccion);
        panelFormulario.add(new JLabel("Distancia (km):"));
        panelFormulario.add(campoDistancia);
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
        int id;
        double distanciaKm;

        try {
            id = Integer.parseInt(campoId.getText().trim());
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero positivo.",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean idRepetido = controlador.obtenerPedidos().stream()
                .anyMatch(pedido -> pedido.getIdPedido() == id);
        if (idRepetido) {
            JOptionPane.showMessageDialog(this, "Ya existe un pedido registrado con el ID " + id + ".",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String direccion = campoDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            distanciaKm = Double.parseDouble(campoDistancia.getText().trim());
            if (distanciaKm <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La distancia debe ser un número positivo.",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = (String) comboTipo.getSelectedItem();
        Pedido pedido = switch (tipo) {
            case "Comida" -> new PedidoComida(id, direccion, distanciaKm, true);
            case "Encomienda" -> new PedidoEncomienda(id, direccion, distanciaKm, 5.0, true);
            case "Express" -> new PedidoExpress(id, direccion, distanciaKm, true);
            default -> throw new IllegalStateException("Tipo de pedido no reconocido: " + tipo);
        };

        controlador.registrarPedido(pedido);

        JOptionPane.showMessageDialog(this,
                "Pedido #" + String.format("%03d", id) + " (" + tipo + ") registrado correctamente.",
                "Pedido registrado", JOptionPane.INFORMATION_MESSAGE);

        campoId.setText("");
        campoDireccion.setText("");
        campoDistancia.setText("");
        comboTipo.setSelectedIndex(0);
        campoId.requestFocus();
    }
}
