package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USUARIO = "root";
    // TODO: reemplaza por la contraseña real de tu instalación local de MySQL
    private static final String PASSWORD = "tu_contraseña";

    private ConexionDB() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
