package Gui;

import Concurrencia.Repartidor;
import Gestion_Envios.ControladorDeEnvios;
import Gestion_Pedidos.Pedido;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    // Controlador común: todas las ventanas hijas operan sobre esta misma instancia
    private final ControladorDeEnvios controlador = new ControladorDeEnvios();

    public VentanaPrincipal() {
        setTitle("SpeedFast — Gestión de entregas");
        setSize(380, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("SpeedFast", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton botonRegistrar = new JButton("Registrar pedido");
        botonRegistrar.addActionListener(e -> new VentanaRegistroPedido(controlador));

        JButton botonListar = new JButton("Listar pedidos");
        botonListar.addActionListener(e -> new VentanaListaPedidos(controlador));

        JButton botonAsignar = new JButton("Asignar repartidor / Iniciar entrega");
        botonAsignar.addActionListener(e -> asignarRepartidorEIniciarEntrega());

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonListar);
        panelBotones.add(botonAsignar);

        add(panelBotones, BorderLayout.CENTER);

        setVisible(true);
    }

    private void asignarRepartidorEIniciarEntrega() {
        List<Pedido> pendientes = controlador.obtenerPedidos().stream()
                .filter(pedido -> "Pendiente".equals(pedido.getEstado()))
                .toList();

        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes por asignar.",
                    "Sin pedidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = pendientes.stream()
                .map(pedido -> "#" + String.format("%03d", pedido.getIdPedido()) + " — "
                        + pedido.getClass().getSimpleName() + " — " + pedido.getDireccionEntrega())
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(this, "Selecciona el pedido a asignar:",
                "Asignar repartidor", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) {
            return;
        }
        Pedido pedido = pendientes.get(java.util.Arrays.asList(opciones).indexOf(seleccion));

        String nombreRepartidor = JOptionPane.showInputDialog(this, "Nombre del repartidor:");
        if (nombreRepartidor == null || nombreRepartidor.isBlank()) {
            return;
        }

        controlador.asignarRepartidorAutomatico(pedido);
        controlador.asignarRepartidorManual(pedido, nombreRepartidor.trim());

        JOptionPane.showMessageDialog(this,
                "Repartidor " + nombreRepartidor.trim() + " asignado al pedido #"
                        + String.format("%03d", pedido.getIdPedido()) + ". Iniciando entrega...",
                "Entrega en curso", JOptionPane.INFORMATION_MESSAGE);

        // La entrega se simula en un hilo aparte para no bloquear la interfaz gráfica
        Repartidor repartidor = new Repartidor(nombreRepartidor.trim(), List.of(pedido));
        Thread hiloEntrega = new Thread(() -> {
            repartidor.run();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                    "Entrega del pedido #" + String.format("%03d", pedido.getIdPedido()) + " finalizada.",
                    "Entrega completada", JOptionPane.INFORMATION_MESSAGE));
        });
        hiloEntrega.start();
    }
}
