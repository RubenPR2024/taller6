package aplicacion;

import java.util.Scanner;

import presentacion.Interfaz;

/**
 * Clase principal donde se ejecuta el programa
 */
public class Main {

	/**
	 * Principio del programa donde se ejecuta todo el programa completo
	 * @param args Array de argumentos
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Interfaz.imprimirBienvenida();
        boolean salir = false;
        while (!salir) {
            Interfaz.mostrarMenu();
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(Interfaz.sc.nextLine());
            if (opcion == 12) {
                salir = true;
            } else {
                Logica.ejecutarOpcion(opcion, sc);
            }
            Interfaz.esperaIntro();
        }
        System.out.println("Gracias por usar la aplicación. ¡Hasta luego!");
	}

}
