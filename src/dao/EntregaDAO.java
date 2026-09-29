package dao;

import modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class EntregaDAO {

    // Registra la relación entre un pedido y el repartidor que lo entrega
    public void guardar(Entrega entrega) throws SQLException {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet generadas = null;
        try {
            conexion = ConexionDB.conectar();
            statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entrega.getIdPedido());
            statement.setInt(2, entrega.getIdRepartidor());
            statement.setDate(3, Date.valueOf(entrega.getFecha()));
            statement.setTime(4, Time.valueOf(entrega.getHora()));
            statement.executeUpdate();

            generadas = statement.getGeneratedKeys();
            if (generadas.next()) {
                entrega.setId(generadas.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la entrega: " + e.getMessage());
            throw e;
        } finally {
            ConexionDB.cerrar(generadas, statement, conexion);
        }
    }
}
