package Gui;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import java.sql.SQLException;

/** CRUD de repartidores. */
class PanelRepartidores extends PanelCrud {

    private final RepartidorDAO dao = new RepartidorDAO();
    private final JTextField campoNombre = new JTextField();

    PanelRepartidores() {
        super("ID", "Nombre");
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
    }

    @Override
    protected void cargar() throws SQLException {
        modelo.setRowCount(0);
        for (Repartidor r : dao.readAll()) {
            modelo.addRow(new Object[]{r.getId(), r.getNombre()});
        }
    }

    @Override
    protected void crear() throws SQLException {
        dao.create(new Repartidor(nombre()));
    }

    @Override
    protected void actualizar() throws SQLException {
        dao.update(new Repartidor(idSeleccionado(), nombre()));
    }

    @Override
    protected void eliminar() throws SQLException {
        dao.delete(idSeleccionado());
    }

    @Override
    protected void llenarFormulario(int fila) {
        campoNombre.setText((String) modelo.getValueAt(fila, 1));
    }

    @Override
    protected void limpiar() {
        campoNombre.setText("");
    }

    private String nombre() {
        return requerido(campoNombre, "El nombre es obligatorio.", 100);
    }
}
