package Gestion_Pedidos;

public class PedidoExpress extends Pedido {

    private boolean disponibilidadInmediata;

    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm, boolean disponibilidadInmediata) {
        super(idPedido, direccionEntrega, distanciaKm, "Pedido Express");
        this.disponibilidadInmediata = disponibilidadInmediata;
    }

    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 10;
        if (distanciaKm > 5) {
            tiempo += 5;
        }
        return tiempo;
    }


    @Override
    public void asignarRepartidor() {
        super.asignarRepartidor();
    }


    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        if (disponibilidadInmediata) {
            System.out.println("→ Repartidor más cercano con disponibilidad inmediata encontrado.");
            System.out.println("→ Pedido asignado a " + nombreRepartidor);
        } else {
            System.out.println("→ No se encontró repartidor cercano con disponibilidad inmediata.");
            System.out.println("→ No se pudo asignar el pedido a " + nombreRepartidor);
        }
    }
}
