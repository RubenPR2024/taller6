package persistencia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dominio.Estado;
import dominio.Incidencias;
import dominio.Listas;
/**
 * Clase que proporciona los métodos para la persistencia de incidencias
 */
public class IncidenciasDAO {
	/**
	 * Importamos los array list
	 */
	private static Listas listas = new Listas();

	/**
	 * Método para registrar una nueva incidencia
	 * @param estado	Estado de la incidencia
	 * @param puesto	Puesto de la incidencia
	 * @param descripcion	Descripcion de la incidencia
	 * @return True si la operacion se ha realizado o false en caso contrario
	 */
    public static boolean registrarIncidencia(Estado estado, int puesto, String descripcion) {
        try {
            String codigo = generarCodigoIncidencia(new Date());
            Incidencias incidencia = new Incidencias(codigo, estado, puesto, descripcion);
            listas.agregarIncidencia(incidencia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para buscar la incidencia
     * @param identificador Identificador de la incidencia a buscar
     * @return	Devuelve incidencia en contrada o null si no encontró nada
     */
    public static Incidencias buscarIncidencia(String identificador) {
        return listas.buscarIncidencia(identificador);
    }

    /**
     * Método para eliminar una incidencia
     * @param identificador	Identificador de la incidencia a eliminar
     * @param fechaEliminacion	Fecha de eliminacion de la incidencia
     * @param causaEliminacion	Causa de eliminacion de la incidencia
     * @return	Devuelve true si se eliminó la incidencia o false en caso contrario
     */
    public static boolean eliminarIncidencia(String identificador, Date fechaEliminacion, String causaEliminacion) {
        Incidencias incidencia = buscarIncidencia(identificador);
        if (incidencia != null && incidencia.getEstado() != Estado.Resuelta) {
            incidencia.eliminarIncidencia(fechaEliminacion, causaEliminacion);
            listas.eliminarIncidencia(incidencia);
            return true;
        }
        return false;
    }

    /**
     * Método para resolver una incidencia
     * @param identificador		Identificador de la incidencia a resolver
     * @param fechaResolucion	Fecha de resolucion de la incidencia
     * @param resolucion		Resolucion de la incidenica
     * @return					Devuelve true si la ha resuelto o false en caso contrario
     */
    public static boolean resolverIncidencia(String identificador, Date fechaResolucion, String resolucion) {
        Incidencias incidencia = buscarIncidencia(identificador);
        if (incidencia != null && incidencia.getEstado() != Estado.Resuelta) {
            incidencia.resolverIncidencia(fechaResolucion, resolucion);
            listas.resolverIncidencia(incidencia);
            return true;
        }
        return false;
    }

    /**
     * Método que modifica la descripcion una incidencia pendiente
     * @param identificador		Identificador de la incidencia pendiente a modificar
     * @param nuevaDescripcion	Nueva descripcion de la incidencia
     * @return					Devuelve true si la ha modificado o false en caso contrario
     */
    public static boolean modificarIncidencia(String identificador, String nuevaDescripcion) {
        Incidencias incidencia = buscarIncidencia(identificador);
        if (incidencia != null && incidencia.getEstado() == Estado.Pendiente) {
            incidencia.setDescripcion(nuevaDescripcion);
            listas.actualizarIncidencia(incidencia);
            return true;
        }
        return false;
    }

    /**
     * Método que devuelve una una incidencia resuelta a pendiente
     * @param identificador		Identificador de la incidencia a devolver
     * @return					Devuelve true si lo ha devuelto o false en caso contrario
     */
    public static boolean devolverIncidenciasResueltas(String identificador) {
        for (Incidencias incidencia : listas.getIncidenciasResueltas()) {
            if (incidencia.getIdentificador().equals(identificador)) {
                // Remover de la lista de resueltas
                listas.getIncidenciasResueltas().remove(incidencia);

                // Actualizar estado y remover datos de resolución
                incidencia.setEstado(Estado.Pendiente);
                incidencia.setFechaResolucion(null);
                incidencia.setResolucion(null);

                // Agregar a la lista de pendientes
                listas.agregarIncidencia(incidencia);
                return true;
            }
        }
        System.out.println("No hay incidencias resueltas para devolver.");
        return false;
    }

    /**
     * Métodod que modifica una incidencia resuelta
     * @param identificador			Identificador de la incidencia resuelta a modificar
     * @param nuevaResolucion		Nueva resolucion de la incidencia resuelta
     * @return						Devuelve true si lo ha modificado o false en caso contrario
     */
    public static boolean modificarIncidenciaResuelta(String identificador, String nuevaResolucion) {
        for (Incidencias incidencia : listas.getIncidenciasResueltas()) {
            if (incidencia.getIdentificador().equals(identificador)) {
                incidencia.setResolucion(nuevaResolucion);
                return true;
            }
        }
        System.out.println("La incidencia no existe o no está resuelta.");
        return false;
    }

    /**
     * Método que devuelve el array list de incidencias pendientes
     * @return	Lista de incidencias pendiente
     */
    public static ArrayList<Incidencias> getIncidenciasPendientes() {
        return listas.getIncidenciasPendientes();
    }

    /**
     * Método que devuelve el array list de incidencias eliminadas
     * @return Lista de incidencias eliminadas
     */
    public static ArrayList<Incidencias> getIncidenciasEliminadas() {
        return listas.getIncidenciasEliminadas();
    }

    /**
     * Método que devuelve el array list de incidencias resueltas
     * @return Lista de incidencias resueltas
     */
    public static ArrayList<Incidencias> getIncidenciasResueltas() {
        return listas.getIncidenciasResueltas();
    }

    /**
     * Método encargado de generar un nuevo codigo de incidencia
     * @param fechaRegistro	Fecha de registro de la incidencia
     * @return	Devuelve el codigo generado de la incidencia
     */
    public static String generarCodigoIncidencia(Date fechaRegistro) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        String fechaFormateada = dateFormat.format(fechaRegistro);

        // Cuenta el número de incidencias registradas en la misma fecha
        int contador = 1;
        for (Incidencias incidencia : listas.getIncidenciasPendientes()) {
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
    public static void exportarPendientesXML() {
        XMLExportar.exportarAXML(listas.getIncidenciasPendientes(), "pendientes.xml");
    }

    /**
     * Método que exporta la lista de resueltas a XML
     */
    public static void exportarResueltasXML() {
        XMLExportar.exportarAXML(listas.getIncidenciasResueltas(), "resueltas.xml");
    }

    /**
     * Método que exporta la lista de eliminadas a XML
     */
    public static void exportarEliminadasXML() {
        XMLExportar.exportarAXML(listas.getIncidenciasEliminadas(), "eliminadas.xml");
    }
}
