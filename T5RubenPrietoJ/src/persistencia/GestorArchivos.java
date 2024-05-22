package persistencia;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import dominio.Incidencias;
/**
 * La clase GestorArchivos se encarga de gestionar el almacenamiento y la recuperación de
 * incidencias en archivos.
 */
public class GestorArchivos {
	/**
     * Nombre del archivo donde se almacenan las incidencias pendientes.
     */
	private static final String PENDIENTES_FILE = "pendientes.dat";
	/**
     * Nombre del archivo donde se almacenan las incidencias resueltas.
     */
	private static final String RESUELTAS_FILE = "resueltas.dat";
	/**
     * Nombre del archivo donde se almacenan las incidencias eliminadas.
     */
	private static final String ELIMINADAS_FILE = "eliminadas.dat";

	 /**
     * Guarda la lista de incidencias pendientes en el archivo correspondiente.
     *
     * @param pendientes La lista de incidencias pendientes a guardar.
     */
    public static void guardarIncidenciasPendientes(ArrayList<Incidencias> pendientes) {
        guardarArrayList(pendientes, PENDIENTES_FILE);
    }

    /**
     * Carga la lista de incidencias pendientes desde el archivo correspondiente.
     *
     * @return La lista de incidencias pendientes cargadas.
     */
    public static ArrayList<Incidencias> cargarIncidenciasPendientes() {
        crearArchivoSiNoExiste(PENDIENTES_FILE);
        return cargarArrayList(PENDIENTES_FILE);
    }

    /**
     * Guarda la lista de incidencias resueltas en el archivo correspondiente.
     *
     * @param resueltas La lista de incidencias resueltas a guardar.
     */

    public static void guardarIncidenciasResueltas(ArrayList<Incidencias> resueltas) {
        guardarArrayList(resueltas, RESUELTAS_FILE);
    }

    /**
     * Carga la lista de incidencias resueltas desde el archivo correspondiente.
     *
     * @return La lista de incidencias resueltas cargadas.
     */
    public static ArrayList<Incidencias> cargarIncidenciasResueltas() {
        crearArchivoSiNoExiste(RESUELTAS_FILE);
        return cargarArrayList(RESUELTAS_FILE);
    }

    /**
     * Guarda la lista de incidencias eliminadas en el archivo correspondiente.
     *
     * @param eliminadas La lista de incidencias eliminadas a guardar.
     */
    public static void guardarIncidenciasEliminadas(ArrayList<Incidencias> eliminadas) {
        guardarArrayList(eliminadas, ELIMINADAS_FILE);
    }

    /**
     * Carga la lista de incidencias eliminadas desde el archivo correspondiente.
     *
     * @return La lista de incidencias eliminadas cargadas.
     */
    public static ArrayList<Incidencias> cargarIncidenciasEliminadas() {
        crearArchivoSiNoExiste(ELIMINADAS_FILE);
        return cargarArrayList(ELIMINADAS_FILE);
    }

    /**
     * Crea un archivo vacío si no existe.
     *
     * @param nombreArchivo El nombre del archivo a crear.
     */
    private static void crearArchivoSiNoExiste(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Guarda una lista de incidencias en un archivo.
     *
     * @param lista La lista de incidencias a guardar.
     * @param nombreArchivo El nombre del archivo donde se guardará la lista.
     */
    private static void guardarArrayList(ArrayList<Incidencias> lista, String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga una lista de incidencias desde un archivo.
     *
     * @param nombreArchivo El nombre del archivo desde donde se cargará la lista.
     * @return La lista de incidencias cargada.
     */
    private static ArrayList<Incidencias> cargarArrayList(String nombreArchivo) {
    	ArrayList<Incidencias> lista = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            lista = (ArrayList<Incidencias>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Archivo no encontrado, puede ser la primera vez que se ejecuta el programa
            System.out.println("El archivo " + nombreArchivo + " no existe. Se creará uno nuevo.");
        } catch (EOFException e) {
            // Fin de archivo alcanzado (archivo vacío), no hay datos que leer
            System.out.println("El archivo " + nombreArchivo + " está vacío. No se han cargado datos.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
