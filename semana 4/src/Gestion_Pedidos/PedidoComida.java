package Gestion_Pedidos;

public class PedidoComida extends Pedido {

    private boolean mochilaTermica;

    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm, boolean mochilaTermica) {
        super(idPedido, direccionEntrega, distanciaKm, "Pedido Comida");
        this.mochilaTermica = mochilaTermica;
    }

    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(15 + 2 * distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        super.asignarRepartidor();
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        if (mochilaTermica) {
            System.out.println("→ Verificando mochila térmica... OK");
            System.out.println("→ Pedido asignado a " + nombreRepartidor);
        } else {
            System.out.println("→ Verificando mochila térmica... FALLIDA (no cuenta con mochila térmica)");
            System.out.println("→ No se pudo asignar el pedido a " + nombreRepartidor);
        }
    }
}
