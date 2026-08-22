# Semana 2 Desarrollo orientado a objetos II

Proyecto formativo en Java que simula el sistema de asignación y estimación de tiempos de
entrega de **SpeedFast**, una empresa de reparto a domicilio con tres tipos de servicio: comida,
encomiendas y compras express. El proyecto se desarrolla en dos semanas, aplicando distintos
conceptos de **polimorfismo** sobre la misma jerarquía de clases.

- **Semana 1 — Sobrecarga y sobreescritura**: `asignarRepartidor()` se **sobrescribe** en cada
  subclase, y `asignarRepartidor(String nombreRepartidor)` es una versión **sobrecargada** (misma
  funcionalidad, distinta firma) que agrega la validación propia de cada tipo de pedido.
- **Semana 2 — Clase abstracta**: `Pedido` pasa a ser una clase **abstracta** que centraliza los
  atributos y el método `mostrarResumen()`, y declara el método abstracto
  `calcularTiempoEntrega()`, que cada subclase implementa con su propia fórmula.

## Estructura del proyecto

```
src/
├── Main.java                       # Punto de entrada, prueba todas las clases
└── Gestion_Pedidos/
    ├── Pedido.java                 # Clase abstracta base
    ├── PedidoComida.java           # Valida mochila térmica
    ├── PedidoEncomienda.java       # Valida peso y embalaje
    └── PedidoExpress.java          # Valida cercanía y disponibilidad inmediata
```

## Jerarquía de clases

### `Pedido` (clase abstracta)

Atributos comunes a todo pedido:

| Atributo           | Tipo     | Descripción                                    |
|---------------------|----------|-------------------------------------------------|
| `idPedido`          | `int`    | Identificador del pedido                         |
| `direccionEntrega`  | `String` | Dirección donde se debe entregar                |
| `distanciaKm`       | `double` | Distancia a recorrer, usada para estimar el tiempo |
| `tipoPedido`        | `String` | Etiqueta del tipo de pedido (usada en los logs de asignación) |

Métodos:

```java
public void mostrarResumen()          // implementado: imprime id, dirección y distancia
public abstract int calcularTiempoEntrega(); // abstracto: cada subclase define su fórmula

public void asignarRepartidor()                       // sobrescrito por cada subclase
public void asignarRepartidor(String nombreRepartidor) // sobrecargado y sobrescrito
```

### `PedidoComida extends Pedido`

Agrega `mochilaTermica` (`boolean`).

- `calcularTiempoEntrega()`: **15 min + 2 min por cada km** (`15 + 2 * distanciaKm`).
- `asignarRepartidor(String)`: valida que el repartidor cuente con mochila térmica.

### `PedidoEncomienda extends Pedido`

Agrega `pesoKg` (`double`) y `embalajeCorrecto` (`boolean`).

- `calcularTiempoEntrega()`: **20 min + 1.5 min por km**, ajustado a entero
  (`Math.round(20 + 1.5 * distanciaKm)`).
- `asignarRepartidor(String)`: valida embalaje correcto y peso máximo de 20 kg.

### `PedidoExpress extends Pedido`

Agrega `disponibilidadInmediata` (`boolean`).

- `calcularTiempoEntrega()`: **10 min base**; si `distanciaKm > 5`, se suman **5 min extra**.
- `asignarRepartidor(String)`: valida cercanía/disponibilidad inmediata del repartidor.

## Semana 2: clase abstracta y `calcularTiempoEntrega()`

`Pedido` no puede instanciarse directamente (`abstract class`); solo sus subclases pueden
crearse, y cada una está obligada a implementar `calcularTiempoEntrega()` con su propia lógica.
`mostrarResumen()` sí está implementado en la clase base y es **heredado sin cambios** por las
tres subclases, reutilizando el nombre real de la clase (`getClass().getSimpleName()`) para
imprimir el encabezado:

```
PedidoComida #001

Dirección: Av. Italia 456
Distancia: 4 km
Tiempo estimado de entrega: 23 minutos
```

## Semana 1: sobrecarga y sobreescritura de `asignarRepartidor()`

1. `asignarRepartidor()` (sin argumentos) está **sobrescrito** en cada subclase e imprime el
   encabezado del pedido y el mensaje "Asignando repartidor...".
2. `asignarRepartidor(String nombreRepartidor)` está **sobrecargado** (misma clase, distinta
   firma) y además **sobrescrito** en cada subclase: ejecuta la validación propia del tipo de
   pedido y, si es válida, confirma la asignación con el nombre del repartidor.

```
[Pedido Comida]
Asignando repartidor...
→ Verificando mochila térmica... OK
→ Pedido asignado a Juan Pérez
```

## `Main.java`

1. Crea un arreglo `Pedido[]` con una instancia de cada subclase y, para cada una, llama a
   `mostrarResumen()` y `calcularTiempoEntrega()`, imprimiendo los tiempos estimados de forma
   comparativa (semana 2).
2. Reutiliza el mismo arreglo para llamar a `asignarRepartidor()` y
   `asignarRepartidor(String)` en sus versiones sobrescrita y sobrecargada (semana 1).

