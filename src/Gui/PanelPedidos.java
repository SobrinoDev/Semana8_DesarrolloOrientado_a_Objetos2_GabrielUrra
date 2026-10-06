package Gui;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import java.sql.SQLException;

/** CRUD de pedidos, con filtros opcionales por estado y tipo. */
class PanelPedidos extends PanelCrud {

    private static final String[] TIPOS = {"COMIDA", "ENCOMIENDA", "EXPRESS"};
    private static final String[] ESTADOS = {"PENDIENTE", "EN_REPARTO", "ENTREGADO"};
    private static final String TODOS = "TODOS";

    private final PedidoDAO dao = new PedidoDAO();
    private final JTextField campoDireccion = new JTextField();
    private final JComboBox<String> comboTipo = new JComboBox<>(TIPOS);
    private final JComboBox<String> comboEstado = new JComboBox<>(ESTADOS);
    private final JComboBox<String> filtroEstado = crearFiltro(ESTADOS);
    private final JComboBox<String> filtroTipo = crearFiltro(TIPOS);

    PanelPedidos() {
        super("ID", "Dirección", "Tipo", "Estado");
        formulario.add(new JLabel("Dirección:"));
        formulario.add(campoDireccion);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(comboTipo);
        formulario.add(new JLabel("Estado:"));
        formulario.add(comboEstado);

        botones.add(new JLabel("   Filtrar estado:"));
        botones.add(filtroEstado);
        botones.add(new JLabel("tipo:"));
        botones.add(filtroTipo);
        botones.add(boton("Filtrar", this::refrescar));
    }

    @Override
    protected void cargar() throws SQLException {
        modelo.setRowCount(0);
        for (Pedido p : dao.readAll(valorFiltro(filtroEstado), valorFiltro(filtroTipo))) {
            modelo.addRow(new Object[]{p.getId(), p.getDireccion(), p.getTipo(), p.getEstado()});
        }
    }

    @Override
    protected void crear() throws SQLException {
        dao.create(leerFormulario(0));
    }

    @Override
    protected void actualizar() throws SQLException {
        dao.update(leerFormulario(idSeleccionado()));
    }

    @Override
    protected void eliminar() throws SQLException {
        dao.delete(idSeleccionado());
    }

    @Override
    protected void llenarFormulario(int fila) {
        campoDireccion.setText((String) modelo.getValueAt(fila, 1));
        comboTipo.setSelectedItem(modelo.getValueAt(fila, 2));
        comboEstado.setSelectedItem(modelo.getValueAt(fila, 3));
    }

    @Override
    protected void limpiar() {
        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
    }

    private Pedido leerFormulario(int id) {
        return new Pedido(id, requerido(campoDireccion, "La dirección es obligatoria.", 100),
                (String) comboTipo.getSelectedItem(), (String) comboEstado.getSelectedItem());
    }

    private static JComboBox<String> crearFiltro(String[] valores) {
        JComboBox<String> combo = new JComboBox<>(valores);
        combo.insertItemAt(TODOS, 0);
        combo.setSelectedIndex(0);
        return combo;
    }

    /** null = sin filtro (opción "TODOS"). */
    private static String valorFiltro(JComboBox<String> combo) {
        String valor = (String) combo.getSelectedItem();
        return TODOS.equals(valor) ? null : valor;
    }
}
