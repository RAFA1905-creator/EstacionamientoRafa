package programa;

import modelo.Coche;
import modelo.Estacionamiento;

public class main {

    public static void main(String[] args) {

        //Crear instancia de estacionamiento
        Estacionamiento estacionamiento = new Estacionamiento();

        //Crear hilos (15)
        Thread[] hilos = new Thread[15];

        //Crear coches
        for (int i = 0; i < hilos.length; i++) {
            String nombre = "Coche " + (i + 1);
            boolean vip = ((i + 1) % 3 == 0);
            Coche coche = new Coche(nombre, vip, estacionamiento);
            hilos[i] = new Thread(coche);
            System.out.println("Se ha creado " + coche.toString());
        }
        System.out.println("");

        //Lanzar todos los hilos
        for (Thread hilo : hilos) {
            hilo.start();
        }

        //Unir hilos
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Error al esperar el hilo: " + e);
            }
        }

        System.out.println("No quedan coches en el estacionamiento.");

        //Detener estado en tiempo real
        estacionamiento.estadisticasFinales(hilos.length);
    }
}