package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

// Representa una fila de la tabla `entrega`: la relación entre un pedido y el
// repartidor que lo entrega, con fecha y hora del registro.
public class Entrega {

    private int id;
    private final int idPedido;
    private final int idRepartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    // Para insertar una entrega nueva (el id lo asigna MySQL con AUTO_INCREMENT)
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this(0, idPedido, idRepartidor, fecha, hora);
    }

    // Para reconstruir una entrega leída desde la base de datos
    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    @Override
    public String toString() {
        return "Entrega{id=" + id + ", idPedido=" + idPedido + ", idRepartidor=" + idRepartidor
                + ", fecha=" + fecha + ", hora=" + hora + "}";
    }
}
