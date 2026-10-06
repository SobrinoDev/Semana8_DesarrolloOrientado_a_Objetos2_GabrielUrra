package dao;

import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** CRUD de la tabla `repartidores`. try-with-resources cierra Connection/Statement/ResultSet. */
public class RepartidorDAO {

    public void create(Repartidor r) throws SQLException {
        try (Connection c = ConexionDB.conectar();
             PreparedStatement ps = c.prepareStatement("INSERT INTO repartidores (nombre) VALUES (?)")) {
            ps.setString(1, r.getNombre());
            ps.executeUpdate();
        }
    }

    public List<Repartidor> readAll() throws SQLException {
        List<Repartidor> repartidores = new ArrayList<>();
        try (Connection c = ConexionDB.conectar();
             PreparedStatement ps = c.prepareStatement("SELECT id, nombre FROM repartidores ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                repartidores.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return repartidores;
    }

    public void update(Repartidor r) throws SQLException {
        try (Connection c = ConexionDB.conectar();
             PreparedStatement ps = c.prepareStatement("UPDATE repartidores SET nombre = ? WHERE id = ?")) {
            ps.setString(1, r.getNombre());
            ps.setInt(2, r.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = ConexionDB.conectar();
             PreparedStatement ps = c.prepareStatement("DELETE FROM repartidores WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
