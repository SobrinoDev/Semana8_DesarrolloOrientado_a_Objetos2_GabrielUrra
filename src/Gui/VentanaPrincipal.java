package Gui;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    // DAOs compartidos: todas las ventanas hijas operan contra la misma base de datos
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    public VentanaPrincipal() {
        setTitle("SpeedFast — Gestión de entregas");
        setSize(380, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("SpeedFast", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton botonRegistrarPedido = new JButton("Registrar pedido");
        botonRegistrarPedido.addActionListener(e -> new VentanaRegistroPedido(pedidoDAO));

        JButton botonRegistrarRepartidor = new JButton("Registrar repartidor");
        botonRegistrarRepartidor.addActionListener(e -> new VentanaRegistroRepartidor(repartidorDAO));

        JButton botonListar = new JButton("Listar pedidos");
        botonListar.addActionListener(e -> new VentanaListaPedidos(pedidoDAO));

        JButton botonAsignar = new JButton("Asignar repartidor / Iniciar entrega");
        botonAsignar.addActionListener(e -> asignarRepartidorEIniciarEntrega());

        panelBotones.add(botonRegistrarPedido);
        panelBotones.add(botonRegistrarRepartidor);
        panelBotones.add(botonListar);
        panelBotones.add(botonAsignar);

        add(panelBotones, BorderLayout.CENTER);

        setVisible(true);
    }

    private void asignarRepartidorEIniciarEntrega() {
        List<Pedido> pendientes;
        List<Repartidor> repartidores;
        try {
            pendientes = pedidoDAO.listarTodos().stream()
                    .filter(pedido -> "PENDIENTE".equals(pedido.getEstado()))
                    .toList();
            repartidores = repartidorDAO.listarTodos();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
            return;
        }

        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay pedidos con estado PENDIENTE por asignar.",
                    "Sin pedidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay repartidores registrados. Registra uno primero.",
                    "Sin repartidores", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opcionesPedido = pendientes.stream()
                .map(pedido -> "#" + pedido.getId() + " — " + pedido.getTipo() + " — " + pedido.getDireccion())
                .toArray(String[]::new);
        String seleccionPedido = (String) JOptionPane.showInputDialog(this, "Selecciona el pedido a asignar:",
                "Asignar repartidor", JOptionPane.QUESTION_MESSAGE, null, opcionesPedido, opcionesPedido[0]);
        if (seleccionPedido == null) {
            return;
        }
        Pedido pedido = pendientes.get(Arrays.asList(opcionesPedido).indexOf(seleccionPedido));

        String[] opcionesRepartidor = repartidores.stream()
                .map(repartidor -> "#" + repartidor.getId() + " — " + repartidor.getNombre())
                .toArray(String[]::new);
        String seleccionRepartidor = (String) JOptionPane.showInputDialog(this, "Selecciona el repartidor:",
                "Asignar repartidor", JOptionPane.QUESTION_MESSAGE, null, opcionesRepartidor, opcionesRepartidor[0]);
        if (seleccionRepartidor == null) {
            return;
        }
        Repartidor repartidor = repartidores.get(Arrays.asList(opcionesRepartidor).indexOf(seleccionRepartidor));

        try {
            entregaDAO.guardar(new Entrega(pedido.getId(), repartidor.getId(), LocalDate.now(), LocalTime.now()));
            pedidoDAO.actualizarEstado(pedido.getId(), "EN_REPARTO");
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Repartidor " + repartidor.getNombre() + " asignado al pedido #" + pedido.getId()
                        + ". Entrega registrada y pedido en EN_REPARTO.",
                "Entrega registrada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarErrorBD(SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "No se pudo conectar/operar con la base de datos:\n" + ex.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
