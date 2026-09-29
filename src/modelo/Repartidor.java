package modelo;

// Representa una fila de la tabla `repartidor`.
public class Repartidor {

    private int id;
    private String nombre;

    // Para insertar un repartidor nuevo (el id lo asigna MySQL con AUTO_INCREMENT)
    public Repartidor(String nombre) {
        this(0, nombre);
    }

    // Para reconstruir un repartidor leído desde la base de datos
    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Repartidor{id=" + id + ", nombre='" + nombre + "'}";
    }
}
