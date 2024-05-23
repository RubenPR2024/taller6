package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.Estado;
import dominio.Incidencias;
/**
 * Clase que proporciona los métodos para la persistencia de incidencias
 */
public class IncidenciasDAO {
	
	private Connection conexion;
	
    public static boolean registrarIncidencia(Estado estado, int puesto, String descripcion) {
    	String sql = "INSERT INTO incidencias (identificador, estado, puesto, descripcion, fechaRegistro) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement sentencia = null;
        
        try {
        	con = ConexionBD.conectar();
        	sentencia = con.prepareStatement(sql);
        	String codigo = generarCodigoIncidencia(new Date());
        	
        	sentencia.setString(1, codigo);
        	sentencia.setString(2, estado.toString());
        	sentencia.setInt(3, puesto);
        	sentencia.setString(4, descripcion);
        	sentencia.setDate(5, new java.sql.Date(new Date().getTime()));
        	
        	int filasAfectadas = sentencia.executeUpdate();
        	return filasAfectadas > 0;
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
        
    }

    /**
     * Método para buscar la incidencia
     * @param identificador Identificador de la incidencia a buscar
     * @return	Devuelve incidencia en contrada o null si no encontró nada
     */
    public static Incidencias buscarIncidencia(String identificador) {
        String sql = "SELECT * FROM incidencias WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        
        try {
        	con = ConexionBD.conectar();
        	sentencia = con.prepareStatement(sql);
        	sentencia.setString(1, identificador);
        	rs = sentencia.executeQuery();
        	
        	if (rs.next()) {
        		String iden = rs.getString("identificador");
                Estado estado = Estado.valueOf(rs.getString("estado"));
                int puesto = rs.getInt("puesto");
                String descripcion = rs.getString("descripcion");
                Incidencias incidencia = new Incidencias(iden, estado, puesto, descripcion);
                incidencia.setFechaRegistro(rs.getDate("fechaRegistro"));
                incidencia.setFechaResolucion(rs.getDate("fechaResolucion"));
                incidencia.setResolucion(rs.getString("resolucion"));
                incidencia.setFechaEliminacion(rs.getDate("fechaEliminacion"));
                incidencia.setCausaEliminacion(rs.getString("causaEliminacion"));
                return incidencia;
        	}
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
        return null;
    }

    /**
     * Método para eliminar una incidencia
     * @param identificador	Identificador de la incidencia a eliminar
     * @param fechaEliminacion	Fecha de eliminacion de la incidencia
     * @param causaEliminacion	Causa de eliminacion de la incidencia
     * @return	Devuelve true si se eliminó la incidencia o false en caso contrario
     */
    public static boolean eliminarIncidencia(String identificador, Date fechaEliminacion, String causaEliminacion) {
    	String sql = "DELETE FROM incidencias WHERE identificador = ?";
    	Connection con = null;
    	PreparedStatement sentencia = null;
    	
    	try {
    		con = ConexionBD.conectar();
    		sentencia = con.prepareStatement(sql);
    		sentencia.setString(1, identificador);
    		
    		int filasEliminadas = sentencia.executeUpdate();
    		return filasEliminadas > 0;
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	} finally {
    		ConexionBD.cerrarConexion(con);
    	}
    }

    /**
     * Método para resolver una incidencia
     * @param identificador		Identificador de la incidencia a resolver
     * @param fechaResolucion	Fecha de resolucion de la incidencia
     * @param resolucion		Resolucion de la incidenica
     * @return					Devuelve true si la ha resuelto o false en caso contrario
     */
    public static boolean resolverIncidencia(String identificador, Date fechaResolucion, String resolucion) {
        String sql = "UPDATE incidencias SET estado = ?, fechaResolucion = ?, resolucion = ?, WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        
        try {
        	con = ConexionBD.conectar();
        	sentencia = con.prepareStatement(sql);
        	sentencia.setString(1, Estado.Resuelta.toString());
        	sentencia.setDate(2, (java.sql.Date) fechaResolucion);
        	sentencia.setString(3, resolucion);
        	sentencia.setString(4, identificador);
        	
        	int filasActualizadas = sentencia.executeUpdate();
        	return filasActualizadas > 0;
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
    }

    /**
     * Método que modifica la descripcion una incidencia pendiente
     * @param identificador		Identificador de la incidencia pendiente a modificar
     * @param nuevaDescripcion	Nueva descripcion de la incidencia
     * @return					Devuelve true si la ha modificado o false en caso contrario
     */
    public static boolean modificarIncidencia(String identificador, String nuevaDescripcion) {
        String sql = "UPDATE incidencias SET descripcion = ? WHERE identificador = ? AND estado = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        
        try {
        	con = ConexionBD.conectar();
        	sentencia = con.prepareStatement(sql);
        	sentencia.setString(1, nuevaDescripcion);
        	sentencia.setString(2, identificador);
        	sentencia.setString(3, Estado.Pendiente.toString());
        	
        	int filasActualizadas = sentencia.executeUpdate();
        	return filasActualizadas > 0;
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
    }

    /**
     * Método que devuelve una una incidencia resuelta a pendiente
     * @param identificador		Identificador de la incidencia a devolver
     * @return					Devuelve true si lo ha devuelto o false en caso contrario
     */
    public static boolean devolverIncidenciasResueltas(String identificador) {
        String sql = "UPDATE incidencias SET estado = ?, fechaResolucion = ?, resolucion = ? WHERE identificador = ? AND estado = ?";
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);

            sentencia.setString(1, Estado.Pendiente.toString());
            sentencia.setDate(2, null);
            sentencia.setString(3, null);
            sentencia.setString(4, identificador);
            sentencia.setString(5, Estado.Resuelta.toString());

            int filasActualizadas = sentencia.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
    }

    public static boolean modificarIncidenciaResuelta(String identificador, String nuevaResolucion) {
        String sql = "UPDATE incidencias SET resolucion = ? WHERE identificador = ? AND estado = ?";
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
        	con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);

            sentencia.setString(1, nuevaResolucion);
            sentencia.setString(2, identificador);
            sentencia.setString(3, Estado.Resuelta.toString());

            int filasActualizadas = sentencia.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
    }
    /**
     * Método que devuelve el array list de incidencias pendientes
     * @return	Lista de incidencias pendiente
     */
    public static List<Incidencias> getIncidenciasPendientes() {
        List<Incidencias> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM incidencias WHERE estado = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        
        try {
        con = ConexionBD.conectar();
    	sentencia = con.prepareStatement(sql);
    	sentencia.setString(1, Estado.Pendiente.toString());
    	rs = sentencia.executeQuery();
    	
    	while (rs.next()) {
    		String iden = rs.getString("identificador");
    		Estado estado = Estado.valueOf(rs.getString("estado"));
    		int Puesto = rs.getInt("puesto");
    		String desc = rs.getString("descripcion");
    		Incidencias incidencia = new Incidencias(desc, estado, Puesto, desc);
    		incidencia.setFechaRegistro(rs.getDate("fechaRegistro"));
    		incidencias.add(incidencia);
        }
    	
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
		return incidencias;
    }

    /**
     * Método que devuelve el array list de incidencias eliminadas
     * @return Lista de incidencias eliminadas
     */
    public static List<Incidencias> getIncidenciasEliminadas() {
    	List<Incidencias> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM incidencias WHERE estado = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        
        try {
        con = ConexionBD.conectar();
    	sentencia = con.prepareStatement(sql);
    	sentencia.setString(1, Estado.Eliminada.toString());
    	rs = sentencia.executeQuery();
    	
    	while (rs.next()) {
    		String iden = rs.getString("identificador");
    		Estado estado = Estado.valueOf(rs.getString("estado"));
    		int Puesto = rs.getInt("puesto");
    		String desc = rs.getString("descripcion");
    		Incidencias incidencia = new Incidencias(desc, estado, Puesto, desc);
    		incidencia.setFechaIncidencia(rs.getDate("fechaIncidencia"));
    		incidencia.setFechaEliminacion(rs.getDate("fechaEliminacion"));
            incidencia.setCausaEliminacion(rs.getString("causaEliminacion"));
    		incidencias.add(incidencia);
        }
    	
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
		return incidencias;
    }

    /**
     * Método que devuelve el array list de incidencias resueltas
     * @return Lista de incidencias resueltas
     */
    public static List<Incidencias> getIncidenciasResueltas() {
    	List<Incidencias> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM incidencias WHERE estado = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        
        try {
        con = ConexionBD.conectar();
    	sentencia = con.prepareStatement(sql);
    	sentencia.setString(1, Estado.Resuelta.toString());
    	rs = sentencia.executeQuery();
    	
    	while (rs.next()) {
    		String iden = rs.getString("identificador");
    		Estado estado = Estado.valueOf(rs.getString("estado"));
    		int Puesto = rs.getInt("puesto");
    		String desc = rs.getString("descripcion");
    		Incidencias incidencia = new Incidencias(desc, estado, Puesto, desc);
    		incidencia.setFechaRegistro(rs.getDate("fechaRegistro"));
    		incidencia.setFechaResolucion(rs.getDate("fechaResolucion"));
            incidencia.setResolucion(rs.getString("resolucion"));
    		incidencias.add(incidencia);
        }
    	
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
        	ConexionBD.cerrarConexion(con);
        }
		return incidencias;
    }

    /**
     * Método encargado de generar un nuevo codigo de incidencia
     * @param fechaRegistro	Fecha de registro de la incidencia
     * @return	Devuelve el codigo generado de la incidencia
     */
    public static String generarCodigoIncidencia(Date fechaRegistro) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        String fechaFormateada = dateFormat.format(fechaRegistro);

        List<Incidencias> incidenciasPendientes = getIncidenciasPendientes();
        
        int contador = 1;
        for (Incidencias incidencia : incidenciasPendientes) {
            Date fechaIncidencia = incidencia.getFechaIncidencia();
            if (isSameDay(fechaRegistro, fechaIncidencia)) {
                contador++;
            }
        }

        // Concatena la fecha formateada y el contador para generar el nuevo código
        return fechaFormateada + "-" + contador;
    }

    /**
     * Método que compara las dos fecha para comprobar si es el mismo dia
     * @param fecha1	Fecha de la incidencia anterior
     * @param fecha2	Fecha de la nueva incidencia
     * @return			Devuelve true si son iguales o false en caso contrario
     */
    private static boolean isSameDay(Date fecha1, Date fecha2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(fecha1).equals(fmt.format(fecha2));
    }
    
    /**
     * Método que exporta la lista de pendientes a XML
     */
//    public static void exportarPendientesXML() {
//        XMLExportar.exportarAXML(listas.getIncidenciasPendientes(), "pendientes.xml");
//    }
//
//    /**
//     * Método que exporta la lista de resueltas a XML
//     */
//    public static void exportarResueltasXML() {
//        XMLExportar.exportarAXML(listas.getIncidenciasResueltas(), "resueltas.xml");
//    }
//
//    /**
//     * Método que exporta la lista de eliminadas a XML
//     */
//    public static void exportarEliminadasXML() {
//        XMLExportar.exportarAXML(listas.getIncidenciasEliminadas(), "eliminadas.xml");
//    }
}
