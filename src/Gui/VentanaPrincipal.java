package Gui;

import javax.swing.*;

/** Ventana principal: una pestaña CRUD por entidad. Al cambiar de pestaña se recargan sus datos y combos. */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFast — Gestión de repartidores, pedidos y entregas");
        setSize(820, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Repartidores", new PanelRepartidores());
        pestanas.addTab("Pedidos", new PanelPedidos());
        pestanas.addTab("Entregas", new PanelEntregas());
        pestanas.addChangeListener(e -> ((PanelCrud) pestanas.getSelectedComponent()).refrescar());
        add(pestanas);

        setVisible(true);
        ((PanelCrud) pestanas.getSelectedComponent()).refrescar();
    }
}
