# Manual técnico de BymetooBotones

## Índice

1. [Introducción](#1-introducción)
2. [Arquitectura general](#2-arquitectura-general)
3. [Componentes disponibles](#3-componentes-disponibles)
4. [Clases principales](#4-clases-principales)
5. [Estados visuales](#5-estados-visuales)
6. [Estilos e iconos](#6-estilos-e-iconos)
7. [Propiedades configurables](#7-propiedades-configurables)
8. [Sistema de eventos personalizados](#8-sistema-de-eventos-personalizados)
9. [Información del evento y cancelación de navegación](#9-información-del-evento-y-cancelación-de-navegación)
10. [Eventos PropertyChange](#10-eventos-propertychange)
11. [Navegación entre ventanas](#11-navegación-entre-ventanas)
12. [Ayuda contextual](#12-ayuda-contextual)
13. [Integración en otros proyectos](#13-integración-en-otros-proyectos)
14. [Compilación y generación del JAR](#14-compilación-y-generación-del-jar)
15. [Documentación Javadoc](#15-documentación-javadoc)
16. [Distribución y versionado](#16-distribución-y-versionado)
17. [Mantenimiento y resolución de problemas](#17-mantenimiento-y-resolución-de-problemas)

---

## 1. Introducción

**BymetooBotones** es una biblioteca de componentes gráficos reutilizables desarrollada en Java Swing. Su objetivo es proporcionar una colección de botones personalizados que mantengan una apariencia visual coherente y permitan reutilizar funcionalidades comunes en diferentes aplicaciones.

La biblioteca forma parte del proyecto ByMeToo, aunque está diseñada para poder utilizarse independientemente en otros proyectos Java Swing.

Es importante diferenciar ambos elementos:

- **BymetooBotones:** biblioteca que contiene los componentes gráficos reutilizables.
- **ByMeToo:** aplicación que utiliza dichos componentes.

Por tanto, el archivo distribuible de la biblioteca contiene exclusivamente los componentes gráficos, sin incluir la aplicación completa.

### 1.1. Tecnologías utilizadas

| Tecnología | Utilidad |
|---|---|
| Java | Lenguaje de programación utilizado para desarrollar la biblioteca. |
| Java Swing | Framework utilizado para crear los componentes gráficos. |
| Apache Ant | Herramienta de construcción y generación del proyecto. |
| Apache NetBeans | Entorno de desarrollo utilizado. |
| JUnit | Framework utilizado para las pruebas automatizadas. |
| Javadoc | Herramienta utilizada para generar la documentación de la API. |

### 1.2. Paquete principal

Las clases principales de la biblioteca se encuentran en:

`com.bymetoo.componentes`

Este paquete agrupa los componentes, las clases de eventos, las enumeraciones y las utilidades necesarias para su funcionamiento.

---

## 2. Arquitectura general

La arquitectura de BymetooBotones se basa en una clase común denominada `BotonBymetoo`, que centraliza el comportamiento compartido por los distintos botones personalizados.

Los ocho componentes concretos heredan de esta clase base, evitando duplicar la implementación de funcionalidades comunes.

La clase `BotonBymetoo` es abstracta y sellada (`sealed`), por lo que establece una jerarquía controlada de componentes.

### 2.1. Organización de la arquitectura

La biblioteca se organiza en diferentes responsabilidades:

| Elemento | Responsabilidad |
|---|---|
| `BotonBymetoo` | Centraliza el comportamiento común de los botones. |
| Botones concretos | Proporcionan las distintas opciones de navegación de la aplicación. |
| `BotonBymetooEvent` | Transporta información sobre los eventos generados. |
| `BotonBymetooListener` | Define los eventos personalizados que pueden recibir los desarrolladores. |
| `BotonBymetooAdapter` | Facilita la implementación de listeners. |
| Enumeraciones | Definen los estados, estilos e iconos disponibles. |
| BeanInfo | Proporciona información de las propiedades y eventos del componente al entorno de desarrollo. |
| `AyudaContextual` | Proporciona ayuda contextual mediante la tecla F1. |

Esta organización permite separar la apariencia, el comportamiento, la comunicación mediante eventos y la configuración de los componentes.

### 2.2. Ventajas de la arquitectura

La utilización de una clase base común proporciona varias ventajas:

- Evita repetir código entre los distintos botones.
- Facilita el mantenimiento de los componentes.
- Permite centralizar las modificaciones de comportamiento.
- Mantiene una apariencia coherente.
- Facilita la incorporación de nuevos componentes que compartan el comportamiento común.

---

## 3. Componentes disponibles

La biblioteca proporciona ocho botones personalizados.

| Componente | Funcionalidad |
|---|---|
| `BotonInicio` | Permite acceder a la sección inicial de la aplicación. |
| `BotonPerfil` | Permite acceder a la sección del perfil del usuario. |
| `BotonCompartir` | Proporciona acceso a las funciones de compartir. |
| `BotonGastos` | Permite acceder a la sección de gestión de gastos. |
| `BotonRepartos` | Proporciona acceso a la sección de repartos. |
| `BotonNuevaCompra` | Permite acceder a la funcionalidad de creación de una nueva compra. |
| `BotonResumen` | Permite acceder a la sección de resumen de información. |
| `BotonSalir` | Proporciona acceso a la funcionalidad de salida de la aplicación. |

Cada botón dispone de una configuración visual asociada y puede utilizarse de forma independiente dentro de una interfaz Swing.

### 3.1. Ejemplo de configuración de BotonInicio

El componente `BotonInicio` dispone de los siguientes valores predeterminados conocidos:

| Característica | Valor |
|---|---|
| Texto inicial | `Inicio` |
| Tipo de icono | `TipoIcono.USUARIOS` |
| Estilo | `EstiloBoton.MENU` |
| Ventana de destino | `vista.MainView` |

Estos valores permiten que el botón tenga una configuración inicial sin necesidad de establecer todas sus propiedades manualmente.

---

## 4. Clases principales

### 4.1. BotonBymetoo

Es la clase base de los botones personalizados.

Centraliza las propiedades y el comportamiento común de los componentes, incluyendo la gestión de estados visuales, los eventos personalizados y la configuración de navegación.

Entre sus métodos relacionados con los eventos se encuentran:

| Método | Función |
|---|---|
| `addBotonBymetooListener` | Registra un listener para recibir eventos del botón. |
| `removeBotonBymetooListener` | Elimina un listener registrado. |
| `getBotonBymetooListeners` | Permite consultar los listeners registrados. |
| `fireEstadoCambiado` | Notifica el evento de cambio de estado. |
| `fireBotonPulsado` | Notifica el evento de pulsación. |

### 4.2. BotonBymetooEvent

Representa un evento generado por uno de los botones de la biblioteca.

Transporta información que permite al desarrollador conocer qué componente ha generado el evento y qué acción o cambio se ha producido.

Contiene información relacionada con:

- El botón que origina el evento.
- El tipo de evento.
- El estado anterior.
- El estado nuevo.
- La ventana de destino.

También incorpora mecanismos para controlar la navegación:

- `cancelarNavegacion()`
- `isNavegacionCancelada()`

### 4.3. BotonBymetooListener

Es la interfaz que define los eventos personalizados de la biblioteca.

Dispone de dos métodos:

```java
void estadoCambiado(BotonBymetooEvent e);

void botonPulsado(BotonBymetooEvent e);
```

El primero permite reaccionar a los cambios de estado del botón, mientras que el segundo permite ejecutar acciones cuando el usuario pulsa el componente.

### 4.4. BotonBymetooAdapter

Es una clase adaptadora que implementa `BotonBymetooListener`.

Su finalidad es facilitar la creación de listeners cuando solamente se necesita reaccionar a uno de los eventos.

En lugar de implementar directamente todos los métodos de la interfaz, el desarrollador puede extender esta clase y sobrescribir únicamente los métodos que necesite.

### 4.5. EstadoBoton

Enumeración que define los estados visuales disponibles para los componentes:

- `NORMAL`
- `HOVER`
- `ACTIVO`
- `DESHABILITADO`

### 4.6. EstiloBoton

Enumeración que identifica los estilos visuales disponibles:

- `MENU`
- `CONTORNO_SUAVE`
- `CONTORNO`

### 4.7. TipoIcono

Enumeración que identifica los distintos iconos disponibles en la biblioteca:

- `NINGUNO`
- `USUARIOS`
- `PERFIL`
- `COMPARTIR`
- `GASTOS`
- `REPARTOS`
- `NUEVA_COMPRA`
- `RESUMEN`
- `SALIR`

### 4.8. IconoNavegacion

Clase relacionada con los iconos utilizados por los componentes para representar visualmente las distintas opciones de navegación.

### 4.9. BeanInfoBotonBase

Clase encargada de proporcionar información sobre las propiedades y eventos del componente mediante el mecanismo de introspección de JavaBeans.

Permite que entornos de desarrollo como NetBeans puedan reconocer las propiedades configurables y los eventos personalizados.

Define un conjunto de eventos denominado `botonBymetoo`, que incluye:

- `estadoCambiado`
- `botonPulsado`

También contempla el mecanismo estándar `propertyChange`.

### 4.10. AyudaContextual

Clase de utilidad independiente que permite instalar ayuda contextual en componentes Swing.

Su funcionamiento se basa en asociar un título y un mensaje a un componente para mostrar una ventana de ayuda cuando el usuario pulsa F1 mientras dicho componente tiene el foco.

Su método principal es:

```java
AyudaContextual.instalar(
    JComponent componente,
    String titulo,
    String mensaje
);
```

---

## 5. Estados visuales

Los componentes utilizan la enumeración `EstadoBoton` para representar las distintas situaciones visuales en las que puede encontrarse un botón.

| Estado | Descripción |
|---|---|
| `NORMAL` | Estado habitual del botón cuando no está siendo interactuado. |
| `HOVER` | Estado visual que representa que el puntero del ratón se encuentra sobre el botón. |
| `ACTIVO` | Estado que representa que el botón se encuentra activo. |
| `DESHABILITADO` | Estado en el que el botón no está habilitado para la interacción habitual. |

La utilización de estados centralizados permite mantener una representación visual coherente en todos los componentes.

Además, los cambios de estado pueden comunicarse mediante eventos, permitiendo que otros elementos de la aplicación reaccionen ante ellos.

### 5.1. Notificación de cambios de estado

El cambio de la propiedad `estado` también puede comunicarse mediante el mecanismo `PropertyChangeListener` de Swing.

El nombre de la propiedad utilizado es:

```java
BotonBymetoo.PROP_ESTADO
```

Su valor es:

```java
"estado"
```

Esto permite que otros componentes puedan observar los cambios de estado mediante el sistema estándar de notificación de propiedades de Java.

---

## 6. Estilos e iconos

### 6.1. Estilos disponibles

La enumeración `EstiloBoton` proporciona tres estilos visuales.

| Estilo | Descripción | Botones asociados |
|---|---|---|
| `MENU` | Estilo utilizado para los botones de menú. | Inicio y Perfil. |
| `CONTORNO_SUAVE` | Estilo con apariencia de contorno suave. | Compartir y Gastos. |
| `CONTORNO` | Estilo de contorno. | Repartos, Nueva compra, Resumen y Salir. |

La separación entre estilo y comportamiento permite modificar la presentación visual sin tener que duplicar la lógica de los componentes.

### 6.2. Iconos disponibles

La enumeración `TipoIcono` permite identificar los iconos que pueden asociarse a los botones.

| Valor | Utilización |
|---|---|
| `NINGUNO` | Botón sin icono asociado. |
| `USUARIOS` | Icono relacionado con usuarios. |
| `PERFIL` | Icono relacionado con el perfil. |
| `COMPARTIR` | Icono relacionado con compartir. |
| `GASTOS` | Icono relacionado con los gastos. |
| `REPARTOS` | Icono relacionado con los repartos. |
| `NUEVA_COMPRA` | Icono relacionado con nuevas compras. |
| `RESUMEN` | Icono relacionado con los resúmenes. |
| `SALIR` | Icono relacionado con la salida. |

Los iconos permiten identificar visualmente la función de cada botón y mejorar la comprensión de la interfaz.

---

## 7. Propiedades configurables

Los componentes exponen diferentes propiedades mediante el sistema BeanInfo.

Estas propiedades permiten modificar aspectos visuales y funcionales sin necesidad de alterar la implementación interna de los botones.

### 7.1. Propiedades propias

| Propiedad | Función | Modificable |
|---|---|---|
| `texto` | Texto mostrado en el botón. | Sí |
| `ventanaDestino` | Identifica la ventana de destino de la navegación. | Sí |
| `navegacionAutomatica` | Activa o desactiva la navegación automática. | Sí |
| `cerrarVentanaActual` | Configura el cierre de la ventana actual durante la navegación. | Sí |
| `activo` | Permite configurar si el botón se encuentra activo. | Sí |
| `iconoPersonalizado` | Permite establecer un icono personalizado. | Sí |
| `colorFondo` | Color de fondo del botón. | Sí |
| `colorHover` | Color asociado al estado hover. | Sí |
| `colorActivo` | Color asociado al estado activo. | Sí |
| `colorBorde` | Color del borde del botón. | Sí |
| `colorBordeHover` | Color del borde durante el estado hover. | Sí |
| `colorTexto` | Color del texto. | Sí |
| `colorTextoDeshabilitado` | Color del texto cuando el botón está deshabilitado. | Sí |
| `radioBorde` | Radio utilizado para redondear los bordes. | Sí |
| `separacionIcono` | Separación entre el icono y el texto. | Sí |
| `estado` | Estado actual del botón. | No |
| `tipoIcono` | Tipo de icono asociado. | No |
| `estilo` | Estilo visual asociado. | No |

Las propiedades indicadas como de solo lectura permiten consultar la configuración actual del componente, pero no modificarla directamente mediante el BeanInfo.

### 7.2. Propiedades heredadas de Swing

También se exponen propiedades heredadas del sistema de componentes Swing:

| Propiedad | Función |
|---|---|
| `enabled` | Indica si el componente está habilitado. |
| `font` | Permite configurar la fuente utilizada. |
| `toolTipText` | Define el texto de ayuda que aparece al situar el puntero sobre el componente. |
| `preferredSize` | Permite establecer el tamaño preferido del componente. |

La exposición de estas propiedades facilita la integración de los botones en el editor visual de NetBeans.

---

## 8. Sistema de eventos personalizados

BymetooBotones incorpora un sistema propio de eventos que permite a las aplicaciones consumidoras reaccionar ante las acciones realizadas sobre los botones.

Se definen dos eventos principales:

### 8.1. Evento estadoCambiado

Se produce cuando cambia el estado del botón.

El método asociado es:

```java
estadoCambiado(BotonBymetooEvent e)
```

El objeto recibido contiene información sobre el cambio, incluyendo el estado anterior y el nuevo.

### 8.2. Evento botonPulsado

Se produce cuando el usuario pulsa el botón.

El método asociado es:

```java
botonPulsado(BotonBymetooEvent e)
```

Permite ejecutar acciones específicas antes de que se realice la navegación automática, si esta está habilitada.

### 8.3. Registro de listeners

Para recibir eventos de un botón es necesario registrar un listener mediante:

```java
addBotonBymetooListener
```

Para eliminar un listener se utiliza:

```java
removeBotonBymetooListener
```

También es posible consultar los listeners registrados mediante:

```java
getBotonBymetooListeners
```

### 8.4. Ejemplo de utilización

El siguiente ejemplo muestra cómo registrar un listener utilizando `BotonBymetooAdapter`:

```java
BotonRepartos boton = new BotonRepartos();

boton.addBotonBymetooListener(new BotonBymetooAdapter() {

    @Override
    public void botonPulsado(BotonBymetooEvent e) {
        System.out.println("Se ha pulsado el botón de repartos");
    }

    @Override
    public void estadoCambiado(BotonBymetooEvent e) {
        System.out.println("El estado del botón ha cambiado");
    }
});
```

En este ejemplo se sobrescriben los dos métodos del listener para reaccionar tanto a la pulsación como al cambio de estado.

La utilización de la clase adaptadora permite implementar solamente los métodos que sean necesarios para cada situación.

---

## 9. Información del evento y cancelación de navegación

El objeto `BotonBymetooEvent` contiene información relacionada con el evento que se ha producido.

Entre los datos disponibles se encuentran:

- Botón que origina el evento.
- Tipo de evento.
- Estado anterior.
- Estado nuevo.
- Ventana de destino.

Además, incorpora dos métodos relacionados con el control de la navegación:

| Método | Función |
|---|---|
| `cancelarNavegacion()` | Indica que debe cancelarse la navegación automática. |
| `isNavegacionCancelada()` | Permite consultar si se ha cancelado la navegación. |

### 9.1. Funcionamiento

Cuando el usuario pulsa un botón y se genera el evento `botonPulsado`, se notifica a los listeners registrados.

Durante esta notificación, los listeners pueden ejecutar comprobaciones y decidir si permiten continuar con la navegación.

Si alguno de ellos considera que no debe abandonarse la pantalla actual, puede llamar a `cancelarNavegacion()`.

Una vez notificados los listeners, la navegación automática solamente se produce si no ha sido cancelada y está habilitada.

### 9.2. Ejemplo: confirmación antes de navegar

El siguiente ejemplo muestra cómo solicitar confirmación al usuario antes de continuar:

```java
boton.addBotonBymetooListener(new BotonBymetooAdapter() {

    @Override
    public void botonPulsado(BotonBymetooEvent e) {

        int respuesta = JOptionPane.showConfirmDialog(
            null,
            "¿Deseas continuar?",
            "Confirmar navegación",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            e.cancelarNavegacion();
        }
    }
});
```

En este caso, si el usuario responde negativamente, se cancela la navegación automática.

Este mecanismo permite incorporar validaciones y confirmaciones antes de cambiar de ventana.

---

## 10. Eventos PropertyChange

Además del sistema de eventos personalizado, los componentes utilizan el mecanismo estándar `PropertyChangeListener` de Swing.

Este mecanismo permite notificar cambios en las propiedades de un componente.

En BymetooBotones, el cambio de la propiedad `estado` utiliza el nombre:

```java
BotonBymetoo.PROP_ESTADO
```

Cuyo valor es:

```java
"estado"
```

Existen dos mecanismos de notificación complementarios:

| Mecanismo | Finalidad |
|---|---|
| `BotonBymetooListener` | Comunica acciones específicas del botón, como su pulsación o un cambio de estado. |
| `PropertyChangeListener` | Comunica cambios en propiedades del componente mediante el mecanismo estándar de JavaBeans y Swing. |

Esto permite que los desarrolladores utilicen el sistema que mejor se adapte a sus necesidades.

---

## 11. Navegación entre ventanas

Los botones pueden configurarse para facilitar la navegación entre las distintas ventanas de una aplicación.

Para ello se dispone de propiedades que permiten configurar el destino y el comportamiento de navegación.

### 11.1. Ventana de destino

La propiedad `ventanaDestino` identifica la ventana a la que debe dirigirse el botón.

Como ejemplo, `BotonInicio` tiene configurado inicialmente:

```java
"vista.MainView"
```

La aplicación consumidora debe proporcionar los destinos que correspondan a su propia estructura.

### 11.2. Navegación automática

La propiedad `navegacionAutomatica` permite activar o desactivar la navegación automática.

Cuando está desactivada, el desarrollador puede gestionar manualmente las acciones mediante los listeners.

Ejemplo de configuración:

```java
boton.setNavegacionAutomatica(false);
```

### 11.3. Cierre de la ventana actual

La propiedad `cerrarVentanaActual` permite configurar si debe cerrarse la ventana actual durante la navegación.

### 11.4. Control mediante eventos

La navegación automática puede combinarse con el sistema de eventos personalizados.

De esta forma, es posible ejecutar comprobaciones, mostrar confirmaciones o cancelar el cambio de ventana antes de continuar.

---

## 12. Ayuda contextual

La biblioteca incluye la clase independiente `AyudaContextual`, diseñada para proporcionar información contextual mediante la tecla F1.

Esta utilidad permite asociar un título y un mensaje a cualquier componente Swing compatible con `JComponent`.

### 12.1. Método instalar

El método disponible es:

```java
AyudaContextual.instalar(
    JComponent componente,
    String titulo,
    String mensaje
);
```

Sus parámetros son:

| Parámetro | Descripción |
|---|---|
| `componente` | Componente Swing al que se asocia la ayuda. |
| `titulo` | Título de la ventana de ayuda. |
| `mensaje` | Texto explicativo que se mostrará al usuario. |

### 12.2. Ejemplo de utilización

```java
JButton boton = new JButton("Guardar");

AyudaContextual.instalar(
    boton,
    "Ayuda de guardar",
    "Pulsa este botón para guardar los cambios."
);
```

Al situar el foco en el botón y pulsar F1, se muestra un cuadro de diálogo con la información configurada.

### 12.3. Funcionamiento interno

La utilidad utiliza mecanismos estándar de Swing:

- `InputMap`: permite asociar la pulsación de una tecla con una acción.
- `ActionMap`: permite definir la acción que debe ejecutarse.
- `KeyStroke`: permite identificar la combinación de teclas correspondiente a F1.

La ayuda contextual es independiente de `BotonBymetoo`, por lo que puede utilizarse también con otros componentes Swing.

No es obligatorio instalar ayuda en todos los botones de la biblioteca. Cada aplicación consumidora puede decidir qué componentes necesitan esta funcionalidad.

---

## 13. Integración en otros proyectos

La biblioteca puede incorporarse a cualquier proyecto compatible con Java Swing mediante el archivo JAR generado durante la compilación.

### 13.1. Obtener el archivo JAR

El archivo distribuible es:

`BymetooBotones.jar`

Su ubicación dentro del proyecto es:

```text
componentes/dist/BymetooBotones.jar
```

También puede obtenerse desde la publicación correspondiente a la versión `v1.0` en GitHub Releases.

### 13.2. Añadir la biblioteca a NetBeans

Para utilizar los componentes en otro proyecto:

1. Abrir el proyecto consumidor en NetBeans.
2. Acceder a las propiedades del proyecto.
3. Seleccionar el apartado **Libraries** o **Bibliotecas**.
4. Añadir el archivo `BymetooBotones.jar`.
5. Aplicar los cambios.

Una vez añadido, las clases de la biblioteca estarán disponibles para el proyecto.

### 13.3. Importar las clases

Para utilizar los botones es necesario importar las clases correspondientes.

Ejemplo:

```java
import com.bymetoo.componentes.BotonInicio;
import com.bymetoo.componentes.BotonRepartos;
import com.bymetoo.componentes.BotonBymetooAdapter;
import com.bymetoo.componentes.BotonBymetooEvent;
```

Se deben importar únicamente las clases necesarias en cada caso.

### 13.4. Crear un componente

Una vez añadida la biblioteca, se puede crear una instancia de cualquiera de los botones disponibles.

Ejemplo:

```java
BotonInicio botonInicio = new BotonInicio();
```

El componente puede incorporarse a un contenedor Swing como cualquier otro componente gráfico.

```java
JPanel panel = new JPanel();

BotonInicio botonInicio = new BotonInicio();

panel.add(botonInicio);
```

### 13.5. Incorporar los botones a la paleta de NetBeans

También es posible añadir los componentes a la paleta de NetBeans para utilizarlos desde el editor visual.

El procedimiento general consiste en:

1. Abrir la ventana de paleta de componentes.
2. Seleccionar la opción para añadir componentes desde un archivo JAR.
3. Seleccionar `BymetooBotones.jar`.
4. Elegir los componentes que se desean incorporar.
5. Finalizar el asistente.

De esta forma, los botones estarán disponibles en la paleta y podrán utilizarse durante el diseño visual de interfaces.

---

## 14. Compilación y generación del JAR

El proyecto utiliza Apache Ant como sistema de construcción y Apache NetBeans como entorno de desarrollo.

La generación de la biblioteca se realiza mediante la opción **Clean and Build**.

### 14.1. Procedimiento en NetBeans

1. Abrir el proyecto `componentes`.
2. Hacer clic derecho sobre el proyecto.
3. Seleccionar **Clean and Build**.
4. Esperar a que finalice el proceso.
5. Comprobar el resultado de la compilación.

Esta operación limpia los resultados de compilaciones anteriores y ejecuta el proceso de construcción.

### 14.2. Archivo generado

El archivo JAR resultante se encuentra en:

```text
componentes/dist/BymetooBotones.jar
```

Este archivo contiene la biblioteca compilada y constituye el elemento principal para su integración en otros proyectos.

### 14.3. Pruebas automatizadas

El proyecto incorpora pruebas automatizadas mediante JUnit.

Estas pruebas permiten comprobar el funcionamiento de los componentes y detectar errores durante el desarrollo.

Es recomendable ejecutarlas después de realizar modificaciones en la biblioteca y comprobar que finalizan correctamente antes de distribuir una nueva versión.

---

## 15. Documentación Javadoc

El proyecto incorpora documentación Javadoc para facilitar la consulta de la API de la biblioteca.

Javadoc permite generar documentación HTML a partir de los comentarios de documentación incluidos en el código fuente.

### 15.1. Contenido de la documentación

La documentación permite consultar:

- Clases disponibles.
- Descripción de cada clase.
- Métodos documentados.
- Parámetros.
- Valores devueltos.
- Relaciones entre los distintos elementos de la API.

### 15.2. Ubicación

La documentación generada se encuentra en:

```text
componentes/dist/javadoc/index.html
```

Para consultarla, basta con abrir el archivo `index.html` en un navegador.

Disponer de documentación Javadoc facilita el trabajo de otros desarrolladores que quieran utilizar la biblioteca sin necesidad de estudiar inicialmente toda su implementación interna.

---

## 16. Distribución y versionado

La biblioteca se distribuye mediante GitHub Releases.

Las publicaciones permiten asociar archivos descargables a versiones concretas del proyecto.

### 16.1. Versión v1.0

Se ha publicado la primera versión estable de la biblioteca con los siguientes datos:

| Elemento | Valor |
|---|---|
| Nombre | BymetooBotones |
| Versión | `v1.0` |
| Título | BymetooBotones v1.0 – Primera versión estable |
| Distribución | GitHub Releases |

### 16.2. Archivos distribuidos

La publicación incluye:

- `BymetooBotones.jar`: biblioteca compilada para su integración en proyectos Java Swing.
- Documentación Javadoc comprimida: documentación técnica de las clases y métodos de la API.

### 16.3. Ventajas de utilizar Releases

La distribución mediante Releases permite:

- Mantener un historial de versiones.
- Facilitar la descarga de versiones concretas.
- Identificar qué versión se está utilizando.
- Distribuir los archivos necesarios sin incluir los archivos internos del proyecto de desarrollo.

Los archivos distribuidos corresponden exclusivamente a la biblioteca de componentes gráficos, no a la aplicación completa ByMeToo.

---

## 17. Mantenimiento y resolución de problemas

Durante la utilización y el mantenimiento de la biblioteca pueden aparecer diferentes problemas relacionados con la integración, la configuración o la compilación.

La siguiente tabla recoge algunos de los problemas más habituales y sus posibles soluciones.

| Problema | Posible solución |
|---|---|
| No se reconoce el paquete `com.bymetoo.componentes`. | Comprobar que `BymetooBotones.jar` se ha añadido correctamente a las bibliotecas del proyecto. |
| No se reconoce una clase de botón. | Comprobar los imports y verificar que se está utilizando una versión correcta de la biblioteca. |
| Error de incompatibilidad de Java. | Comprobar que la versión de Java utilizada es compatible con la versión con la que se compiló la biblioteca. |
| No se realiza la navegación. | Revisar la propiedad `ventanaDestino`, comprobar `navegacionAutomatica` y verificar si algún listener ha cancelado la navegación. |
| No se reciben los eventos. | Comprobar que el listener se ha registrado correctamente mediante `addBotonBymetooListener`. |
| La ayuda contextual no aparece. | Comprobar que el componente tiene el foco y que se ha instalado correctamente la ayuda mediante `AyudaContextual.instalar`. |
| No se genera el archivo JAR. | Revisar los errores de compilación y los resultados de las pruebas automatizadas. |

### 17.1. Recomendaciones de mantenimiento

Para mantener la calidad y estabilidad de la biblioteca se recomienda:

1. Mantener actualizada la documentación técnica cuando se modifique la API.
2. Ejecutar las pruebas automatizadas después de realizar cambios.
3. Regenerar la documentación Javadoc cuando se modifiquen las clases o sus métodos.
4. Comprobar la integración en un proyecto consumidor después de introducir cambios importantes.
5. Publicar nuevas versiones cuando se incorporen funcionalidades o correcciones relevantes.
6. Mantener la compatibilidad con los proyectos que utilicen la biblioteca.

---