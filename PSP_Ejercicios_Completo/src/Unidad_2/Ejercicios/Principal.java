package Unidad_2.Ejercicios;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class Principal {
	// Mapa donde se almacenan los resultados de las impresiones de cada impresora
	// donde se indica cuántas impresiones ha realizado de cada material.
	private static Map<String, Map<String, Integer>> resultados = new HashMap<>();

	public static void main(String[] args) {
		// Llamamos al método para inicializar los materiales de cada impresora
		inicializarResultados();
		// Se crea la cola de impresión donde los clientes añaden sus solicitudes
		// y donde las impresoras recogen los trabajos pendientes.
		BlockingQueue<Cliente> cola = new LinkedBlockingDeque<Cliente>();
		// Se crea el semáforo con 3 permisos para controlar el acceso a las impresoras.
		Semaphore semaforo = new Semaphore(3);
		// Se crea las impresoras pasando por parámetro la cola y el semáforo.
		Impresora imp1 = new Impresora("1", cola, semaforo);
		Impresora imp2 = new Impresora("2", cola, semaforo);
		Impresora imp3 = new Impresora("3", cola, semaforo);
		
		// Se crea los hilos correspondientes a cada impresora.
		Thread hilo1 = new Thread(imp1);
		Thread hilo2 = new Thread(imp2);
		Thread hilo3 = new Thread(imp3);
		// Iniciación de los hilos de las impresoras para que puedan trabajar
		hilo1.start();
		hilo2.start();
		hilo3.start();
		
		// Se crea un array de los hilos para almacenar los 6 hilos de los clientes
		Thread[] hilosClientes = new Thread[6];
		
		for (int i = 1; i <= 6; i++) {
			// Creamos los clientes y se pasa a la cola de impresión.
			Cliente cliente = new Cliente("Cliente-" + i, cola);
			// Se crea e inicializa el hilo correspondiente al cliente.
			hilosClientes[i - 1] = new Thread(cliente);
			hilosClientes[i - 1].start();
		}
		// Espera a que todos los clientes terminen de enviar las solicitudes
		for (int i = 0; i < hilosClientes.length; i++) {
			try {
				// Espera a que termine cada cliente.
				hilosClientes[i].join();
			} catch (Exception e) {
				// Si se produce una excepción, el hilo principal se interrumpe
				Thread.currentThread().interrupt();
				break;
			}
		}
		
		// Se añade tres clientes vacíos a la cola para indicar a las impresoras
		// que deben finalizar su ejecución.
		for (int i = 0; i < 3; i++) {
			try {
				cola.put(new Cliente());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		try {
			// Se espera a que terminen los tres hilos de las impresoras
			hilo1.join();
			hilo2.join();
			hilo3.join();
			// Se muestra el resumen de las impresiones realizadas.
			mostrarResultados();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	// Se inicializa los resultados de las tres impresoras
	// donde se establece a cero las impresiones de cada material.
	public static void inicializarResultados() {
		for (int i = 1; i <= 3; i++) {
			Map<String, Integer> materiales = new HashMap<String, Integer>();
			materiales.put("PLA", 0);
			materiales.put("PETG", 0);
			materiales.put("ABS", 0);
			resultados.put(String.valueOf(i), materiales);
		}
	}
	
	// Método syncrhonized para evitar que varios hilos modifiquen
	// simultáneamente los resultados de las impresiones.
	public static synchronized void registrarImpresion(String idImpresora, String material) {
		// Se obtiene los materiales correspondiente a la impresora
		Map<String, Integer> materiales = resultados.get(idImpresora);
		// Se consulta cuántas impresiones se han realizado de ese material
		int cantidad = materiales.get(material);
		// Se incrementa en uno el contador de impresiones
		materiales.put(material, cantidad + 1);
	}
	
	// Se muestra por pantalla el resumen de las impresiones realizadas
	// por cada una de las tres impresoras.
	public static void mostrarResultados() {
		System.out.println("=========== RESUMEN DE IMPRESIONES ==========");
		for (int i = 1; i <= 3; i++) {
			String id = String.valueOf(i);
			Map<String, Integer> materiales = resultados.get(id);
			System.out.println("Impresora " + id);
			System.out.println("PLA: " + materiales.get("PLA"));
			System.out.println("PETG: " + materiales.get("PETG"));
			System.out.println("ABS: " + materiales.get("ABS"));
		}
	}

}
