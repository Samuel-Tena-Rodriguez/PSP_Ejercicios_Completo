package Unidad_1.Ejercicio.Ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
*
* @author Samuel Tena Rodriguez
* Ejercicio 2: Aplicación 1
* TransformarLecturas - Lee un CSV de entrada (mismo formato) y escribe un CSV
* de salida añadiendo:
* * ALERTA si valor >= 30.0 (umbral configurable con --minAlert=).
* * OK si valor < 30.0
*/
public class TransformarLecturas {
	static double minAlert = 30; // Valor por defecto
	static String ficheroEntrada;
	static String ficheroSalida;
	static String estado;
   
   public static void main(String[] args) {
       // Recogemos los datos introducidos por args por defecto
       
       if (args.length < 2) {
    	   System.out.println("Uso de la Aplicación:");
           System.out.println("La aplicación necesita mínimo 2 entradas, que son:");
           System.out.println("entrada.csv: el archivo de los datos en formato csv.");
           System.out.println("salida.csv: nombre del archivo de salida con extensión .csv.");
       } else if (args.length == 2) {
    	   // El usuario no ha indicado el --minAlert, lo dejamos por defecto
    	   ficheros(args);
    	   leerFicheroCSV();
       } else if (args.length >= 3) {
    	   // El usuario si ha indicado el --minAlert, pero lo verificamos por si no lo ha escrito bien.
    	   if (args[2].startsWith("--minAlert=")) {
    		   String alerta = args[2];
    		   // Recogemos la posición de donde está el signo Igual de minAlert
    		   int posicionIgual = alerta.indexOf("=");
    		   minAlert = Double.parseDouble(args[2].substring(posicionIgual + 1, alerta.length()));
    		   //System.out.println("VALOR: " + alerta.substring(posicionIgual + 1, alerta.length()));
    		   //System.out.println("minAlert: " + minAlert);
    		   ficheros(args);
    		   leerFicheroCSV();
    	   } else {
    		   System.out.println("--minAlert= está mal escrito. Escriba de nuevo correctamente.");
    	   }
       } else {
    	   System.out.println("Uso de la aplicación:");
    	   System.out.println("java TransformarLecturas \"entrada.csv\" \"salida.csv\" \"--minAlert=XX.X\"");
    	   System.out.println("Donde XX.X son números con un decimal.");
       }
   }
   
   private static void ficheros(String[] argumentos) {
	   ficheroEntrada = argumentos[0];
	   ficheroSalida = argumentos[1];
   }
   
   private static void leerFicheroCSV() {
	   // Método para leer el archivo CSV
	   try (BufferedReader leer = new BufferedReader(new FileReader(ficheroEntrada))) {
		//BufferedReader leer = new BufferedReader(new FileReader(ficheroEntrada));
		String linea;
		while ((linea = leer.readLine()) != null) {
			// Separamos los datos del archivo CSV
			String[] partes = linea.split(";");
			
			// Comprobamos que las líneas tengan siempre 3 partes
			if (partes.length == 3) {
				String sensorId = partes[0];
				String timestamp = partes[1];
				// Remplazamos el punto por una coma, para que sea mejor visible.
				double valor = Double.parseDouble(partes[2].replace(",", "."));
				
				// Verificamos el valor para compararlo con minAlert
				if (valor >= minAlert) {
					estado = "ALERTA";
				} else {
					estado = "OK";
				}
				
				// Escribimos los datos en el nuevo archivo CSV
				escribirNuevoCSV(partes);
			}
		}
		leer.close();
	} catch (Exception e) {
		System.out.println("Error al leer el fichero CSV de entrada.");
		System.out.println("Detalles: " + e.getMessage());
	}
   }
   
   private static void escribirNuevoCSV(String[] listado) {
	   try (BufferedWriter escribir = new BufferedWriter(new FileWriter(ficheroSalida, true))) {
		
		//for (int i = 0; i < listado.length; i++) {
			escribir.write(listado[0] + ";" + listado[1] + ";" + listado[2] + ";" + estado);
			escribir.newLine();
		//}
		// Cerramos el Buffered
		escribir.close();
	} catch (Exception e) {
		System.out.println("Se ha producido un error al generar el archivo CSV.");
		System.out.println("Detalles: " + e.getMessage());
	}
   }
   
}
