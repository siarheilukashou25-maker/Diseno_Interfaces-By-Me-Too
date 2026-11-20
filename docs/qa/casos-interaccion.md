# DI-3.2.3 · Casos de prueba de interacción

Tarea padre: **DI-3.2** · Componentes: `MenuButton` y `GroupButton` · Relacionado: [`plan-pruebas.md`](plan-pruebas.md)

> ⚠️ **Dependencia pendiente: DI-3.2.2 (Roberto).**
> Los nombres de los eventos todavía no están definidos. En este documento se usan **marcadores** con el formato `EV_*` (por ejemplo, `EV_CLIC`).
> Cuando DI-3.2.2 esté terminada:
> 1. Rellenar la columna «Nombre real (DI-3.2.2)» de la tabla 1.
> 2. Sustituir cada marcador en todo el archivo (buscar `EV_` y reemplazar).
> 3. Revisar los casos marcados con 🔶, que dependen de decisiones de DI-3.2.2.
> 4. Cambiar el estado de este documento a «Validado».

**Estado:** Borrador · pendiente de DI-3.2.2

## 1. Tabla de correspondencia de eventos

Única tabla que hay que editar cuando lleguen los datos de Roberto. La columna «Propuesta» es la opción estándar de Swing, por si se decide reutilizarla.

| Marcador | Significado | Propuesta (Swing estándar) | Nombre real (DI-3.2.2) |
|---|---|---|---|
| `EV_CLIC` | Acción del botón tras un clic válido | `ActionEvent` → `actionPerformed` | _pendiente_ |
| `EV_HOVER_ENTRA` | El puntero entra en el botón | `MouseEvent.MOUSE_ENTERED` → `mouseEntered` | _pendiente_ |
| `EV_HOVER_SALE` | El puntero sale del botón | `MouseEvent.MOUSE_EXITED` → `mouseExited` | _pendiente_ |
| `EV_CAMBIO_ESTADO` | Cambio de estado visual (normal/hover/activo/deshabilitado) | `ChangeEvent` → `stateChanged` | _pendiente_ |
| `EV_SELECCION` | El botón pasa a activo o deja de estarlo | `ItemEvent` → `itemStateChanged` | _pendiente_ |
| `EV_PROPIEDAD` | Cambio de una propiedad ligada | `PropertyChangeEvent` → `propertyChange` | _pendiente_ |
| `EV_NINGUNO` | No debe dispararse ningún evento | — | — |

Si DI-3.2.2 añade eventos nuevos, se agregan como filas `EV_*` en esta tabla y después como casos en la sección 3.

## 2. Precondiciones comunes

| ID | Precondición |
|---|---|
| PRE-1 | Botón creado en el EDT, visible dentro de un `JFrame` de prueba, habilitado y no seleccionado (estado Normal) |
| PRE-2 | Hay un listener registrado para cada evento `EV_*` que cuenta las llamadas y guarda el último evento |
| PRE-3 | Botón deshabilitado con `setEnabled(false)` |
| PRE-4 | Botón activo con `setSelected(true)` |
| PRE-5 | Varios `GroupButton` en un mismo grupo de selección única (por ejemplo, «Gastos» y «Repartos») |

Criterio de aceptación (igual que en `plan-pruebas.md`): el resultado coincide con el esperado y no se lanza ninguna excepción.

## 3. Casos de prueba

Columnas: **ID** · **Acción** · **Precondición** · **Resultado esperado** · **Evento esperado**.
«×N» indica cuántas veces debe recibirse el evento.

### 3.1 Clic

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-01 | Un clic izquierdo sobre el botón | PRE-1, PRE-2 | Se ejecuta la acción; el botón vuelve a su estado visual anterior al soltar | `EV_CLIC` ×1 |
| CI-02 | Clic sobre `GroupButton` «Gastos» | PRE-1, PRE-2, PRE-5 | «Gastos» pasa a Activo (fondo `#81DAAB`) | `EV_CLIC` ×1, `EV_SELECCION` ×1 |
| CI-03 | Pulsar dentro del botón y soltar fuera | PRE-1, PRE-2 | No se ejecuta la acción; vuelve a Normal | `EV_NINGUNO` (sin `EV_CLIC`) |
| CI-04 | Clic con el botón derecho | PRE-1, PRE-2 | No se ejecuta la acción | `EV_NINGUNO` (sin `EV_CLIC`) |
| CI-05 | Tecla Espacio con el foco en el botón | PRE-1, PRE-2 | Mismo efecto que un clic | `EV_CLIC` ×1 |

### 3.2 Doble clic

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-06 | Doble clic sobre el botón | PRE-1, PRE-2 | 🔶 La acción se ejecuta dos veces (comportamiento estándar de Swing) o una sola, según DI-3.2.2 | `EV_CLIC` ×2 (o ×1 si DI-3.2.2 lo decide) |
| CI-07 | Doble clic sobre un `GroupButton` ya activo | PRE-4, PRE-2, PRE-5 | 🔶 Sigue activo; no se deselecciona | `EV_CLIC` ×2, `EV_SELECCION` ×0 |

