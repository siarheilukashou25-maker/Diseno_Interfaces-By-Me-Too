# DI-3.2.3 · Casos de prueba de interacción

Tarea padre: **DI-3.2** (#134) · Issue #135 · Componente: `BotonBymetoo` y sus 8 botones · Relacionado: [`plan-pruebas.md`](plan-pruebas.md)

**Versión:** 3.0 (25/11/2026) · **Estado:** Ejecutado: 38/38 casos pasan (sección 5).
La v1 (20/11) usaba marcadores `EV_*` porque DI-3.2.2 no estaba hecha. La v2 (24/11) los cambió por los eventos reales de DI-3.2.2 (#152, Roberto, 22/11). La v3 (25/11) ajusta CI-19 al nuevo diseño del PR #195, cierra los puntos de la sección 4 y añade la trazabilidad y el registro de ejecución.

## 1. Eventos del componente

| Evento | Método del listener | Cuándo se dispara | Datos que trae |
|---|---|---|---|
| `BotonBymetooEvent` tipo `CAMBIO_ESTADO` | `BotonBymetooListener.estadoCambiado(e)` | Cada vez que `getEstado()` cambia entre `NORMAL`, `HOVER`, `ACTIVO` y `DESHABILITADO` | `getEstadoAnterior()`, `getEstadoNuevo()`, `getBoton()`, `getVentanaDestino()` |
| `BotonBymetooEvent` tipo `PULSADO` | `BotonBymetooListener.botonPulsado(e)` | Al pulsar el botón (clic izquierdo, Espacio, Intro o `pulsar()`), antes de cambiar de ventana | `getVentanaDestino()`, `cancelarNavegacion()`, `isNavegacionCancelada()` |
| `PropertyChangeEvent` | `PropertyChangeListener.propertyChange(e)` | Al cambiar una propiedad (`PROP_TEXTO`, `PROP_ACTIVO`, `PROP_ESTADO`, colores…) | Nombre, valor antiguo y valor nuevo |

Abreviaturas en las tablas: **EC** = `estadoCambiado`, **BP** = `botonPulsado`, **PC** = `propertyChange`. «×N» indica cuántas veces debe recibirse. «—» significa que no debe llegar ningún evento.

> El componente no expone `ActionEvent` ni eventos de ratón propios hacia fuera: hover, clic y teclado se notifican solo con `BotonBymetooEvent`.

## 2. Precondiciones comunes

| ID | Precondición |
|---|---|
| PRE-1 | Botón creado en el EDT, dentro de un `JFrame` de prueba visible, habilitado, `activo = false` (estado `NORMAL`) y con `setNavegacionAutomatica(false)` |
| PRE-2 | Listener contador registrado con `addBotonBymetooListener` (y `addPropertyChangeListener` cuando el caso usa PC) |
| PRE-3 | Botón deshabilitado con `setEnabled(false)` |
| PRE-4 | Botón activo con `setActivo(true)` |
| PRE-5 | Navegación real: `setNavegacionAutomatica(true)`, botón dentro de `VentanaPruebaA` y `ventanaDestino` = `VentanaPruebaB` (ventanas falsas de `test/`) |

Criterio de aceptación (igual que en `plan-pruebas.md`): el resultado coincide con el esperado y no se lanza ninguna excepción no prevista.

Herramienta: **AssertJ Swing** (robot real de ratón y teclado). TestFX no sirve porque el componente es Swing, no JavaFX.

## 3. Casos de prueba

Columnas: **ID** · **Acción** · **Precondición** · **Resultado esperado** · **Evento esperado**.

### 3.1 Clic simple

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-01 | Un clic izquierdo sobre el botón | PRE-1, PRE-2 | Se pulsa; al terminar vuelve a `HOVER` (el puntero sigue encima) | EC `NORMAL→HOVER`, EC `HOVER→ACTIVO`, BP ×1, EC `ACTIVO→HOVER` |
| CI-02 | Pulsar dentro del botón y soltar fuera | PRE-1, PRE-2 | No se pulsa | BP — |
| CI-03 | Clic con el botón derecho | PRE-1, PRE-2 | No se pulsa | BP — |
| CI-04 | Clic con el botón central | PRE-1, PRE-2 | No se pulsa | BP — |
| CI-05 | Tecla Espacio con el foco en el botón | PRE-1, PRE-2 | Mismo efecto que un clic | BP ×1 |
| CI-06 | Tecla Intro con el foco en el botón | PRE-1, PRE-2 | Mismo efecto que un clic | BP ×1 |
| CI-07 | Clic con navegación real | PRE-5, PRE-2 | Se abre `VentanaPruebaB` y se cierra `VentanaPruebaA` | BP ×1 |
| CI-08 | Clic con navegación real y un listener que llama a `e.cancelarNavegacion()` | PRE-5, PRE-2 | No cambia de ventana; el botón vuelve a su estado anterior | BP ×1 con `isNavegacionCancelada() = true` |
| CI-09 | Clic: el botón recibe el foco | PRE-1 | `isFocusOwner() = true` tras pulsar con el ratón | — |

### 3.2 Doble clic

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-10 | Doble clic sobre el botón | PRE-1, PRE-2 | 🔶 Se pulsa dos veces (hoy cada `mouseReleased` llama a `pulsar()`). Decidir con Roberto si debe ser una sola | BP ×2 |
| CI-11 | Doble clic con navegación real | PRE-5, PRE-2 | Se abre **una sola** `VentanaPruebaB`. Si se abren dos, es fallo (observación O-3 del plan) | BP ×1 o ×2; ventanas `VentanaPruebaB` abiertas = 1 |
| CI-12 | Doble clic sobre un botón ya activo | PRE-4, PRE-2 | Sigue `ACTIVO` | BP ×2, EC — |

### 3.3 Clics repetidos

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-13 | 10 clics seguidos rápidos | PRE-1, PRE-2 | Se procesan todos; sin bloqueos ni excepciones; termina en `HOVER` | BP ×10 |
| CI-14 | 10 clics, `removeBotonBymetooListener(l)` y 1 clic más | PRE-1, PRE-2 | El listener eliminado no recibe el último | BP ×10 en `l` |
| CI-15 | Clic en «Gastos», luego en «Repartos», luego en «Gastos» (dos botones en el mismo `JFrame`) | PRE-1, PRE-2 en los dos | Cada botón vuelve a su estado tras pulsarse; ninguno se queda `ACTIVO` | BP ×2 en «Gastos», ×1 en «Repartos» |
| CI-16 | 50 pulsaciones de Espacio seguidas | PRE-1, PRE-2 | Sin excepciones; termina en `NORMAL` | BP ×50 |

### 3.4 Hover

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-17 | Mover el puntero sobre `BotonInicio` (`MENU`) | PRE-1, PRE-2 | Estado `HOVER`; se pinta el fondo `colorHover`; cursor de mano | EC `NORMAL→HOVER` ×1 |
| CI-18 | Sacar el puntero | CI-17 hecho | Vuelve a `NORMAL` | EC `HOVER→NORMAL` ×1 |
| CI-19 | Mover el puntero sobre `BotonCompartir` (`CONTORNO_SUAVE`) | PRE-1, PRE-2 | Estado `HOVER`; el fondo pasa a verde `#81DAAB` (corregido en #188 / PR #195) | EC `NORMAL→HOVER` ×1 |
| CI-20 | Entrar y salir 5 veces seguidas | PRE-1, PRE-2 | Termina en `NORMAL`; sin estados mezclados | EC ×10, alternando `NORMAL→HOVER` y `HOVER→NORMAL` |
| CI-21 | Hover sobre un botón activo | PRE-4, PRE-2 | Se mantiene `ACTIVO` | EC — |
| CI-22 | Salir del botón activo | CI-21 hecho | Se mantiene `ACTIVO` | EC — |

### 3.5 Botón deshabilitado

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-23 | Un clic | PRE-3, PRE-2 | No se pulsa; sigue `DESHABILITADO` | — |
| CI-24 | Doble clic y 10 clics | PRE-3, PRE-2 | Igual que CI-23 | — |
| CI-25 | Hover | PRE-3, PRE-2 | Sigue `DESHABILITADO`; cursor por defecto | — |
| CI-26 | `pulsar()` desde código | PRE-3, PRE-2 | No hace nada | — |
| CI-27 | Espacio o Intro | PRE-3, PRE-2 | No reacciona | — |
| CI-28 | Destino que no existe al mostrarse | Botón con `ventanaDestino = "vista.NoExiste"` y navegación activada, añadido a un `JFrame` | Se deshabilita solo; tooltip «Pantalla no disponible: vista.NoExiste» | EC `NORMAL→DESHABILITADO` ×1 |

### 3.6 Cambios de estado no válidos o límite

| ID | Acción | Precondición | Resultado esperado | Evento esperado |
|---|---|---|---|---|
| CI-29 | `setActivo(true)` sobre un botón deshabilitado | PRE-3, PRE-2 | `isActivo() = true`, pero el estado sigue `DESHABILITADO` y no se pinta verde | PC `PROP_ACTIVO` ×1, EC — |
| CI-30 | Habilitar después de CI-29 | CI-29 hecho | Pasa a `ACTIVO` | EC `DESHABILITADO→ACTIVO` ×1 |
| CI-31 | Deshabilitar con el puntero encima | CI-17 hecho, PRE-2 | Pasa a `DESHABILITADO` y no se queda en `HOVER` | EC `HOVER→DESHABILITADO` ×1 |
| CI-32 | Volver a habilitar con el puntero aún encima | CI-31 hecho | Pasa a `NORMAL` (el hover se recupera al mover el ratón otra vez) | EC `DESHABILITADO→NORMAL` ×1 |
| CI-33 | `setActivo(true)` dos veces | PRE-1, PRE-2 | Sin cambios la segunda vez | EC ×1, PC `PROP_ACTIVO` ×1 |
| CI-34 | `setEnabled(false)` dos veces | PRE-1, PRE-2 | Sin cambios la segunda vez | EC ×1 |
| CI-35 | `setRadioBorde(-1)` | PRE-1, PRE-2 | `IllegalArgumentException`; el radio no cambia | PC — |
| CI-36 | `setColorActivo(null)` | PRE-1, PRE-2 | `NullPointerException` con «colorActivo» en el mensaje; el color no cambia | PC — |
| CI-37 | `setTexto(null)` | PRE-1, PRE-2 | El texto pasa a `""`; sin excepción | PC `PROP_TEXTO` ×1 |
| CI-38 | Listener que se da de baja a sí mismo dentro de `botonPulsado` | PRE-1, PRE-2 | Sin `ConcurrentModificationException`; en la siguiente pulsación ya no recibe nada | BP ×1 |

## 4. Puntos que estaban pendientes de DI-3.2.2

| Punto | Casos | Situación a 25/11 |
|---|---|---|
| ¿El doble clic debe pulsar una vez o dos? | CI-10, CI-11, CI-12 | Se mantiene el comportamiento de Swing: **dos pulsaciones**. No es un fallo porque solo se abre **una** ventana destino (CI-11 pasa). Queda anotado como observación O-3. |
| Un listener que lanza una excepción en `botonPulsado` corta el aviso al resto | EV-10 del plan | Sin cambios en el PR #195. Observación para el informe final (#146). |
| Fuera de una ventana (`isDisplayable() = false`) el botón se queda `ACTIVO` tras `pulsar()` | EV-05b del plan | Comportamiento documentado y probado (EV-05b). Dentro de una ventana real vuelve a su estado (CI-15). |

## 5. Trazabilidad y registro de ejecución

Ejecución del **25/11/2026** sobre `main` después del PR #195 (commit `e62ebfb`). Ejecutado en Windows (JDK 26, NetBeans 31) y en Linux (JDK 21, Xvfb), 4 veces seguidas sin fallos aleatorios. Los tests están en `componentes/test/com/bymetoo/componentes/`.

| Caso | Test que lo comprueba | Resultado |
|---|---|---|
| CI-01 | G-02, EV-05 | ✔ |
| CI-02 | G-07 | ✔ |
| CI-03, CI-04 | G-03 | ✔ |
| CI-05, CI-06 | G-04 | ✔ |
| CI-07 | G-09, N-03 | ✔ |
| CI-08 | CI-08 (`BotonBymetooInteraccionTest`), EV-09 | ✔ |
| CI-09 | CI-09 | ✔ |
| CI-10 | G-08 | ✔ (dos pulsaciones, ver sección 4) |
| CI-11 | CI-11 | ✔ (una sola ventana) |
| CI-12 | CI-12 | ✔ |
| CI-13 | CI-13 | ✔ |
| CI-14 | CI-14, EV-08 | ✔ |
| CI-15 | CI-15 | ✔ |
| CI-16 | CI-16 | ✔ |
| CI-17, CI-18 | G-01, E-01/E-02, P-06 | ✔ |
| CI-19 | O-1a, E-01/E-02 | ✔ (antes fallaba: #188) |
| CI-20 | CI-20, EV-03 | ✔ |
| CI-21, CI-22 | CI-21/CI-22, E-09 | ✔ |
| CI-23 | G-06, EV-06 | ✔ |
| CI-24 | CI-24 | ✔ |
| CI-25 | E-11, P-07/P-08 | ✔ |
| CI-26 | EV-06 | ✔ |
| CI-27 | G-06 | ✔ |
| CI-28 | N-06 | ✔ |
| CI-29 | E-07/E-08, P-09 | ✔ |
| CI-30 | E-07/E-08 | ✔ |
| CI-31 | E-10 | ✔ |
| CI-32 | CI-32 | ✔ |
| CI-33 | EV-02 | ✔ |
| CI-34 | CI-34 | ✔ |
| CI-35 | L-09 | ✔ |
| CI-36 | L-12 | ✔ |
| CI-37 | L-03, EV-11 | ✔ |
| CI-38 | CI-38 (`BotonBymetooEventosTest`) | ✔ |

**Resultado: 38/38 casos pasan.** Los tests con ratón real (G-* y `BotonBymetooInteraccionTest`) se han ejecutado 4 veces seguidas sin fallos aleatorios.

## 6. Fuentes

- Código de `BotonBymetoo`, `BotonBymetooEvent` y `BotonBymetooListener` (`main` tras el PR #195, 25/11/2026) y `componentes/README.md`.
- Issues #135 (DI-3.2.3), #152 (DI-3.2.2) y #106 (DI-3.2).
- AssertJ Swing, documentación: https://assertj-swing.readthedocs.io/
- Oracle, *How to Write a Mouse Listener*: https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html
- Oracle, *How to Use Key Bindings*: https://docs.oracle.com/javase/tutorial/uiswing/misc/keybinding.html
