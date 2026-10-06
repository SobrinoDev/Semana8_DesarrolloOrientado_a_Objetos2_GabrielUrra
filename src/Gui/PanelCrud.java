package Gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Esqueleto común de los paneles CRUD: formulario arriba, JTable al centro y botones abajo.
 * Cada subclase solo define su formulario y qué hace cada operación; aquí se centraliza el
 * cableado de eventos, las validaciones (IllegalArgumentException) y los mensajes de error SQL.
 */
abstract class PanelCrud extends JPanel {

    /** Operación que puede fallar con SQLException. */
    interface Accion {
        void run() throws SQLException;
    }

    protected final DefaultTableModel modelo;
    protected final JTable tabla;
    protected final JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
    protected final JPanel botones = new JPanel();

    protected PanelCrud(String... columnas) {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                llenarFormulario(tabla.getSelectedRow());
            }
        });

        botones.add(boton("Guardar", () -> ejecutar(this::crear, "Registro guardado correctamente.")));
        botones.add(boton("Actualizar", () -> ejecutar(this::actualizar, "Registro actualizado correctamente.")));
        botones.add(boton("Eliminar", this::confirmarEliminar));
        botones.add(boton("Limpiar", this::reiniciar));

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    // --- Lo que define cada panel ---

    /** Vuelve a leer la tabla (y combos) desde la base de datos. */
    protected abstract void cargar() throws SQLException;

    protected abstract void crear() throws SQLException;

    protected abstract void actualizar() throws SQLException;

    protected abstract void eliminar() throws SQLException;

    protected abstract void llenarFormulario(int fila);

    protected abstract void limpiar();

    // --- Utilidades compartidas ---

    /** Recarga los datos; si la BD falla, informa al usuario en vez de lanzar la excepción. */
    void refrescar() {
        try {
            cargar();
        } catch (SQLException e) {
            mostrarErrorSql(e);
        }
    }

    protected JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    /** Id (columna 0) de la fila seleccionada; exige haber seleccionado una. */
    protected int idSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione una fila de la tabla.");
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    /** Texto obligatorio y con largo máximo (acorde a la columna de la BD). */
    protected static String requerido(JTextField campo, String mensaje, int max) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
        if (texto.length() > max) {
            throw new IllegalArgumentException("Máximo " + max + " caracteres.");
        }
        return texto;
    }

    /** Ejecuta una operación CRUD mostrando éxito o un mensaje claro según el tipo de error. */
    private void ejecutar(Accion accion, String exito) {
        try {
            accion.run();
            JOptionPane.showMessageDialog(this, exito, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            reiniciar();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            mostrarErrorSql(e);
        }
    }

    private void confirmarEliminar() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila de la tabla.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar el registro seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            ejecutar(this::eliminar, "Registro eliminado correctamente.");
        }
    }

    private void reiniciar() {
        tabla.clearSelection();
        limpiar();
        refrescar();
    }

    private void mostrarErrorSql(SQLException e) {
        String mensaje = e instanceof SQLIntegrityConstraintViolationException
                ? "La operación viola una restricción: el registro está asociado a otros (por ejemplo, entregas).\n"
                + e.getMessage()
                : "Error de base de datos:\n" + e.getMessage();
        JOptionPane.showMessageDialog(this, mensaje, "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
