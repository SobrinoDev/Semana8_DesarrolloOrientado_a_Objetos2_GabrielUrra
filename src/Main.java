import Sincronizacion.Pedido;
import Sincronizacion.Repartidor;
import Sincronizacion.ZonaDeCarga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        zonaDeCarga.agregarPedido(new Pedido(1, "Av. Italia 456"));
        zonaDeCarga.agregarPedido(new Pedido(2, "Av. Vicuña Mackenna 200"));
        zonaDeCarga.agregarPedido(new Pedido(3, "Av. Independencia 123"));
        zonaDeCarga.agregarPedido(new Pedido(4, "Calle Los Aromos 456"));
        zonaDeCarga.agregarPedido(new Pedido(5, "Av. Apoquindo 1500"));
        zonaDeCarga.agregarPedido(new Pedido(6, "Pasaje Las Rosas 789"));

        System.out.println("\n=== Iniciando reparto concurrente desde la zona de carga ===\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Repartidor("Juan Pérez", zonaDeCarga));
        executor.submit(new Repartidor("Camila Soto", zonaDeCarga));
        executor.submit(new Repartidor("Luis Díaz", zonaDeCarga));

        // No se aceptan más tareas; espera hasta que los 3 repartidores vacíen la zona de carga
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);

        System.out.println("\nTodos los pedidos han sido entregados correctamente");
    }
}
