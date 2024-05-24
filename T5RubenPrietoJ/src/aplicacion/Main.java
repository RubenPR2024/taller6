package aplicacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import persistencia.ConexionBD;
import presentacion.Interfaz;

/**
 * Clase principal donde se ejecuta el programa
 */
public class Main {

	/**
	 * Principio del programa donde se ejecuta todo el programa completo
	 * @param args Array de argumentos
	 * @throws SQLException Excepcion en el caso de que falle la conexion a la base de datos
	 */
	public static void main(String[] args) throws SQLException {
		Connection con = ConexionBD.obtenerConexion();
		Scanner sc = new Scanner(System.in);
		Interfaz.imprimirBienvenida();
        boolean salir = false;
        while (!salir) {
            Interfaz.mostrarMenu();
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(Interfaz.sc.nextLine());
            if (opcion == 11) {
                salir = true;
            } else {
                Logica.ejecutarOpcion(opcion, sc, con);
            }
            Interfaz.esperaIntro();
        }
        System.out.println("Gracias por usar la aplicación. ¡Hasta luego!");
	}

}
