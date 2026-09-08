package Gestion_Pedidos;

import Interfaces_Pedido.Cancelable;
import Interfaces_Pedido.Despachable;

public abstract class Pedido implements Despachable, Cancelable {

    protected int idPedido;
    protected String direccionEntrega;
    protected double distanciaKm;
    protected String tipoPedido;
    protected String estado;

    public Pedido(int idPedido, String direccionEntrega, double distanciaKm, String tipoPedido) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.tipoPedido = tipoPedido;
        this.estado = "Pendiente";
    }

    public void mostrarResumen() {
        System.out.printf("%s #%03d%n%n", getClass().getSimpleName(), idPedido);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + formatDistanciaKm() + " km");
    }

    public abstract int calcularTiempoEntrega();

    public void asignarRepartidor() {
        System.out.println("[" + tipoPedido + "]");
        System.out.println("Asignando repartidor...");
    }
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("→ Pedido asignado a " + nombreRepartidor);
    }

    // Implementación de Despachable
    @Override
    public void despachar() {
        if ("Cancelado".equals(estado)) {
            System.out.println("No se puede despachar el pedido #" + formatId() + ", ya fue cancelado.");
            return;
        }
        estado = "Despachado";
        System.out.println("Pedido #" + formatId() + " despachado hacia " + direccionEntrega + ".");
    }

    // Implementación de Cancelable
    @Override
    public void cancelar() {
        if ("Despachado".equals(estado)) {
            System.out.println("No se puede cancelar el pedido #" + formatId() + ", ya fue despachado.");
            return;
        }
        estado = "Cancelado";
        System.out.println("Pedido #" + formatId() + " cancelado.");
    }

    private String formatId() {
        return String.format("%03d", idPedido);
    }

    private String formatDistanciaKm() {
        if (distanciaKm == Math.rint(distanciaKm)) {
            return String.valueOf((int) distanciaKm);
        }
        return String.valueOf(distanciaKm);
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public String getEstado() {
        return estado;
    }
}
