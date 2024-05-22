package dominio;

import java.util.ArrayList;

import persistencia.GestorArchivos;
import persistencia.XMLExportar;

/**
 * Clase que se encarga de todos los movimientos relacionados con las listas
 */
public class Listas {
		/**
		 * ArrayList que guarda las incidencias con estado pendiente
		 */
	    private static ArrayList<Incidencias> incidencias_pendientes;
	    /**
		 * ArrayList que guarda las incidencias con estado resuelta
		 */
	    private ArrayList<Incidencias> incidencias_resueltas;
	    /**
		 * ArrayList que guarda las incidencias con estado eliminada
		 */
	    private ArrayList<Incidencias> incidencias_eliminadas;
	    /**
		 * Constructor de la clase Lista
		 */
	    public Listas() {
	        incidencias_pendientes = GestorArchivos.cargarIncidenciasPendientes();
	        incidencias_resueltas = GestorArchivos.cargarIncidenciasResueltas();
	        incidencias_eliminadas = GestorArchivos.cargarIncidenciasEliminadas();
	    }

	    /**
		 * Método para acceder al ArrayList de incidencias pendientes
		 * @return Lista de incidencias pendientes
		 */
	    public static ArrayList<Incidencias> getIncidenciasPendientes() {
	        return incidencias_pendientes;
	    }

	    /**
		 * Método para acceder al ArrayList de incidencias resueltas
		 * @return Lista de incidencias resueltas
		 */
	    public ArrayList<Incidencias> getIncidenciasResueltas() {
	        return incidencias_resueltas;
	    }

	    /**
		 * Método para acceder al ArrayList de incidencias eliminadas
		 * @return Lista de incidencias eliminadas
		 */
	    public ArrayList<Incidencias> getIncidenciasEliminadas() {
	        return incidencias_eliminadas;
	    }

	    /**
	     * Método que se encarga de añadir una incidencia en el array list de incidencias pendientes
	     * @param incidencia Incidencias pendiente a añadir
	     */
	    public void agregarIncidencia(Incidencias incidencia) {
	        incidencias_pendientes.add(incidencia);
	        GestorArchivos.guardarIncidenciasPendientes(incidencias_pendientes);
	    }

	    /**
	     * Método que se encarga de eliminar de la lista de pendientes la incidencia y añadirla en la lista de eliminadas
	     * @param incidencia Incidencia a eliminar
	     */
	    public void eliminarIncidencia(Incidencias incidencia) {
	        incidencias_pendientes.remove(incidencia);
	        GestorArchivos.guardarIncidenciasPendientes(incidencias_pendientes);
	        incidencias_eliminadas.add(incidencia);
	        GestorArchivos.guardarIncidenciasEliminadas(incidencias_eliminadas);
	    }

	    /**
	     * Método que se encarga de eliminar de la lista de pendientes la incidencia y añadirla en la lista de resueltas
	     * @param incidencia Incidencia a resolver
	     */
	    public void resolverIncidencia(Incidencias incidencia) {
	        incidencias_pendientes.remove(incidencia);
	        GestorArchivos.guardarIncidenciasPendientes(incidencias_pendientes);
	        incidencias_resueltas.add(incidencia);
	        GestorArchivos.guardarIncidenciasResueltas(incidencias_resueltas);
	    }

	    /**
	     * Método que se encarga de iterar en el array list de incidencias pendientes hasta encontrar la incidencia que solicita el usuario
	     * @param identificador Identificador que nos da el usuario para buscar la incidencia
	     * @return Devuelve la incidencia si la encuentra o vacía si no la encuentra
	     */
	    public Incidencias buscarIncidencia(String identificador) {
	        for (Incidencias incidencia : incidencias_pendientes) {
	            if (incidencia.getIdentificador().equals(identificador)) {
	                return incidencia;
	            }
	        }
	        return null;
	    }

	    /**
	     * Método que busca la incidencia que queremos actualizar
	     * @param incidenciaActualizada Devolvemos la incidencia actualizada
	     */
	    public void actualizarIncidencia(Incidencias incidenciaActualizada) {
	        for (int i = 0; i < incidencias_pendientes.size(); i++) {
	            if (incidencias_pendientes.get(i).getIdentificador().equals(incidenciaActualizada.getIdentificador())) {
	                incidencias_pendientes.set(i, incidenciaActualizada);
	                return;
	            }
	        }
	    }
	    /**
	     * Método que se encarga de guardar los array list en los ficheros
	     */
	    public void guardarListasEnArchivos() {
	        GestorArchivos.guardarIncidenciasPendientes(incidencias_pendientes);
	        GestorArchivos.guardarIncidenciasResueltas(incidencias_resueltas);
	        GestorArchivos.guardarIncidenciasEliminadas(incidencias_eliminadas);
	    }
}
