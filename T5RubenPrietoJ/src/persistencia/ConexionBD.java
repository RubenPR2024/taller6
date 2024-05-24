package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona la conexión a la base de datos.
 */
public class ConexionBD {
	// Datos de conexión
	private static final String USUARIO="ruben";
	private static final String PASSWORD="12345";
	private static final String MAQUINA="localhost";
	private static final String BD="bd_incidencias";
	private static final String URL="jdbc:mysql://"+MAQUINA+":3306/"+BD;
	
	/**
     * Establece una conexión con la base de datos.
     *
     * @return La conexión establecida
     * @throws SQLException si ocurre un error al conectar
     */
	public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
	 /**
     * Cierra la conexión a la base de datos.
     *
     * @param conn La conexión a cerrar
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return La conexión obtenida
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection obtenerConexion() throws SQLException {
        Connection conexion = null;
        try {
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            // Manejar la excepción
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
        return conexion;
    }
}
