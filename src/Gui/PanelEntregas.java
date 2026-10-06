package Gui;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** CRUD de entregas: asocia un pedido y un repartidor (elegidos en JComboBox cargados desde la BD). */
class PanelEntregas extends PanelCrud {

    private static final String TODOS = "TODOS";

    private final EntregaDAO dao = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    private final JComboBox<Object> comboPedido = new JComboBox<>();
    private final JComboBox<Object> comboRepartidor = new JComboBox<>();
    private final JTextField campoFecha = new JTextField();
    private final JTextField campoHora = new JTextField();
    private final JComboBox<Object> filtroPedido = new JComboBox<>();
    private final JComboBox<Object> filtroRepartidor = new JComboBox<>();

    PanelEntregas() {
        super("ID", "Pedido", "Repartidor", "Fecha", "Hora");
        formulario.add(new JLabel("Pedido:"));
        formulario.add(comboPedido);
        formulario.add(new JLabel("Repartidor:"));
        formulario.add(comboRepartidor);
        formulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formulario.add(campoFecha);
        formulario.add(new JLabel("Hora (HH:mm o HH:mm:ss):"));
        formulario.add(campoHora);

        botones.add(new JLabel("   Filtrar pedido:"));
        botones.add(filtroPedido);
        botones.add(new JLabel("repartidor:"));
        botones.add(filtroRepartidor);
        botones.add(boton("Filtrar", this::refrescar));
        limpiar();
    }

    /** Recarga los combos (conservando la selección previa) y la tabla filtrada. */
    @Override
    protected void cargar() throws SQLException {
        List<Item> pedidos = pedidoDAO.readAll().stream()
                .map(p -> new Item(p.getId(), p.getDireccion())).toList();
        List<Item> repartidores = repartidorDAO.readAll().stream()
                .map(r -> new Item(r.getId(), r.getNombre())).toList();
        recargar(comboPedido, false, pedidos);
        recargar(comboRepartidor, false, repartidores);
        recargar(filtroPedido, true, pedidos);
        recargar(filtroRepartidor, true, repartidores);

        Map<Integer, Item> pedidoPorId = pedidos.stream().collect(Collectors.toMap(Item::id, Function.identity()));
        Map<Integer, Item> repartidorPorId = repartidores.stream().collect(Collectors.toMap(Item::id, Function.identity()));

        modelo.setRowCount(0);
        for (Entrega e : dao.readAll(idFiltro(filtroPedido), idFiltro(filtroRepartidor))) {
            modelo.addRow(new Object[]{e.getId(),
                    pedidoPorId.getOrDefault(e.getIdPedido(), new Item(e.getIdPedido(), "?")),
                    repartidorPorId.getOrDefault(e.getIdRepartidor(), new Item(e.getIdRepartidor(), "?")),
                    e.getFecha(), e.getHora()});
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
        comboPedido.setSelectedItem(modelo.getValueAt(fila, 1));
        comboRepartidor.setSelectedItem(modelo.getValueAt(fila, 2));
        campoFecha.setText(String.valueOf(modelo.getValueAt(fila, 3)));
        campoHora.setText(String.valueOf(modelo.getValueAt(fila, 4)));
    }

    /** Deja fecha y hora actuales como valor por defecto; los combos conservan su selección. */
    @Override
    protected void limpiar() {
        campoFecha.setText(LocalDate.now().toString());
        campoHora.setText(LocalTime.now().withNano(0).toString());
    }

    private Entrega leerFormulario(int id) {
        Item pedido = (Item) comboPedido.getSelectedItem();
        Item repartidor = (Item) comboRepartidor.getSelectedItem();
        if (pedido == null || repartidor == null) {
            throw new IllegalArgumentException("Debe existir y seleccionarse un pedido y un repartidor.");
        }
        try {
            return new Entrega(id, pedido.id(), repartidor.id(),
                    LocalDate.parse(campoFecha.getText().trim()), LocalTime.parse(campoHora.getText().trim()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha (AAAA-MM-DD) u hora (HH:mm o HH:mm:ss) con formato inválido.");
        }
    }

    private static void recargar(JComboBox<Object> combo, boolean conTodos, List<Item> items) {
        Object previo = combo.getSelectedItem();
        combo.removeAllItems();
        if (conTodos) {
            combo.addItem(TODOS);
        }
        items.forEach(combo::addItem);
        if (previo != null) {
            combo.setSelectedItem(previo);
        }
    }

    /** null = sin filtro (opción "TODOS"). */
    private static Integer idFiltro(JComboBox<Object> combo) {
        return combo.getSelectedItem() instanceof Item item ? item.id() : null;
    }
}
