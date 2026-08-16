# Explorando la sobrecarga y sobreescritura 

Proyecto formativo en Java que simula el sistema de asignación de repartidores de **SpeedFast**,
una empresa de reparto a domicilio con tres tipos de servicio: comida, encomiendas y compras
express. El objetivo es aplicar **polimorfismo** mediante:

- **Sobreescritura (`@Override`)**: cada subclase personaliza el comportamiento del método
  `asignarRepartidor()` heredado de la clase base.
- **Sobrecarga (*overload*)**: existe una segunda versión `asignarRepartidor(String
  nombreRepartidor)`, con distinta firma, que agrega la validación específica de cada tipo de
  pedido y confirma la asignación.

## Estructura del proyecto

```
src/
├── Main.java                       # Punto de entrada, prueba todas las clases
└── Gestion_Pedidos/
    ├── Pedido.java                 # Clase base
    ├── PedidoComida.java           # Valida mochila térmica
    ├── PedidoEncomienda.java       # Valida peso y embalaje
    └── PedidoExpress.java          # Valida cercanía y disponibilidad inmediata
```

## Jerarquía de clases

### `Pedido` (clase base)

Atributos comunes a todo pedido:

| Atributo           | Tipo   | Descripción                                   |
|---------------------|--------|------------------------------------------------|
| `idPedido`          | `int`  | Identificador del pedido                        |
| `direccionEntrega`  | `String` | Dirección donde se debe entregar               |
| `tipoPedido`        | `String` | Nombre del tipo de pedido (usado en los logs)  |

Define los dos métodos que serán sobrecargados y sobrescritos:

```java
public void asignarRepartidor()                       // versión genérica
public void asignarRepartidor(String nombreRepartidor) // versión sobrecargada
```

### `PedidoComida extends Pedido`

Agrega el atributo `mochilaTermica` (`boolean`). Al sobrecargar `asignarRepartidor(String)`
valida que el repartidor cuente con mochila térmica antes de confirmar la asignación.

### `PedidoEncomienda extends Pedido`

Agrega `pesoKg` (`double`) y `embalajeCorrecto` (`boolean`). La validación exige embalaje
correcto y un peso máximo de 20 kg.

### `PedidoExpress extends Pedido`

Agrega `distanciaKm` (`double`) y `disponibilidadInmediata` (`boolean`), simulando la búsqueda
del repartidor más cercano con disponibilidad inmediata.

## Cómo se combinan sobrecarga y sobreescritura

1. `asignarRepartidor()` (sin argumentos) está **sobrescrito** en cada subclase e imprime el
   encabezado del pedido y el mensaje "Asignando repartidor...".
2. `asignarRepartidor(String nombreRepartidor)` está **sobrecargado** (misma clase, distinta
   firma) y además **sobrescrito** en cada subclase: ejecuta la validación propia del tipo de
   pedido (mochila térmica, peso/embalaje o cercanía/disponibilidad) y, si es válida, confirma
   la asignación con el nombre del repartidor.

Llamando ambos métodos en secuencia sobre el mismo objeto se obtiene un flujo legible en
consola, por ejemplo:

```
[Pedido Comida]
Asignando repartidor...
→ Verificando mochila térmica... OK
→ Pedido asignado a Juan Pérez
```

Si la validación falla (por ejemplo, sin mochila térmica), el mensaje final indica que no fue
posible asignar el pedido en lugar de confirmarlo.

## `Main.java`

Crea una instancia de cada subclase, llama a ambas versiones de `asignarRepartidor()` y luego
repite el ejercicio con datos que **no** cumplen la validación, para mostrar el comportamiento
diferenciado (éxito vs. rechazo) de cada tipo de pedido.

## Cómo ejecutar

Desde IntelliJ IDEA basta con abrir el proyecto y ejecutar `Main.java`.

Desde línea de comandos, situado en `src/`:

```bash
javac -encoding UTF-8 -d out Main.java Gestion_Pedidos/*.java
java -Dfile.encoding=UTF-8 -cp out Main
```
