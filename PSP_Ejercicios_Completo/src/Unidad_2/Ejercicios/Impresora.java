package Unidad_2.Ejercicios;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Clase Impresora que representa a una impresora con sus atributos
 */
public class Impresora implements Runnable {
	// Atributos de la impresora
	private String id;
	private BlockingQueue<Cliente> colaClientes;
	private Semaphore semaforo;
	//private boolean permisoAdquirido = false;
	
	
	// Constructor parametrizado
	public Impresora(String id, BlockingQueue<Cliente> colaClientes, Semaphore semaforo) {
		this.id = id;
		this.colaClientes = colaClientes;
		this.semaforo = semaforo;
	}



	@Override
	public void run() {
		while (true) {
			// Inicamos que la impresora no tiene permiso de impresión.
			boolean permisoAdquirido = false;
			try {
				// Solicitamos un permiso, si no hay el cliente esperará.
				semaforo.acquire();
				// Recoge el primer cliente de la cola.
				permisoAdquirido = true;
				Cliente cliente = colaClientes.take();
				// Verificamos que el cliente no haya finalizado su impresión
				if (cliente.getFin()) {
					// Si la ha finalizado, paramos la ejecución
					break;
				}
				System.out.println("[ASIGNACIÓN] " + cliente.getId() + " asignado a " + id);
				System.out.println("[IMPRESORA-" + id + "] Iniciando impresión de " + cliente.getId() + " - Material: " + cliente.getMaterial());
				if (cliente.getMaterial().equals("PLA")) {
					Thread.sleep(Material.PLA);
				} else if (cliente.getMaterial().equals("PETG")) {
					Thread.sleep(Material.PETG);
				} else {
					Thread.sleep(Material.ABS);
				}
				Principal.registrarImpresion(id, cliente.getMaterial());
				System.out.println("[IMPRESORA-" + id + "] Finalizada la impresión de " + cliente.getId() + " - Material: " + cliente.getMaterial());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// Sale del bucle si se produce una interrupción
				break;
			} finally {
				// Si el cliente ya tiene el permiso para la impresión, liberamos el semáforo para que el siguiente pueda imprimir.
				if (permisoAdquirido) {
					semaforo.release();
				}
			}
		}

	}

}