### 3.3 Serie de clics

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-08 | 10 clics seguidos rápidos | PRE-1, PRE-2 | Se procesan todos; sin bloqueos ni excepciones | `EV_CLIC` ×10 |
| CI-09 | Clic en «Gastos», luego en «Repartos», luego en «Gastos» | PRE-1, PRE-2, PRE-5 | Al final solo «Gastos» está Activo | `EV_CLIC` ×3; `EV_SELECCION` en cada cambio |
| CI-10 | 10 clics y después `removeActionListener` y 1 clic más | PRE-1, PRE-2 | El listener eliminado no recibe el último clic | `EV_CLIC` ×10 |

### 3.4 Hover

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-11 | Mover el puntero sobre `MenuButton` «Inicio» | PRE-1, PRE-2 | Fondo Hover `rgba(26, 32, 44, 0.08)` | `EV_HOVER_ENTRA` ×1, `EV_CAMBIO_ESTADO` ×1 |
| CI-12 | Sacar el puntero de «Inicio» | CI-11 hecho | Vuelve a Normal (fondo transparente) | `EV_HOVER_SALE` ×1, `EV_CAMBIO_ESTADO` ×1 |
| CI-13 | Mover el puntero sobre `GroupButton` «Compartir» | PRE-1, PRE-2 | Fondo sin cambios (`#FAFAFA`) | `EV_HOVER_ENTRA` ×1 |
| CI-14 | Entrar y salir 5 veces seguidas | PRE-1, PRE-2 | Termina en Normal; sin parpadeos ni estados mezclados | `EV_HOVER_ENTRA` ×5, `EV_HOVER_SALE` ×5 |
| CI-15 | Hover sobre un botón Activo | PRE-4, PRE-2 | Se mantiene el aspecto Activo | `EV_HOVER_ENTRA` ×1 |

### 3.5 Clic en estado deshabilitado

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-16 | Un clic sobre el botón | PRE-3, PRE-2 | No se ejecuta la acción; aspecto Deshabilitado sin cambios | `EV_NINGUNO` (sin `EV_CLIC`) |
| CI-17 | Doble clic y serie de 10 clics | PRE-3, PRE-2 | Igual que CI-16 | `EV_NINGUNO` |
| CI-18 | Hover sobre el botón | PRE-3, PRE-2 | No cambia el fondo | 🔶 `EV_CAMBIO_ESTADO` ×0 |
| CI-19 | `doClick()` desde código | PRE-3, PRE-2 | No se ejecuta la acción | `EV_NINGUNO` |
| CI-20 | Tecla Espacio | PRE-3, PRE-2 | El botón no recibe el foco ni reacciona | `EV_NINGUNO` |

### 3.6 Cambio de estado no permitido

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-21 | `setSelected(true)` sobre un botón deshabilitado | PRE-3, PRE-2 | 🔶 Se sigue pintando como Deshabilitado (no verde); sin excepción | 🔶 `EV_SELECCION` según DI-3.2.2; `EV_CAMBIO_ESTADO` ×0 |
| CI-22 | Activar dos `GroupButton` del mismo grupo desde código | PRE-5, PRE-2 | Solo queda activo el último | `EV_SELECCION` ×2 (activa uno y desactiva otro) |
| CI-23 | Poner rollover (`getModel().setRollover(true)`) en un botón deshabilitado | PRE-3, PRE-2 | Se mantiene Deshabilitado | `EV_CAMBIO_ESTADO` ×0 |
| CI-24 | Deshabilitar el botón mientras el puntero está encima | CI-11 hecho, PRE-2 | Pasa a Deshabilitado, sin quedarse en Hover | `EV_CAMBIO_ESTADO` ×1 |
| CI-25 | Volver a asignar el mismo estado (`setSelected(true)` dos veces) | PRE-4, PRE-2 | Sin cambios | `EV_SELECCION` ×0, `EV_PROPIEDAD` ×0 |
| CI-26 | Asignar un valor inválido de propiedad (`arc = -1`) | PRE-1, PRE-2 | Se ajusta a `0`; sin excepción | `EV_PROPIEDAD` ×1 (valor nuevo `0`) |

## 4. Pendiente de DI-3.2.2

| Punto | Casos afectados |
|---|---|
| Nombres reales de todos los eventos `EV_*` | Todos |
| ¿El doble clic dispara una o dos acciones? | CI-06, CI-07 |
| ¿Se emite `EV_CAMBIO_ESTADO` con hover en deshabilitado? | CI-18 |
| ¿Se permite seleccionar un botón deshabilitado? | CI-21 |
| ¿Hay eventos propios del componente además de los de Swing? | Añadir filas en la tabla 1 y casos en la sección 3 |

## 5. Registro de ejecución

| Fecha | Commit | ID del caso | Resultado (pasado/fallado) | Observaciones |
|---|---|---|---|---|
| | | | | |
