package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	private static final String USUARIO="ruben";
	private static final String PASSWORD="12345";
	private static final String MAQUINA="localhost";
	private static final String BD="bd_incidencias";
	private static final String URL="jdbc:mysql://"+MAQUINA+":3306/"+BD;
	
	public static Connection conectar() throws SQLException {
		return DriverManager.getConnection(URL, USUARIO, PASSWORD);
	}
	
	public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
