package aplicacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import dominio.Estado;
import dominio.Incidencias;
import dominio.Listas;
import persistencia.IncidenciasDAO;
import persistencia.XMLExportar;
import presentacion.Interfaz;

/**
 * La clase Logica contiene la lógica principal de la aplicación que gestiona las incidencias.
 */
public class Logica {
	/**
     * Lista de incidencias manejadas por la aplicación.
     */
	private Listas listas;

	/**
	 * Constructor por defecto que inicializa la lista de incidencias.
	 */
    public Logica() {
        this.listas = new Listas();
    }
    /**
     * Ejecuta la opción seleccionada por el usuario en el menú.
     *
     * @param opcion La opción seleccionada.
     * @param sc El escáner para la entrada del usuario.
     */
	public static void ejecutarOpcion(int opcion, Scanner sc) {
        switch (opcion) {
            case 1 -> { // Registrar incidencia
                ArrayList<String> incidenciasPendientes = Interfaz.registrarIncidencia();
                String codigo = generarCodigoIncidencia(new Date());
                boolean operacionCorrecta = IncidenciasDAO.registrarIncidencia(Estado.Pendiente,
                        Integer.parseInt(incidenciasPendientes.get(0)), incidenciasPendientes.get(1));
                Interfaz.informaResultado(opcion, operacionCorrecta);
            }
            case 2 -> { // Buscar incidencia
            	List<Incidencias> pendientes = IncidenciasDAO.getIncidenciasPendientes();
                System.out.println("Lista de incidencias pendientes:");
                for (Incidencias incidencia : pendientes) {
                    System.out.println("Código: " + incidencia.getIdentificador());
                }
                String identificador = Interfaz.identificadorIncidencia(opcion);
                boolean operacionCorrecta = IncidenciasDAO.buscarIncidencia(identificador) != null;
                Incidencias incidenciaEncontrada = IncidenciasDAO.buscarIncidencia(identificador);
                if (incidenciaEncontrada != null) {
                    System.out.println("\nInformación de la incidencia:");
                    System.out.println(incidenciaEncontrada);
                } else {
                    System.out.println("\nNo se ha encontrado la incidencia");
                }
                Interfaz.informaResultado(opcion, operacionCorrecta);
            }
            case 3 -> { // Modificar incidencia
                String identificador = Interfaz.identificadorIncidencia(opcion);
                String nuevaDescripcion = Interfaz.modificarIncidencia();
                boolean operacionCorrecta = IncidenciasDAO.modificarIncidencia(identificador, nuevaDescripcion);
                Interfaz.informaResultado(opcion, operacionCorrecta);
            }
            case 4 -> { // Eliminar incidencia
                String identificador = Interfaz.identificadorIncidencia(opcion);
                ArrayList<String> incidenciasEliminadas = Interfaz.eliminarIncidencia();
                try {
                    Date fechaEliminacion = new SimpleDateFormat("dd/MM/yyyy").parse(incidenciasEliminadas.get(0));
                    boolean operacionCorrecta = IncidenciasDAO.eliminarIncidencia(identificador, fechaEliminacion, incidenciasEliminadas.get(1));
                    Interfaz.informaResultado(opcion, operacionCorrecta);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            case 5 -> { // Resolver incidencia
                String identificador = Interfaz.identificadorIncidencia(opcion);
                ArrayList<String> incidenciasResueltas = Interfaz.resolverIncidencia();
                try {
                    Date fechaResolucion = new SimpleDateFormat("dd/MM/yyyy").parse(incidenciasResueltas.get(0));
                    boolean operacionCorrecta = IncidenciasDAO.resolverIncidencia(identificador, fechaResolucion, incidenciasResueltas.get(1));
                    Interfaz.informaResultado(opcion, operacionCorrecta);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            case 6 -> { // Modificar incidencia resuelta
                String identificador = Interfaz.identificadorIncidencia(opcion);
                String nuevaDescripcion = Interfaz.modificarIncidencia();
                boolean operacionCorrecta = IncidenciasDAO.modificarIncidenciaResuelta(identificador, nuevaDescripcion);
                Interfaz.informaResultado(opcion, operacionCorrecta);
            }
            case 7 -> { // Devolver incidencia resuelta
            	String identificador = Interfaz.identificadorIncidencia(opcion);
                boolean operacionCorrecta = IncidenciasDAO.devolverIncidenciasResueltas(identificador);
                Interfaz.informaResultado(opcion, operacionCorrecta);
            }
            case 8 -> { // Listar incidencias pendientes
                listarIncidencias(IncidenciasDAO.getIncidenciasPendientes(), "Listado de incidencias pendientes:");
            }
            case 9 -> { // Listar incidencias resueltas
                listarIncidencias(IncidenciasDAO.getIncidenciasResueltas(), "Listado de incidencias resueltas:");
            }
            case 10 -> { // Listar incidencias eliminadas
                listarIncidencias(IncidenciasDAO.getIncidenciasEliminadas(), "Listado de incidencias eliminadas:");
            }
            case 11 -> { // Exportar XML
            	Interfaz.menuXML();
                System.out.print("Seleccione una opción: ");
                int opcionXML = Integer.parseInt(sc.nextLine());
                switch (opcionXML) {
//                    case 1 -> exportarPendientesXML();
//                    case 2 -> exportarResueltasXML();
//                    case 3 -> exportarEliminadasXML();
                    default -> System.out.println("Opción no válida.");
                }
            }

            default -> System.out.println("Opción no válida.");
        }
    }

	/**
     * Lista las incidencias proporcionadas con un comentario.
     *
     * @param list La lista de incidencias a listar.
     * @param comentario El comentario a mostrar antes de la lista.
     */
    public static void listarIncidencias(List<Incidencias> list, String comentario) {
        System.out.println(comentario);
        if (list.isEmpty()) {
            System.out.println("No hay incidencias en esta categoría.");
        } else {
            for (Incidencias incidencia : list) {
                System.out.println(incidencia);
            }
        }
    }
    /**
     * Genera un código único para una incidencia basada en la fecha de registro.
     *
     * @param fechaRegistro La fecha de registro de la incidencia.
     * @return El código generado para la incidencia.
     */
    private static String generarCodigoIncidencia(Date fechaRegistro) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        String fechaFormateada = dateFormat.format(fechaRegistro);

        // Acceder al ArrayList de incidencias pendientes en el objeto listas
        ArrayList<Incidencias> incidenciasPendientes = Listas.getIncidenciasPendientes();

        // Contar el número de incidencias registradas en la misma fecha
        int contador = 1;
        for (Incidencias incidencia : incidenciasPendientes) {
            Date fechaIncidencia = incidencia.getFechaIncidencia();
            if (isSameDay(fechaRegistro, fechaIncidencia)) {
                contador++;
            }
        }

        // Concatenar la fecha formateada y el contador para generar el código único
        return fechaFormateada + "-" + contador;
    }
    
    /**
     * Verifica si dos fechas corresponden al mismo día.
     *
     * @param fecha1 La primera fecha.
     * @param fecha2 La segunda fecha.
     * @return true si ambas fechas son el mismo día, false en caso contrario.
     */
    public static boolean isSameDay(Date fecha1, Date fecha2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(fecha1).equals(fmt.format(fecha2));
    }
    
    /**
     * Exporta las incidencias pendientes a un archivo XML.
     */
//    public static void exportarPendientesXML() {
//        XMLExportar.exportarAXML(IncidenciasDAO.getIncidenciasPendientes(), "pendientes.xml");
//    }
//
//
//    /**
//     * Exporta las incidencias resueltas a un archivo XML.
//     */
//    public static void exportarResueltasXML() {
//        XMLExportar.exportarAXML(IncidenciasDAO.getIncidenciasResueltas(), "resueltas.xml");
//    }
//
//    /**
//     * Exporta las incidencias eliminadas a un archivo XML.
//     */
//    public static void exportarEliminadasXML() {
//        XMLExportar.exportarAXML(IncidenciasDAO.getIncidenciasEliminadas(), "eliminadas.xml");
//    }
}
