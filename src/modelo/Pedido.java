package modelo;

// Representa una fila de la tabla `pedido`. A diferencia de Gestion_Pedidos.Pedido
// (jerarquía polimórfica de las semanas 1-4), esta clase es un POJO plano que refleja
// exactamente las columnas de la base de datos, para usarse con PedidoDAO.
public class Pedido {

    private int id;
    private String direccion;
    private String tipo;
    private String estado;

    // Para insertar un pedido nuevo (el id lo asigna MySQL con AUTO_INCREMENT)
    public Pedido(String direccion, String tipo, String estado) {
        this(0, direccion, tipo, estado);
    }

    // Para reconstruir un pedido leído desde la base de datos
    public Pedido(int id, String direccion, String tipo, String estado) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", direccion='" + direccion + "', tipo='" + tipo
                + "', estado='" + estado + "'}";
    }
}
