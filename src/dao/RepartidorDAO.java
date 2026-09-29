package dao;

import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public void guardar(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet generadas = null;
        try {
            conexion = ConexionDB.conectar();
            statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, repartidor.getNombre());
            statement.executeUpdate();

            generadas = statement.getGeneratedKeys();
            if (generadas.next()) {
                repartidor.setId(generadas.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar el repartidor: " + e.getMessage());
            throw e;
        } finally {
            ConexionDB.cerrar(generadas, statement, conexion);
        }
    }

    public List<Repartidor> listarTodos() throws SQLException {
        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";
        List<Repartidor> repartidores = new ArrayList<>();

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionDB.conectar();
            statement = conexion.prepareStatement(sql);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                repartidores.add(new Repartidor(resultado.getInt("id"), resultado.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los repartidores: " + e.getMessage());
            throw e;
        } finally {
            ConexionDB.cerrar(resultado, statement, conexion);
        }

        return repartidores;
    }
}
