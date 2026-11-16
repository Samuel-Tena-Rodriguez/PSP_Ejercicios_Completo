package Unidad_1.Ejercicio.Ejercicio1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * Clase LimpiarYOrdenarLecturas.
 * Información sobre el método Collections.sort(), consultada en:
 * https://academiasanroque.com/ordenar-arraylist-en-java-metodos-y-ejemplos-practicos/
 */

/**
 *
 * @author Samuel Tena Rodriguez
 */
public class LimpiarYOrdenarLecturas {
	/*
	 * Ejercicio 1, Aplicación 2. Lee de STDIN, descarta las líneas mal formadas o
	 * fuera de rango (valor <-20 o > 60), normaliza el valor a 2 decimales y ordena
	 * por sensorId y timestamp ascendente. Muestra por STDOUT.
	 */
	public static void main(String[] args) {
		try {
			// Leemos los datos de SimularLecturas, por ello utilizamos System.in para
			// recoger del propio sistema.
			BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
			// Creamos un Array para recoger los datos
			ArrayList<String[]> listado = new ArrayList();
			// Guardamos los datos recogidos en el array
			String linea;
			while ((linea = leer.readLine()) != null) {
				String[] leido = linea.split(";");
				// Descartamos las líneas mal formadas.
				if (leido.length == 3) {
					// Vemos los datos obtenidos de cada sentencia
					String sensorId = leido[0];
					String timestamp = leido[1];
					double valor = Double.parseDouble(leido[2]);
					// Comprobamos que el "Valor" esté dentro del rango: <-20 y >60
					// System.out.println("SensorId: " + sensorId);
					// System.out.println("timestamp: " + timestamp);
					// System.out.println("Valor: " + valor);
					// Descartamos los valores del rango: < -20 y > 60
					if (valor >= -20 && valor <= 60) {
						// Normalizamos los dos decimales
						valor = Math.round(valor * 100) / 100;
						// Ya que hemos convertido a 2 decimales, lo guardamos en el ArrayList
						listado.add(new String[] { sensorId, timestamp, String.valueOf(valor) });
					}
				}

			}
			// Cerramos leer por precaución y buena costumbre
			leer.close();

			// Ordenamos el ArrayList
			Collections.sort(listado, new Comparator<String[]>() {
				@Override
				public int compare(String[] sensorId1, String[] sensorId2) {
					int estadoComparar = sensorId1[0].compareTo(sensorId2[0]);
					// Verificamos la comparación
					if (estadoComparar == 0) {
						return sensorId1[1].compareTo(sensorId2[1]);
					}
					return estadoComparar;
				}
			});

			// Mostramos por STDOUT el resultado.
			for (String[] mostrar : listado) {
				System.out.println(mostrar[0] + ";" + mostrar[1] + ";" + mostrar[2]);
			}

		} catch (Exception e) {
			System.out.println("Error LimpiarYOrdenar: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
