package Unidad_1.Ejercicio.Ejercicio1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


/**
 *
 * @author Samuel Tena Rodríguez
 */
public class SimularLecturas {
	/*
	 * Ejercicio 1: SimularLecuras -- Genera 50 lecturas de sensores por salida
	 * estándar, una por línea, en formato CSV: <sensorId>;<timestamp>;<valor>
	 * S2;2025-10-01 09:31:12;21.70 S1;2025-09-30 18:02:55;12.30 S3;2025-10-01
	 * 09:30:48;33.90
	 */
	public static void main(String[] args) {
		try {
			// Creamos el nombre de los sensores ficticios:
			String[] sensores = { "S1", "S2", "S3" };
			// Crear un formateo de la fecha personalizado
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			// System.out.println("Formato:");
			// System.out.println("<sensorId>;<timestamp>;<valor>");
			// Generamos los números aleatorios con decimales, con Double
			Random rand = new Random();
			for (int i = 0; i < 50; i++) {
				// Asignamos un sensor
				int sensor = (int) (Math.random() * sensores.length);
				// Recogemos el nombre del sensor asignado del array
				String idSensor = sensores[sensor];
				// Generamos un número decimal aleatorio, comprendido entre -30 y mayor que 60
				double valor = -30 + (rand.nextDouble() * 100);
				// Mostramos los datos del sensor, la fecha y la hora, y el valor generado
				// Probamos que nos imprima correctamente las 50 lecturas, por eso está
				// comentado.
				// System.out.println(i + ": " + idSensor + ";" +
				// LocalDateTime.now().format(formato) + ";" + valor);
				System.out.println(idSensor + ";" + LocalDateTime.now().format(formato) + ";" + valor);
			}
		} catch (Exception e) {
			System.out.println("Se ha producido un error.\nDetalles: " + e.getMessage());
		}
	}

}
