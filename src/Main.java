import Gestion_Envios.ControladorDeEnvios;
import Gestion_Pedidos.Pedido;
import Gestion_Pedidos.PedidoComida;
import Gestion_Pedidos.PedidoEncomienda;
import Gestion_Pedidos.PedidoExpress;

public class Main {
    public static void main(String[] args) {

        ControladorDeEnvios controlador = new ControladorDeEnvios();

        PedidoComida pedidoComida = new PedidoComida(1, "Av. Italia 456", 4, true);
        PedidoEncomienda pedidoEncomienda = new PedidoEncomienda(2, "Av. Independencia 123", 6, 8.5, true);
        PedidoExpress pedidoExpress = new PedidoExpress(3, "Av. Apoquindo 1500", 7, true);

        controlador.registrarPedido(pedidoComida);
        controlador.registrarPedido(pedidoEncomienda);
        controlador.registrarPedido(pedidoExpress);

        // ===== Resumen y tiempo estimado de entrega (abstracción) =====
        System.out.println("=== Resumen y tiempo estimado ===\n");
        for (Pedido pedido : new Pedido[]{pedidoComida, pedidoEncomienda, pedidoExpress}) {
            controlador.mostrarTiempoEstimado(pedido);
            System.out.println();
        }

        // ===== Asignación de repartidores: automática y manual (polimorfismo) =====
        System.out.println("=== Asignación de repartidores ===\n");
        controlador.asignarRepartidorAutomatico(pedidoComida);
        controlador.asignarRepartidorManual(pedidoComida, "Juan Pérez");
        System.out.println();

        controlador.asignarRepartidorAutomatico(pedidoEncomienda);
        controlador.asignarRepartidorManual(pedidoEncomienda, "Camila Soto");
        System.out.println();

        controlador.asignarRepartidorAutomatico(pedidoExpress);
        controlador.asignarRepartidorManual(pedidoExpress, "Luis Díaz");
        System.out.println();

        // ===== Despacho y cancelación (interfaces Despachable / Cancelable) =====
        System.out.println("=== Despacho y cancelación ===\n");
        controlador.despacharPedido(pedidoComida);
        controlador.despacharPedido(pedidoExpress);
        controlador.cancelarPedido(pedidoEncomienda);

        // Casos inválidos: no se puede cancelar lo ya despachado ni despachar lo cancelado
        controlador.cancelarPedido(pedidoComida);
        controlador.despacharPedido(pedidoEncomienda);
        System.out.println();

        // ===== Historial de entregas (Rastreable) =====
        controlador.verHistorial();
    }
}
