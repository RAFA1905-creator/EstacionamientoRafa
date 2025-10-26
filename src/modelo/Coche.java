package modelo;

public class Coche implements Runnable {

    private String nombre;
    private boolean vip;
    private Estacionamiento estacionamiento;

    public Coche(String nombre, boolean vip, Estacionamiento estacionamiento) {
        this.nombre = nombre;
        this.vip = vip;
        this.estacionamiento = estacionamiento;
    }

    @Override
    public void run() {
        boolean aparcado = estacionamiento.entrar(this);
        if (aparcado == true) {
            try {
                int tiempo = 2000 + ((int) (Math.random() * 4000));
                Thread.sleep(tiempo);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            } finally {
                estacionamiento.salir(this);
            }
        }
    }

    public boolean esVip() {
        return vip;
    }

    @Override
    public String toString() {
        return nombre + (vip ? " (VIP)" : " (Normal)");
    }
}