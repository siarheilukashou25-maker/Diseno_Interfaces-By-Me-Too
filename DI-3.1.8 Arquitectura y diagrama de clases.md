# Sprint 3 · T-3.1.1 — Arquitectura del componente `BotonNavegacion`

Paquete: `com.dam.interfaces.componentes` · Java puro (Swing + JavaBeans) · Ant/NetBeans

## 1. Análisis del mockup

| Elemento del mockup | Decisión de diseño |
|---|---|
| Icono + texto en todos los botones | Propiedades `icono` (`Icon`) y `texto` (`String`) |
| Inicio / Perfil: sin borde, fondo gris claro en Hover y Activo | `EstiloBoton.PLANO` |
| Compartir / Gastos: borde gris claro, Activo verde | `EstiloBoton.CONTORNO` + `colorBorde` gris |
| Repartos / Nueva compra / Resumen / Salir: borde negro, Activo verde | `EstiloBoton.CONTORNO` + `colorBorde` negro |
| 4 columnas: Normal, Hover, Activo, Deshabilitado | `enum EstadoBoton` calculado, nunca asignado a mano |
| Esquinas redondeadas | Propiedad `radioEsquina` |

Un solo componente con propiedades configurables evita cinco clases casi idénticas y permite que el equipo lo arrastre desde la paleta de Matisse.

## 2. Diagrama de clases (UML)

```mermaid
classDiagram
    direction TB

    class JComponent {
        <<Swing>>
    }
    class Serializable {
        <<interface>>
    }
    class ActionListener {
        <<interface>>
        +actionPerformed(ActionEvent e)
    }
    class SimpleBeanInfo {
        <<JavaBeans>>
    }

    class EstadoBoton {
        <<enumeration>>
        NORMAL
        HOVER
        ACTIVO
        DESHABILITADO
    }

    class EstiloBoton {
        <<enumeration>>
        PLANO
        CONTORNO
    }

    class BotonNavegacion {
        +String PROP_TEXTO$
        +String PROP_ICONO$
        +String PROP_ESTILO$
        +String PROP_ACTIVO$
        +String PROP_COLOR_BORDE$
        +String PROP_COLOR_ACTIVO$
        -String texto
        -Icon icono
        -EstiloBoton estilo
        -boolean activo
        -boolean hover
        -Color colorBorde
        -Color colorFondoHover
        -Color colorFondoActivo
        -Color colorTexto
        -int radioEsquina
        -PropertyChangeSupport propertySupport
        -List~ActionListener~ escuchadores
        +BotonNavegacion()
        +BotonNavegacion(String texto, Icon icono)
        +getTexto() String
        +setTexto(String texto) void
        +getIcono() Icon
        +setIcono(Icon icono) void
        +getEstilo() EstiloBoton
        +setEstilo(EstiloBoton estilo) void
        +isActivo() boolean
        +setActivo(boolean activo) void
        +getColorBorde() Color
        +setColorBorde(Color c) void
        +getColorFondoActivo() Color
        +setColorFondoActivo(Color c) void
        +getRadioEsquina() int
        +setRadioEsquina(int r) void
        +getEstadoActual() EstadoBoton
        +doClick() void
        +addActionListener(ActionListener l) void
        +removeActionListener(ActionListener l) void
        +addPropertyChangeListener(PropertyChangeListener l) void
        +removePropertyChangeListener(PropertyChangeListener l) void
        #paintComponent(Graphics g) void
        +getPreferredSize() Dimension
        +setEnabled(boolean b) void
        -calcularEstado() EstadoBoton
        -fireActionPerformed() void
        -actualizarVista() void
    }

    class ManejadorRaton {
        <<clase interna>>
        +mouseEntered(MouseEvent e) void
        +mouseExited(MouseEvent e) void
        +mouseClicked(MouseEvent e) void
    }

    class BotonNavegacionBeanInfo {
        +getBeanDescriptor() BeanDescriptor
        +getPropertyDescriptors() PropertyDescriptor[]
        +getIcon(int kind) Image
    }

    class GrupoBotonesNavegacion {
        -List~BotonNavegacion~ botones
        -BotonNavegacion seleccionado
        +agregar(BotonNavegacion b) void
        +quitar(BotonNavegacion b) void
        +getSeleccionado() BotonNavegacion
    }

    class VentanaPrincipal {
        <<app: Sprints 1 y 2>>
        -BotonNavegacion btnInicio
        -BotonNavegacion btnSalir
        -GrupoBotonesNavegacion grupoMenu
    }

    class ControladorNavegacion {
        <<app: Sprints 1 y 2>>
        +actionPerformed(ActionEvent e) void
    }

    class BotonNavegacionTest {
        <<JUnit 5>>
        +testValoresPorDefecto() void
        +testSetTextoNulo() void
        +testEstadoDeshabilitado() void
        +testClickDisparaEvento() void
        +testPropiedadLigadaActivo() void
    }

    JComponent <|-- BotonNavegacion
    Serializable <|.. BotonNavegacion
    SimpleBeanInfo <|-- BotonNavegacionBeanInfo
    ActionListener <|.. ControladorNavegacion

    BotonNavegacion *-- ManejadorRaton : contiene
    BotonNavegacion --> EstadoBoton : calcula
    BotonNavegacion --> EstiloBoton : usa
    BotonNavegacion ..> ActionListener : notifica
    BotonNavegacionBeanInfo ..> BotonNavegacion : describe
    GrupoBotonesNavegacion o-- BotonNavegacion : agrupa 1..*
    VentanaPrincipal *-- BotonNavegacion : instancia
    VentanaPrincipal --> GrupoBotonesNavegacion
    VentanaPrincipal --> ControladorNavegacion
    ControladorNavegacion ..> BotonNavegacion : escucha
    BotonNavegacionTest ..> BotonNavegacion : prueba
```
