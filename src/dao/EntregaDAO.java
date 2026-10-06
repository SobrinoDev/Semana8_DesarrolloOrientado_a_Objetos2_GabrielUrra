package dao;

import modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** CRUD de la tabla `entregas` (relación pedido + repartidor + fecha/hora). */
public class EntregaDAO {

    public void create(Entrega e) throws SQLException {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.getIdPedido());
            ps.setInt(2, e.getIdRepartidor());
            ps.setDate(3, Date.valueOf(e.getFecha()));
            ps.setTime(4, Time.valueOf(e.getHora()));
            ps.executeUpdate();
        }
    }

    public List<Entrega> readAll() throws SQLException {
        return readAll(null, null);
    }

    /** Filtros opcionales por pedido y/o repartidor: null significa "sin filtrar". */
    public List<Entrega> readAll(Integer idPedido, Integer idRepartidor) throws SQLException {
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entregas "
                + "WHERE (? IS NULL OR id_pedido = ?) AND (? IS NULL OR id_repartidor = ?) ORDER BY id";
        List<Entrega> entregas = new ArrayList<>();
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, idPedido);
            ps.setObject(2, idPedido);
            ps.setObject(3, idRepartidor);
            ps.setObject(4, idRepartidor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entregas.add(new Entrega(rs.getInt("id"), rs.getInt("id_pedido"), rs.getInt("id_repartidor"),
                            rs.getObject("fecha", LocalDate.class), rs.getObject("hora", LocalTime.class)));
                }
            }
        }
        return entregas;
    }

    public void update(Entrega e) throws SQLException {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection c = ConexionDB.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.getIdPedido());
            ps.setInt(2, e.getIdRepartidor());
            ps.setDate(3, Date.valueOf(e.getFecha()));
            ps.setTime(4, Time.valueOf(e.getHora()));
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = ConexionDB.conectar();
             PreparedStatement ps = c.prepareStatement("DELETE FROM entregas WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
