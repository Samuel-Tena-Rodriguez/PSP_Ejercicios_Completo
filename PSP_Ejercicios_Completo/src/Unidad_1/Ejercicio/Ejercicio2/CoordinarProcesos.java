package Unidad_1.Ejercicio.Ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuel Tena Rodriguez Ejercicio 2: Aplicación 2 CoordinarProcesos -
 *         Lanza N instancias de TransformarLecturas, cada una procesando un
 *         tramo del fichero Fusiona en salida_final.csv respetando el orden
 *         global
 */
public class CoordinarProcesos {

	private static String ficheroDeEntrada;
	private static int totalProcesos;
	private static String ficheroSalidaFinal;
	private static String valorMinAlert;
	private static ArrayList<String[]> listado = new ArrayList<>();

	public static void main(String[] args) {
		// Verificamos los comandos introducidos por el usuario.
		if (args.length < 2) {
			System.out.println("Uso de la Aplicación:");
			System.out.println("La aplicación necesita mínimo 2 entradas, que son:");
			System.out.println("entrada.csv: el archivo de los datos en formato csv.");
			System.out.println("salida.csv: nombre del archivo de salida con extensión .csv.");
		} else if (args.length == 3) {
			// Solamente el usuario ha introducido "entrada.csv NumeroTotal salida_final.csv"
			totalProcesos = Integer.parseInt(args[1].trim());
			ficheros(args);
			// leerFicheroCSV();
		} else if (args.length == 4) {
			totalProcesos = Integer.parseInt(args[1].trim());
			// El usuario ha introducido el valor de minAlert, lo comprobamos
			String alerta = args[3];
			// Recogemos la posición de donde está el signo = de --minAlert
			int posicionIgual = alerta.indexOf("=");
			valorMinAlert = args[3].substring(posicionIgual + 1, alerta.length());
			ficheros(args);
			leerEscribirFicheros(args);
		} else {
			// No se cumple ninguna condición de los comandos introducidos por el usuario
			System.out.println("Uso de la aplicación:");
			System.out.println("java CoordinarProcesos \"entrada.csv\" X \"salida.csv\" \"--minAlert=XX.X\"");
			System.out.println("Donde: X es el número de veces a crear las salidas\nY XX.X son números con un decimal.");
		}
	}

	private static void ficheros(String[] argumentos) {
		ficheroDeEntrada = argumentos[0];
		ficheroSalidaFinal = argumentos[2];
	}
	
	private static void leerEscribirFicheros(String[] listados) {
		
		try (BufferedReader leer = new BufferedReader(new FileReader(ficheroDeEntrada));
				BufferedWriter escribir = new BufferedWriter(new FileWriter(ficheroSalidaFinal))) {
			// Contamos las líneas que tiene el fichero CSV
			int totalLineas = 0;
			String linea;
			while ((linea = leer.readLine()) != null) {
				// Separamos los datos del archivo CSV
				String[] partes = linea.split(";");
				// Comprobamos que las líneas siempre tengan 3 partes
				if (partes.length == 3) {
					String sensorId = partes[0];
					String timestamp = partes[1];
					double valor = Double.parseDouble(partes[2]);
					// Aumentamos el valor por cada línea que es leída.
					totalLineas++;
					// Cargamos los datos en el Listado
					listado.add(new String[] {sensorId, timestamp, String.valueOf(valor)});
				}
			}
			leer.close();
			// Calculamos cuantas líneas le corresponden a cada proceso
			int lineasProceso = totalLineas / totalProcesos;
			int resto = totalLineas % totalProcesos;
			int inicio = 0;
			File directorioPrueba = new File("pruebas");
			if (!directorioPrueba.exists()) {
				directorioPrueba.mkdirs();
			}
			// Escribimos los datos en los ficheros
			for (int i = 0; i < totalProcesos; i++) {
				// Calculamos las líneas que le corresponden al proceso.
				int cantidadLineas = lineasProceso;
				
				if (i < resto) {
					cantidadLineas++;
				}
				int fin = inicio + cantidadLineas;
				String entradaParcial = "pruebas/entrada_parcial_" + i + ".csv";
				String salidaParcial = "pruebas/salida_parcial_" + i + ".csv";
				
				// Escribimos los datos en los ficheros
				try (BufferedWriter escribirParcial = new BufferedWriter(new FileWriter(entradaParcial))) {
					for (int j = inicio; j < fin; j++) {
						String[] partes = listado.get(j);
						escribirParcial.write(partes[0] + ";" + partes[1] + ";" + partes[2]);
						escribirParcial.newLine();
					}
				} catch (Exception e) {
					System.out.println("Error al escribir los datos.\nDetalles: " + e.getMessage());
				}
				
				// Lanzamos el proceso de TransformarLecturas
				List<String> proceso = new ArrayList<String>();
				try {
					proceso.add("java");
					proceso.add("-cp");
                    proceso.add(".");
					proceso.add("TransformarLecturas");
					proceso.add(entradaParcial);
					proceso.add(salidaParcial);
					if (valorMinAlert != null) {
						proceso.add("--minAlert=" + valorMinAlert);
					}
					ProcessBuilder pb = new ProcessBuilder(proceso);
					// Redirigimos errores
					pb.inheritIO();
					Process p = pb.start();
					// Esperamos a que el proceso finalice
					int codigoSalida = p.waitFor();
					if (codigoSalida != 0) {
						System.out.println("El proceso " + i + " terminó con errores.");
					}
				} catch (Exception e) {
					System.out.println("Error al lanzar el proceso " + i + ".\nDetalles: " + e.getMessage());
				}
				// Actualizamos el inicio para el siguiente proceos
				inicio = fin;
				// Guardamos todos los datos en el fichero final
				try (BufferedWriter escribirFinal = new BufferedWriter(new FileWriter(ficheroSalidaFinal))) {
					for (String salidaPar : proceso) {
						try (BufferedReader leerParcial = new BufferedReader(new FileReader(salidaParcial))) {
							String lineas;
							while ((lineas = leerParcial.readLine()) != null) {
								escribirFinal.write(lineas);
								escribirFinal.newLine();
							}
						}
					}
					System.out.println("Proceso completado con exito.");
				} catch (Exception e) {
					System.out.println("Error al crear el fichero de salida final.");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
