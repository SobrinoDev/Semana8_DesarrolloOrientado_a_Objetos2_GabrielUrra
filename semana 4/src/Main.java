import Concurrencia.Repartidor;
import Gestion_Envios.ControladorDeEnvios;
import Gestion_Pedidos.Pedido;
import Gestion_Pedidos.PedidoComida;
import Gestion_Pedidos.PedidoEncomienda;
import Gestion_Pedidos.PedidoExpress;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        ControladorDeEnvios controlador = new ControladorDeEnvios();

        // Pedidos del repartidor 1
        PedidoComida pedido1 = new PedidoComida(1, "Av. Italia 456", 4, true);
        PedidoComida pedido2 = new PedidoComida(2, "Av. Vicuña Mackenna 200", 3, true);

        // Pedidos del repartidor 2
        PedidoEncomienda pedido3 = new PedidoEncomienda(3, "Av. Independencia 123", 6, 8.5, true);
        PedidoEncomienda pedido4 = new PedidoEncomienda(4, "Calle Los Aromos 456", 5, 4.0, true);

        // Pedidos del repartidor 3
        PedidoExpress pedido5 = new PedidoExpress(5, "Av. Apoquindo 1500", 7, true);
        PedidoExpress pedido6 = new PedidoExpress(6, "Pasaje Las Rosas 789", 2, true);

        List<Pedido> todosLosPedidos = List.of(pedido1, pedido2, pedido3, pedido4, pedido5, pedido6);
        for (Pedido pedido : todosLosPedidos) {
            controlador.registrarPedido(pedido);
        }

        // Se cancela un pedido antes de salir a reparto, para mostrar que Repartidor
        // respeta la validación de Cancelable/Despachable ya definida en Pedido
        controlador.cancelarPedido(pedido6);
        System.out.println();

        Repartidor repartidor1 = new Repartidor("Juan Pérez", List.of(pedido1, pedido2));
        Repartidor repartidor2 = new Repartidor("Camila Soto", List.of(pedido3, pedido4));
        Repartidor repartidor3 = new Repartidor("Luis Díaz", List.of(pedido5, pedido6));

        System.out.println("=== Iniciando entregas simultáneas de SpeedFast ===\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(repartidor1);
        executor.submit(repartidor2);
        executor.submit(repartidor3);

        // No se aceptan más tareas; la simulación continúa hasta que los 3 repartidores terminen
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("\n=== Todos los repartidores finalizaron sus entregas ===\n");
        controlador.verHistorial();
    }
}
