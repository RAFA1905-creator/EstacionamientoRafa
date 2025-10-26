package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Estacionamiento {
    private final int capacidadMaxima = 5;
    private final Semaphore semaforo = new Semaphore(capacidadMaxima);
    private final ArrayList<Coche> cochesAparcados = new ArrayList<>();
    private int contadorCoches = 0;

    public boolean entrar(Coche coche) {
        boolean aparcado = false;
        if (coche.esVip() == false) {
            try {
                if (semaforo.tryAcquire(5, TimeUnit.SECONDS)) {
                    synchronized (cochesAparcados) {
                        cochesAparcados.add(coche);
                        aparcado = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Problema de " + coche.toString() + "al aparcar: " + e);
                return aparcado;
            }
        } else if (coche.esVip() == true) {
            try {
                synchronized (cochesAparcados) {
                    if (cochesAparcados.size() >= capacidadMaxima) {
                        desalojarCocheNormal(coche);
                    } else {
                        semaforo.acquire();
                    }
                    cochesAparcados.add(coche);
                    aparcado = true;
                }
            } catch (Exception e) {
                System.out.println("Problema de " + coche.toString() + "al aparcar: " + e);
            }
        }
        if (aparcado == true) {
            System.out.println(coche.toString() + " ha aparcado.");
            contadorCoches++;
        } else {
            System.out.println(coche + " no ha podido aparcar y se ha ido.");
        }
        return aparcado;
    }

    public void salir(Coche coche) {
        synchronized (cochesAparcados) {
            if (cochesAparcados.remove(coche)) {
                semaforo.release();
                System.out.println(coche + " ha salido del estacionamiento.");
            }
        }
    }

    public void desalojarCocheNormal(Coche cocheVip) throws InterruptedException {
        for (int i = 0; i < cochesAparcados.size(); i++) {
            Coche coche = cochesAparcados.get(i);
            if (!coche.esVip()) {
                cochesAparcados.remove(i);
                semaforo.release();
                cochesAparcados.add(cocheVip);
                semaforo.acquire();
                System.out.println(coche + " fue desalojado por " + cocheVip);
                break;
            }
        }
    }

    public void estadisticasFinales(int totalCoches) {
        System.out.println("Estadísticas finales:");
        System.out.println("- Han aparcado " + contadorCoches + " de " + totalCoches);
    }
}