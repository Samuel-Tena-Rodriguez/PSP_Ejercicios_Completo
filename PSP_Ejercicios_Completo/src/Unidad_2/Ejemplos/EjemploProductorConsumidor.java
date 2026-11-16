package Unidad_2.Ejemplos;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

//Clase que representa los datos producidos
class DatoProducido { }

//Clase Productor: produce y coloca datos en la cola
class Productor implements Runnable {
 private BlockingQueue<DatoProducido> cola;
 private int contadorProduccion = 0;

 public Productor(BlockingQueue<DatoProducido> cola) {
     this.cola = cola;
 }

 @Override
 public void run() {
     try {
         while (true) {
             cola.put(new DatoProducido());
             contadorProduccion++;
             System.out.println("Productor ha producido #" + contadorProduccion);
         }
     } catch (InterruptedException e) {
         contadorProduccion--;
         Thread.currentThread().interrupt();
         System.out.println("Productor interrumpido. Total producidos: " + contadorProduccion);
     }
 }
}

//Clase Consumidor: extrae datos de la cola
class Consumidor implements Runnable {
 private BlockingQueue<DatoProducido> cola;
 private int contadorConsumo = 0;

 public Consumidor(BlockingQueue<DatoProducido> cola) {
     this.cola = cola;
 }

 @Override
 public void run() {
     try {
         while (true) {
             DatoProducido dato = cola.poll(10, TimeUnit.MILLISECONDS);
             if (dato != null) {
                 contadorConsumo++;
                 System.out.println("Consumidor ha consumido #" + contadorConsumo);
             }
         }
     } catch (InterruptedException e) {
         contadorConsumo--;
         Thread.currentThread().interrupt();
         System.out.println("Consumidor interrumpido. Total consumidos: " + contadorConsumo);
     }
 }
}

//Clase principal
public class EjemploProductorConsumidor {
 public static void main(String[] args) {
     BlockingQueue<DatoProducido> cola = new ArrayBlockingQueue<>(5);

     Thread productor = new Thread(new Productor(cola));
     Thread consumidor = new Thread(new Consumidor(cola));

     productor.start();
     consumidor.start();

     try {
         Thread.sleep(1000); // ejecuta durante 1 segundo
     } catch (InterruptedException e) {
         e.printStackTrace();
     }

     productor.interrupt();
     consumidor.interrupt();
 }
}
