package Gestion_Envios;

import Interfaces_Pedido.Cancelable;
import Interfaces_Pedido.Despachable;
import Gestion_Pedidos.Pedido;
import Interfaces_Pedido.Rastreable;

import java.util.ArrayList;
import java.util.List;

public class ControladorDeEnvios implements Rastreable {

    private final List<Pedido> historial = new ArrayList<>();

    public void registrarPedido(Pedido pedido) {
        historial.add(pedido);
    }

    public void asignarRepartidorAutomatico(Pedido pedido) {
        pedido.asignarRepartidor();
    }

    public void asignarRepartidorManual(Pedido pedido, String nombreRepartidor) {
        pedido.asignarRepartidor(nombreRepartidor);
    }

    public void mostrarTiempoEstimado(Pedido pedido) {
        pedido.mostrarResumen();
        System.out.println("Tiempo estimado de entrega: " + pedido.calcularTiempoEntrega() + " minutos");
    }

    public void despacharPedido(Despachable pedido) {
        pedido.despachar();
    }

    public void cancelarPedido(Cancelable pedido) {
        pedido.cancelar();
    }

    @Override
    public void verHistorial() {
        System.out.println("=== Historial de entregas ===");
        for (Pedido pedido : historial) {
            System.out.println("Pedido #" + String.format("%03d", pedido.getIdPedido())
                    + " (" + pedido.getClass().getSimpleName() + ") - Estado: " + pedido.getEstado());
        }
    }
}
