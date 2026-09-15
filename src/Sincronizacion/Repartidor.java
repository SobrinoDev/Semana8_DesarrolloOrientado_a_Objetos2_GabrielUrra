package Sincronizacion;

import java.util.concurrent.ThreadLocalRandom;

public class Repartidor implements Runnable {

    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        Pedido pedido;
        // Cada repartidor sigue retirando pedidos hasta que la zona de carga queda vacía;
        // retirarPedido() es synchronized, así que ningún pedido puede ser tomado dos veces
        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            pedido.setEstado(EstadoPedido.EN_REPARTO);
            System.out.println("[" + nombre + "] retiró el pedido #" + pedido.getId()
                    + " → " + pedido.getDireccionEntrega() + " (estado: " + pedido.getEstado() + ")");

            int tiempoSimuladoMs = ThreadLocalRandom.current().nextInt(1000, 3001);
            try {
                Thread.sleep(tiempoSimuladoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + nombre + "] entrega interrumpida.");
                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);
            System.out.println("[" + nombre + "] entregó el pedido #" + pedido.getId()
                    + " en " + tiempoSimuladoMs + " ms (estado: " + pedido.getEstado() + ")");
        }

        System.out.println("[" + nombre + "] no quedan más pedidos en la zona de carga, finaliza.");
    }

    public String getNombre() {
        return nombre;
    }
}
