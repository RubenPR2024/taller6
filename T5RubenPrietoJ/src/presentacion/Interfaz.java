package presentacion;

import dominio.Estado;
import dominio.Incidencias;
import dominio.Listas;
import persistencia.ConexionBD;
import persistencia.IncidenciasDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import aplicacion.Logica;

/**
 * Clase que gestiona la interfaz de usuario para interactuar con las incidencias.
 */
public class Interfaz {

	/**
	 * Entrada de datos desde el teclado.
	 */
    public static Scanner sc = new Scanner(System.in);
    /**
     * Lista de incidencias
     */
    private static Listas listas = new Listas();
    /**
     * Instancia del DAO de incidencias
     */
    private static IncidenciasDAO incidenciasDAO = new IncidenciasDAO();

    /**
     * Menu de bienvenida
     */
    Connection con = null;
    public static void imprimirBienvenida() {
        System.out.println("*******************************");
        System.out.println("REGISTRO DE INCIDENCIAS");
        System.out.println("*******************************");
        System.out.println(
                "\nBienvenido/a a la aplicación de registro de incidencias.\nUse las opciones del menú para trabajar.");
    }

    /**
     * Menu principal
     */
    public static void mostrarMenu() {
        System.out.println("*******MENÚ*******");
        System.out.println("1. Registrar incidencia");
        System.out.println("2. Buscar incidencia");
        System.out.println("3. Modificar incidencia");
        System.out.println("4. Eliminar incidencia");
        System.out.println("5. Resolver incidencia");
        System.out.println("6. Modificar incidencia resuelta");
        System.out.println("7. Devolver incidencia resuelta");
        System.out.println("8. Mostrar incidencias pendientes");
        System.out.println("9. Mostrar incidencias resueltas");
        System.out.println("10. Mostrar incidencias eliminadas");
        System.out.println("11. Salir");
    }

    /**
     * Metodo para esperar intro del usuario
     */
    public static void esperaIntro() {
        System.out.println("\nPulsa Enter para continuar ...");
        sc.nextLine();
    }

    /**
     * Método para saltar un número específico de lineas
     * @param numLineas	Número de líneas a saltar
     */
    public static void saltarLineas(int numLineas) {
        for (int i = 1; i <= numLineas; i++) {
            System.out.println();
        }
    }

    /**
     * Método que informa del resultado de las operaciones de cada opción
     * @param opcion				Opcion elegida por el usuario del menu
     * @param operacionCorrecta		Bandera para conocer el resultado de la operacion
     */
    public static void informaResultado(int opcion, boolean operacionCorrecta) {
        switch (opcion) {
            case 1 -> {
                if (operacionCorrecta)
                    System.out.println("\nLa incidencia se ha registrado correctamente");
                else
                    System.out.println("\nHa ocurrido un error al registrar la incidencia");
            }
            case 2 -> {
                if (operacionCorrecta)
                    System.out.println("\nIncidencia encontrada");
                else
                    System.out.println("\nNo se ha encontrado la incidencia");
            }
            case 3 -> {
                if (operacionCorrecta)
                    System.out.println("\nIncidencia modificada");
                else
                    System.out.println("\nHa ocurrido un error al modificar la incidencia");
            }
            case 4 -> {
                if (operacionCorrecta)
                    System.out.println("\nIncidencia eliminada correctamente");
                else
                    System.out.println("\nHa ocurrido un error al eliminar la incidencia");
            }
            case 5 -> {
                if (operacionCorrecta)
                    System.out.println("\nIncidencia resuelta correctamente");
                else
                    System.out.println("\nHa ocurrido un error al resolver la incidencia");
            }
            case 6 -> {
                if (operacionCorrecta)
                    System.out.println("\nIncidencia resuelta modificada correctamente");
                else
                    System.out.println("\nHa ocurrido un error al modificar la incidencia resuelta");
            }
            case 7 -> {
                if (operacionCorrecta)
                    System.out.println("\nIncidencia resuelta devuelta correctamente");
                else
                    System.out.println("\nHa ocurrido un error al devolver la incidencia resuelta");
            }
            case 8, 9, 10 -> {
                if (!operacionCorrecta)
                    System.out.println("\nLista vacía");
            }
        }
    }

    /**
     * Método para solicitar los datos al usuario para crear una nueva incidencia
     * @return	ArrayList de datos para crear una nueva incidencia
     */
    public static ArrayList<String> registrarIncidencia() {
        ArrayList<String> incidenciaDatos = new ArrayList<>();
        System.out.println("Ingrese los siguientes datos para registrar la incidencia:");
        System.out.print("Puesto: ");
        String puesto = sc.nextLine();
        incidenciaDatos.add(puesto);
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();
        incidenciaDatos.add(descripcion);
        return incidenciaDatos;
    }

    /**
     * Método que solicita al usuario el identificador de la incidencia en formato (dd/MM/yyyy-HH:mm-1)
     * @param opcion	Opcion del menu
     * @return			Identificador de la incidencia
     */
    public static String identificadorIncidencia(int opcion) {
        System.out.println("Ingrese el identificador de la incidencia:");
        return sc.nextLine();
    }

    /**
     * Método que solicita al usuario los datos a añadir de la incidencia resuelta
     * @return	Lista de datos de la incidencia a eliminar
     */
    public static ArrayList<String> eliminarIncidencia() {
        ArrayList<String> incidenciaDatos = new ArrayList<>();
        System.out.println("Ingrese los siguientes datos para eliminar la incidencia:");
        System.out.print("Fecha de eliminación (dd/MM/yyyy): ");
        String fechaEliminacion = sc.nextLine();
        incidenciaDatos.add(fechaEliminacion);
        System.out.print("Causa de eliminación: ");
        String causaEliminacion = sc.nextLine();
        incidenciaDatos.add(causaEliminacion);
        return incidenciaDatos;
    }

