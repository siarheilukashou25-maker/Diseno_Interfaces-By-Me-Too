# DI-3.2 - Eventos personalizados y ayuda contextual

## 1. Objetivo

Documentar la API de eventos propia de los botones Swing y proporcionar una
ayuda contextual que responda a F1 cuando el componente correspondiente tenga
el foco.

## 2. Suscripción y baja

La clase `BotonBymetoo` expone:

- `addBotonBymetooListener(BotonBymetooListener listener)`.
- `removeBotonBymetooListener(BotonBymetooListener listener)`.
- `getBotonBymetooListeners()`.

El listener se registra en la lista de listeners de Swing. Se recomienda
conservar la referencia del listener cuando vaya a ser necesario eliminarlo,
por ejemplo al cerrar una vista.

## 3. Tipos de evento

`BotonBymetooListener` declara dos callbacks:

- `estadoCambiado(BotonBymetooEvent e)`: cambio de estado del botón.
- `botonPulsado(BotonBymetooEvent e)`: pulsación previa a la navegación.

`BotonBymetooEvent` incluye el origen, tipo, estados anterior y nuevo, destino
de navegación y un indicador para cancelar la navegación. Para cancelar, se
llama a `cancelarNavegacion()` durante el callback `botonPulsado`.

`BotonBymetooAdapter` ofrece implementaciones vacías de ambos callbacks y
permite sobrescribir únicamente el método necesario.

## 4. Ayuda contextual

Se incorpora `AyudaContextual`, una clase de utilidad independiente de los
ocho botones concretos. Su método estático:

```java
AyudaContextual.instalar(JComponent componente, String titulo, String mensaje);
```

registra F1 en el `InputMap` del componente con la condición
`JComponent.WHEN_FOCUSED` y añade la acción correspondiente al `ActionMap`.
Al ejecutarse, presenta el texto mediante `JOptionPane`.

## 5. Comprobación manual

1. Añadir la dependencia de la biblioteca al proyecto Swing.
2. Crear un botón y llamar a `AyudaContextual.instalar(...)`.
3. Ejecutar el formulario y dar el foco al botón.
4. Pulsar F1 y comprobar que aparece el cuadro de ayuda.
5. Dar el foco a otro control y pulsar F1: la ayuda del botón no debe aparecer.
6. Registrar un `BotonBymetooListener`, provocar un cambio de estado y pulsar el
   botón; comprobar que se reciben los callbacks correspondientes.
7. Probar `cancelarNavegacion()` y verificar que el botón no abre su ventana
   de destino.

La comprobación de interfaz debe realizarse en un entorno gráfico, no en modo
headless.
