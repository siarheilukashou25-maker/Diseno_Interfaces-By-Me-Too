# Diagrama de clases - Botones Bymetoo

Diagrama UML de los botones personalizados de la app Bymetoo.

```mermaid
classDiagram
    direction TB

    class JPanel

    class BotonBymetoo {
        <<abstract>>
        -String texto
        -String ventanaDestino
        -boolean navegacionAutomatica
        -boolean activo
        -EstadoBoton estado
        -Color colorFondo
        -Color colorHover
        -Color colorActivo
        -Color colorBorde
        -Color colorTexto
        -int radioBorde
        +getTexto() String
        +setTexto(String texto) void
        +getVentanaDestino() String
        +setVentanaDestino(String ventana) void
        +isActivo() boolean
        +setActivo(boolean activo) void
        +getEstado() EstadoBoton
        +pulsar() void
        +navegar() Window
        +addBotonBymetooListener(BotonBymetooListener l) void
        +removeBotonBymetooListener(BotonBymetooListener l) void
        #fireEstadoCambiado(EstadoBoton anterior, EstadoBoton nuevo) void
        #fireBotonPulsado() BotonBymetooEvent
    }

    class EstadoBoton {
        <<enumeration>>
        NORMAL
        HOVER
        ACTIVO
        DESHABILITADO
    }

    class BotonBymetooEvent {
        -Tipo tipo
        -EstadoBoton estadoAnterior
        -EstadoBoton estadoNuevo
        -String ventanaDestino
        +getEstadoNuevo() EstadoBoton
        +getVentanaDestino() String
        +cancelarNavegacion() void
    }

    class BotonBymetooListener {
        <<interface>>
        +estadoCambiado(BotonBymetooEvent e) void
        +botonPulsado(BotonBymetooEvent e) void
    }

    class BotonInicio {
        +BotonInicio()
    }
    class BotonPerfil {
        +BotonPerfil()
    }
    class BotonCompartir {
        +BotonCompartir()
    }
    class BotonGastos {
        +BotonGastos()
    }
    class BotonRepartos {
        +BotonRepartos()
    }
    class BotonNuevaCompra {
        +BotonNuevaCompra()
    }
    class BotonResumen {
        +BotonResumen()
    }
    class BotonSalir {
        +BotonSalir()
    }

    JPanel <|-- BotonBymetoo
    BotonBymetoo <|-- BotonInicio
    BotonBymetoo <|-- BotonPerfil
    BotonBymetoo <|-- BotonCompartir
    BotonBymetoo <|-- BotonGastos
    BotonBymetoo <|-- BotonRepartos
    BotonBymetoo <|-- BotonNuevaCompra
    BotonBymetoo <|-- BotonResumen
    BotonBymetoo <|-- BotonSalir

    BotonBymetoo --> "1" EstadoBoton : estado
    BotonBymetoo ..> BotonBymetooEvent : lanza
    BotonBymetoo o--> "0..*" BotonBymetooListener : oyentes
```

## Qué significa cada cosa

- **JPanel → BotonBymetoo**: BotonBymetoo hereda de JPanel, así que es un componente de Swing.
- **BotonBymetoo**: es la clase padre (abstracta). Tiene todo lo común: propiedades, estados, eventos y el cambio de ventana.
- **Los 8 botones** (BotonInicio, BotonPerfil...): heredan de BotonBymetoo y solo dicen su texto, su icono y a qué ventana van.
- **EstadoBoton**: los 4 estados del boceto. Cada botón tiene 1 estado en cada momento.
- **BotonBymetooEvent**: el evento propio. El botón lo lanza cuando cambia de estado o cuando se pulsa.
- **BotonBymetooListener**: la interfaz para escuchar los eventos. Un botón puede tener 0 o muchos listeners (oyentes).

## Leyenda

| Símbolo | Significado |
|---|---|
| `-` | privado |
| `+` | público |
| `#` | protegido |
| Flecha con triángulo | herencia |
| Flecha discontinua | dependencia (lo usa) |
| Rombo | agregación (tiene una lista de) |
