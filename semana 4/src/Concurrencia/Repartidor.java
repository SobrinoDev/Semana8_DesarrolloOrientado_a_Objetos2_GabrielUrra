package Concurrencia;

import Gestion_Pedidos.Pedido;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Repartidor implements Runnable {

    private final String nombre;
    private final List<Pedido> pedidosAsignados;

    public Repartidor(String nombre, List<Pedido> pedidosAsignados) {
        this.nombre = nombre;
        this.pedidosAsignados = pedidosAsignados;
    }

    @Override
    public void run() {
        System.out.println("[" + nombre + "] inicia su recorrido con " + pedidosAsignados.size() + " pedido(s).");

        for (Pedido pedido : pedidosAsignados) {
            String idFormateado = String.format("%03d", pedido.getIdPedido());
            System.out.println("[" + nombre + "] → recogiendo pedido #" + idFormateado
                    + " (" + pedido.getClass().getSimpleName() + "), tiempo estimado: "
                    + pedido.calcularTiempoEntrega() + " min.");

            int tiempoSimuladoMs = ThreadLocalRandom.current().nextInt(1000, 3001);
            try {
                Thread.sleep(tiempoSimuladoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + nombre + "] entrega interrumpida.");
                return;
            }

            pedido.despachar();
            if ("Despachado".equals(pedido.getEstado())) {
                System.out.println("[" + nombre + "] ✓ pedido #" + idFormateado + " entregado en "
                        + tiempoSimuladoMs + " ms.");
            } else {
                System.out.println("[" + nombre + "] ✗ pedido #" + idFormateado
                        + " no pudo entregarse (estado: " + pedido.getEstado() + ").");
            }
        }

        System.out.println("[" + nombre + "] finalizó todas sus entregas.");
    }

    public String getNombre() {
        return nombre;
    }
}
