package Unidad_2.Ejercicios;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Clase Cliente que contiene los datos del cliente para su posterior impresión.
 * 
 */
public class Cliente implements Runnable{
	private String id;
	private String material;
	private BlockingQueue<Cliente> cola;
	private boolean fin;
	
	public Cliente(String id, BlockingQueue<Cliente> cola) {
		this.id = id;
		this.cola = cola;
		// Array que contiene los tres tipos de materiales
		String[] materiales = {"PLA", "PETG", "ABS"};
		// Generamos el material de forma aleatoria
		Random aleatorio = new Random();
		// Seleccionamos un material aleatoriamente
		this.material = materiales[aleatorio.nextInt(materiales.length)];
		this.fin = false;
	}
	
	public Cliente() {
		this.fin = true;
	}
	
	public boolean getFin() {
		return fin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public BlockingQueue<Cliente> getCola() {
		return cola;
	}

	public void setCola(BlockingQueue<Cliente> cola) {
		this.cola = cola;
	}
	
	/*
	 * Añadimos el propio cliente a la cola
	 */
	@Override
	public void run() {
		try {
			System.out.println("[COLA] " + id + " solicita impresión de " + material);
			// Añadimos a la cola el cliente con this
			cola.put(this);
			System.out.println("[COLA] " + id + " añadido a la cola de impresión.");
		} catch (InterruptedException e) {
			// Capturamos por si el proceso se interrumpe
			Thread.currentThread().interrupt();
		}
	}

}
