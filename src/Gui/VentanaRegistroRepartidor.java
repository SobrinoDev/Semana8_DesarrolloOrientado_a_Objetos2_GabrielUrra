package Gui;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaRegistroRepartidor extends JFrame {

    private final RepartidorDAO repartidorDAO;
    private final JTextField campoNombre = new JTextField();

    public VentanaRegistroRepartidor(RepartidorDAO repartidorDAO) {
        this.repartidorDAO = repartidorDAO;

        setTitle("Registrar repartidor");
        setSize(320, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(1, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);

        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener(e -> guardarRepartidor());

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonGuardar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void guardarRepartidor() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.",
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Repartidor repartidor = new Repartidor(nombre);
        try {
            repartidorDAO.guardar(repartidor);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el repartidor en la base de datos:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Repartidor #" + repartidor.getId() + " (" + nombre + ") registrado correctamente.",
                "Repartidor registrado", JOptionPane.INFORMATION_MESSAGE);

        campoNombre.setText("");
        campoNombre.requestFocus();
    }
}
