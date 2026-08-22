import Gestion_Pedidos.Pedido;
import Gestion_Pedidos.PedidoComida;
import Gestion_Pedidos.PedidoEncomienda;
import Gestion_Pedidos.PedidoExpress;

public class Main {
    public static void main(String[] args) {

        // Semana 2: clase abstracta Pedido, mostrarResumen() y calcularTiempoEntrega()
        Pedido[] pedidos = {
                new PedidoComida(1, "Av. Italia 456", 4, true),
                new PedidoEncomienda(2, "Av. Independencia 123", 6, 8.5, true),
                new PedidoExpress(3, "Av. Apoquindo 1500", 7, true)
        };

        for (Pedido pedido : pedidos) {
            pedido.mostrarResumen();
            System.out.println("Tiempo estimado de entrega: " + pedido.calcularTiempoEntrega() + " minutos");
            System.out.println();
        }

        // Semana 1: sobrecarga y sobreescritura de asignarRepartidor()
        System.out.println("=== Asignación de repartidores ===\n");

        String[] repartidores = {"Juan Pérez", "Camila Soto", "Luis Díaz"};

        for (int i = 0; i < pedidos.length; i++) {
            pedidos[i].asignarRepartidor();
            pedidos[i].asignarRepartidor(repartidores[i]);
            System.out.println();
        }
    }
}
