package dao;

import modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** CRUD de la tabla `pedidos`. try-with-resources cierra Connection/Statement/ResultSet. */
public class PedidoDAO {

    public void create(Pedido p) throws SQLException {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getDireccion());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getEstado());
            ps.executeUpdate();
        }
    }

    public List<Pedido> readAll() throws SQLException {
        return readAll(null, null);
    }

    /** Filtros opcionales: un parámetro null significa "sin filtrar" por ese campo. */
    public List<Pedido> readAll(String estado, String tipo) throws SQLException {
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos "
                + "WHERE (? IS NULL OR estado = ?) AND (? IS NULL OR tipo = ?) ORDER BY id";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, estado);
            ps.setString(3, tipo);
            ps.setString(4, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new Pedido(rs.getInt("id"), rs.getString("direccion"),
                            rs.getString("tipo"), rs.getString("estado")));
                }
            }
        }
        return pedidos;
    }

    public void update(Pedido p) throws SQLException {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getDireccion());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getEstado());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = ConexionDB.conectar();
             PreparedStatement ps = c.prepareStatement("DELETE FROM pedidos WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
