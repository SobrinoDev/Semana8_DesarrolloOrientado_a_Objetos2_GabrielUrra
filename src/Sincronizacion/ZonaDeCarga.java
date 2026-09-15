package Sincronizacion;

import java.util.LinkedList;
import java.util.Queue;

// Recurso compartido: varios Repartidor (hilos) acceden a ella al mismo tiempo.
// Los métodos synchronized garantizan que agregar/retirar sea atómico y que dos
// repartidores nunca puedan retirar el mismo pedido.
public class ZonaDeCarga {

    private final Queue<Pedido> pedidosPendientes = new LinkedList<>();

    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.add(pedido);
        System.out.println("Zona de carga: ingresó " + pedido + " (pendientes: " + pedidosPendientes.size() + ")");
    }

    // Retira y elimina un único pedido de forma exclusiva; null si ya no quedan pedidos
    public synchronized Pedido retirarPedido() {
        return pedidosPendientes.poll();
    }

    public synchronized int pedidosRestantes() {
        return pedidosPendientes.size();
    }
}
