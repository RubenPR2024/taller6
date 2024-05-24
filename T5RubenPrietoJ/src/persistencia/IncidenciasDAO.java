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
        String sql = "INSERT INTO incidencias_pendientes (identificador, estado, puesto, descripcion, fechaRegistro) VALUES (?, ?, ?, ?, ?)";
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

    public static Incidencias buscarIncidencia(String identificador) {
        String sql = "SELECT * FROM incidencias_pendientes WHERE identificador = ? UNION " +
                     "SELECT * FROM incidencias_resueltas WHERE identificador = ? UNION " +
                     "SELECT * FROM incidencias_eliminadas WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, identificador);
            sentencia.setString(2, identificador);
            sentencia.setString(3, identificador);
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

    public static boolean eliminarIncidencia(String identificador, Date fechaEliminacion, String causaEliminacion) {
        String sqlDelete = "DELETE FROM incidencias_pendientes WHERE identificador = ?";
        String sqlInsert = "INSERT INTO incidencias_eliminadas (identificador, estado, puesto, descripcion, fechaRegistro, fechaEliminacion, causaEliminacion) " +
                           "SELECT identificador, estado, puesto, descripcion, fechaRegistro, ?, ? FROM incidencias_pendientes WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentenciaDelete = null;
        PreparedStatement sentenciaInsert = null;

        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false);

            sentenciaInsert = con.prepareStatement(sqlInsert);
            sentenciaInsert.setDate(1, new java.sql.Date(fechaEliminacion.getTime()));
            sentenciaInsert.setString(2, causaEliminacion);
            sentenciaInsert.setString(3, identificador);
            int filasInsertadas = sentenciaInsert.executeUpdate();

            if (filasInsertadas > 0) {
                sentenciaDelete = con.prepareStatement(sqlDelete);
                sentenciaDelete.setString(1, identificador);
                int filasEliminadas = sentenciaDelete.executeUpdate();
                if (filasEliminadas > 0) {
                    con.commit();
                    return true;
                }
            }
            con.rollback();
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConexionBD.cerrarConexion(con);
        }
    }

    public static boolean resolverIncidencia(String identificador, Date fechaResolucion, String resolucion) {
        String sqlDelete = "DELETE FROM incidencias_pendientes WHERE identificador = ?";
        String sqlInsert = "INSERT INTO incidencias_resueltas (identificador, estado, puesto, descripcion, fechaRegistro, fechaResolucion, resolucion) " +
                           "SELECT identificador, estado, puesto, descripcion, fechaRegistro, ?, ? FROM incidencias_pendientes WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentenciaDelete = null;
        PreparedStatement sentenciaInsert = null;

        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false);

            sentenciaInsert = con.prepareStatement(sqlInsert);
            sentenciaInsert.setDate(1, new java.sql.Date(fechaResolucion.getTime()));
            sentenciaInsert.setString(2, resolucion);
            sentenciaInsert.setString(3, identificador);
            int filasInsertadas = sentenciaInsert.executeUpdate();

            if (filasInsertadas > 0) {
                sentenciaDelete = con.prepareStatement(sqlDelete);
                sentenciaDelete.setString(1, identificador);
                int filasEliminadas = sentenciaDelete.executeUpdate();
                if (filasEliminadas > 0) {
                    con.commit();
                    return true;
                }
            }
            con.rollback();
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConexionBD.cerrarConexion(con);
        }
    }
    
    public static boolean modificarIncidencia(String identificador, String nuevaDescripcion) {
        String sql = "UPDATE incidencias_pendientes SET descripcion = ? WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, nuevaDescripcion);
            sentencia.setString(2, identificador);

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
        String sql = "UPDATE incidencias_resueltas SET resolucion = ? WHERE identificador = ?";
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, nuevaResolucion);
            sentencia.setString(2, identificador);

            int filasActualizadas = sentencia.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrarConexion(con);
        }
    }
    
    public static boolean devolverIncidenciasResueltas(String identificador) {
        String selectSQL = "SELECT * FROM incidencias_resueltas WHERE identificador = ?";
        String insertSQL = "INSERT INTO incidencias_pendientes (identificador, estado, puesto, descripcion, fechaRegistro) VALUES (?, ?, ?, ?, ?)";
        String deleteSQL = "DELETE FROM incidencias_resueltas WHERE identificador = ?";
        Connection con = null;
        PreparedStatement selectStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement deleteStmt = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false); // Start transaction

            // Select from resueltas
            selectStmt = con.prepareStatement(selectSQL);
            selectStmt.setString(1, identificador);
            rs = selectStmt.executeQuery();

            if (rs.next()) {
                // Insert into pendientes
                insertStmt = con.prepareStatement(insertSQL);
                insertStmt.setString(1, rs.getString("identificador"));
                insertStmt.setString(2, Estado.Pendiente.toString());
                insertStmt.setInt(3, rs.getInt("puesto"));
                insertStmt.setString(4, rs.getString("descripcion"));
                insertStmt.setDate(5, rs.getDate("fechaRegistro"));

                int insertRows = insertStmt.executeUpdate();

                // Delete from resueltas
                deleteStmt = con.prepareStatement(deleteSQL);
                deleteStmt.setString(1, identificador);
                int deleteRows = deleteStmt.executeUpdate();

                if (insertRows > 0 && deleteRows > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            ConexionBD.cerrarConexion(con);
        }
        return false;
    }

    public static List<Incidencias> getIncidenciasPendientes() {
        List<Incidencias> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM incidencias_pendientes";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);
            rs = sentencia.executeQuery();

            while (rs.next()) {
                String iden = rs.getString("identificador");
                Estado estado = Estado.valueOf(rs.getString("estado"));
                int puesto = rs.getInt("puesto");
                String descripcion = rs.getString("descripcion");
                Incidencias incidencia = new Incidencias(iden, estado, puesto, descripcion);
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

    public static List<Incidencias> getIncidenciasEliminadas() {
        List<Incidencias> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM incidencias_eliminadas";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);
            rs = sentencia.executeQuery();

            while (rs.next()) {
                String iden = rs.getString("identificador");
                Estado estado = Estado.valueOf(rs.getString("estado"));
                int puesto = rs.getInt("puesto");
                String descripcion = rs.getString("descripcion");
                Incidencias incidencia = new Incidencias(iden, estado, puesto, descripcion);
                incidencia.setFechaRegistro(rs.getDate("fechaRegistro"));
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

    public static List<Incidencias> getIncidenciasResueltas() {
        List<Incidencias> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM incidencias_resueltas";
        Connection con = null;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            con = ConexionBD.conectar();
            sentencia = con.prepareStatement(sql);
            rs = sentencia.executeQuery();

            while (rs.next()) {
                String iden = rs.getString("identificador");
                Estado estado = Estado.valueOf(rs.getString("estado"));
                int puesto = rs.getInt("puesto");
                String descripcion = rs.getString("descripcion");
                Incidencias incidencia = new Incidencias(iden, estado, puesto, descripcion);
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
