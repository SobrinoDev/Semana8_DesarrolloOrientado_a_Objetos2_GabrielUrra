package Gestion_Pedidos;

public class PedidoEncomienda extends Pedido {

    private double pesoKg;
    private boolean embalajeCorrecto;

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm, double pesoKg,
                             boolean embalajeCorrecto) {
        super(idPedido, direccionEntrega, distanciaKm, "Pedido Encomienda");
        this.pesoKg = pesoKg;
        this.embalajeCorrecto = embalajeCorrecto;
    }

    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(20 + 1.5 * distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        super.asignarRepartidor();
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        if (embalajeCorrecto && pesoKg <= 20) {
            System.out.println("→ Validando peso y embalaje... OK");
            System.out.println("→ Pedido asignado a " + nombreRepartidor);
        } else {
            System.out.println("→ Validando peso y embalaje... FALLIDA (peso: " + pesoKg
                    + " kg, embalaje correcto: " + embalajeCorrecto + ")");
            System.out.println("→ No se pudo asignar el pedido a " + nombreRepartidor);
        }
    }
}
