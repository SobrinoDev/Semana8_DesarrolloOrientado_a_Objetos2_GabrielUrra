package dao;

import modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void guardar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet generadas = null;
        try {
            conexion = ConexionDB.conectar();
            statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, pedido.getDireccion());
            statement.setString(2, pedido.getTipo());
            statement.setString(3, pedido.getEstado());
            statement.executeUpdate();

            generadas = statement.getGeneratedKeys();
            if (generadas.next()) {
                pedido.setId(generadas.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar el pedido: " + e.getMessage());
            throw e;
        } finally {
            ConexionDB.cerrar(generadas, statement, conexion);
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        String sql = "SELECT id, direccion, tipo, estado FROM pedido ORDER BY id";
        List<Pedido> pedidos = new ArrayList<>();

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        try {
            conexion = ConexionDB.conectar();
            statement = conexion.prepareStatement(sql);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                pedidos.add(new Pedido(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("tipo"),
                        resultado.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los pedidos: " + e.getMessage());
            throw e;
        } finally {
            ConexionDB.cerrar(resultado, statement, conexion);
        }

        return pedidos;
    }

    public void actualizarEstado(int idPedido, String nuevoEstado) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        Connection conexion = null;
        PreparedStatement statement = null;
        try {
            conexion = ConexionDB.conectar();
            statement = conexion.prepareStatement(sql);
            statement.setString(1, nuevoEstado);
            statement.setInt(2, idPedido);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del pedido: " + e.getMessage());
            throw e;
        } finally {
            ConexionDB.cerrar(statement, conexion);
        }
    }
}