    /**
     * Método que solicita al usuario los datos a añadir a la incidencia resolver
     * @return	Lista de datos de la incidencia a resolver
     */
    public static ArrayList<String> resolverIncidencia() {
        ArrayList<String> incidenciaDatos = new ArrayList<>();
        System.out.println("Ingrese los siguientes datos para resolver la incidencia:");
        System.out.print("Fecha de resolución (dd/MM/yyyy): ");
        String fechaResolucion = sc.nextLine();
        incidenciaDatos.add(fechaResolucion);
        System.out.print("Resolución: ");
        String resolucion = sc.nextLine();
        incidenciaDatos.add(resolucion);
        return incidenciaDatos;
    }

    /**
     * Método que solicita al usuario la nueva descripción de la incidencia pendiente
     * @return	Nueva descripcion de la incidencia
     */
    public static String modificarIncidencia() {
        System.out.println("Ingrese la nueva descripción de la incidencia:");
        return sc.nextLine();
    }
    /**
     * Método que solicita al usuario la nueva resolución de la incidencia resuelta
     * @return	Nueva resolucion de la incidencia
     */
    public static String modificarIncidenciaResuelta() {
        System.out.println("Ingrese la nueva descripción de la incidencia:");
        return sc.nextLine();
    }

    /**
     * Método principal para ejecutar el menú y las operaciones
     */
    public static void ejecutar() {
    	Connection con = null;
        try {
            con = ConexionBD.conectar();
        imprimirBienvenida();
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1 -> {
                    ArrayList<String> datosIncidencia = registrarIncidencia();
                    String codigo = IncidenciasDAO.generarCodigoIncidencia(new Date());
                    boolean resultado = IncidenciasDAO.registrarIncidencia(con, Estado.Pendiente,
                            Integer.parseInt(datosIncidencia.get(0)), datosIncidencia.get(1));
                    informaResultado(1, resultado);
                }
                case 2 -> {
                    String identificador = identificadorIncidencia(2);
                    boolean resultado = IncidenciasDAO.buscarIncidencia(con, identificador) != null;
                    informaResultado(2, resultado);
                }
                case 3 -> {
                    String identificador = identificadorIncidencia(3);
                    String nuevaDescripcion = modificarIncidencia();
                    boolean resultado = IncidenciasDAO.modificarIncidencia(con, identificador, nuevaDescripcion);
                    informaResultado(3, resultado);
                }
                case 4 -> {
                    String identificador = identificadorIncidencia(4);
                    ArrayList<String> datosEliminacion = eliminarIncidencia();
                    try {
                    Date fechaEliminacion = new SimpleDateFormat("dd/MM/yyyy").parse(datosEliminacion.get(0));
                    boolean resultado = IncidenciasDAO.eliminarIncidencia(con, identificador, fechaEliminacion, datosEliminacion.get(1));
					informaResultado(4, resultado);
	                } catch (ParseException e) {
	                    e.printStackTrace();
	                }
                }
                case 5 -> {
                    String identificador = identificadorIncidencia(5);
                    ArrayList<String> datosResolucion = resolverIncidencia();
                    try {
                    	Date fechaResolucion = new SimpleDateFormat("dd/MM/yyyy").parse(datosResolucion.get(0));
                        boolean resultado = IncidenciasDAO.resolverIncidencia(con, identificador, fechaResolucion, datosResolucion.get(1));
                        informaResultado(5, resultado);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                case 6 -> {
                    String identificador = identificadorIncidencia(6);
                    String nuevaDescripcion = modificarIncidenciaResuelta();
                    boolean resultado = IncidenciasDAO.modificarIncidenciaResuelta(con, identificador, nuevaDescripcion);
                    informaResultado(6, resultado);
                }
                case 7 -> {
                	String identificador = identificadorIncidencia(7);
                    boolean resultado = IncidenciasDAO.devolverIncidenciasResueltas(con, identificador);
                    informaResultado(7, resultado);
                }
                case 8 -> {
                    ArrayList<Incidencias> incidenciasPendientes = (ArrayList<Incidencias>) IncidenciasDAO.getIncidenciasPendientes(con);
                    if (incidenciasPendientes.isEmpty()) {
                        informaResultado(8, false);
                    } else {
                        incidenciasPendientes.forEach(System.out::println);
                    }
                }
                case 9 -> {
                    ArrayList<Incidencias> incidenciasResueltas = (ArrayList<Incidencias>) IncidenciasDAO.getIncidenciasResueltas(con);
                    if (incidenciasResueltas.isEmpty()) {
                        informaResultado(9, false);
                    } else {
                        incidenciasResueltas.forEach(System.out::println);
                    }
                }
                case 10 -> {
                    ArrayList<Incidencias> incidenciasEliminadas = (ArrayList<Incidencias>) IncidenciasDAO.getIncidenciasEliminadas(con);
                    if (incidenciasEliminadas.isEmpty()) {
                        informaResultado(10, false);
                    } else {
                        incidenciasEliminadas.forEach(System.out::println);
                    }
                }
                case 11 -> salir = true;
                default -> System.out.println("Opción no válida, por favor intente de nuevo.");
            }
            esperaIntro();
        }
        System.out.println("Gracias por usar la aplicación. ¡Hasta luego!");
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        ConexionBD.cerrarConexion(con);
    }
    }
}
