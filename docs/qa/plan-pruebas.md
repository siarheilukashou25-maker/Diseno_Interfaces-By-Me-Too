# DI-3.1.3 · Plan de pruebas del componente `BotonBymetoo`

Sprint 3 · QA (Siarhei) · Proyecto **By Me Too** (Java Swing, NetBeans/Ant) · Issue #133 (padre #132)

**Versión:** 2.0 (24/11/2026) · Sustituye a la v1 del 20/11, que se basaba en las clases supuestas `MenuButton`/`GroupButton`.
**Código probado:** proyecto `BymetooBotones/`, rama `elias-branch` (commit del 22/11, con los colores del diseño). Todavía no está en `main`.

## 1. Objetivo y alcance

Verificar que el componente propio `BotonBymetoo` y sus 8 botones cumplen la especificación (DI-3.1.1, DI-3.1.2, DI-3.2.2) antes de integrarlo en la aplicación.

| Clase | Estilo (`EstiloBoton`) | Ventana destino por defecto |
|---|---|---|
| `BotonInicio` | `MENU` | `vista.MainView` |
| `BotonPerfil` | `MENU` | `vista.UserView` |
| `BotonCompartir` | `CONTORNO_SUAVE` | `vista.GroupView` |
| `BotonGastos` | `CONTORNO_SUAVE` | `vista.MainView` |
| `BotonRepartos` | `CONTORNO` | `vista.RepartosView` |
| `BotonNuevaCompra` | `CONTORNO` | `vista.CompraWindow` |
| `BotonResumen` | `CONTORNO` | `vista.ResumenView` |
| `BotonSalir` | `CONTORNO` | `vista.LoginView` |

`BotonBymetoo` es una clase `abstract sealed` que hereda de `JPanel`. No se puede instanciar directamente, así que las pruebas usan las subclases. Como las 8 solo cambian los datos del constructor, se prueba **un botón de cada estilo** en profundidad (`BotonInicio`, `BotonCompartir`, `BotonSalir`) y los 8 con pruebas parametrizadas para los valores por defecto.

Se prueba:

1. Valores por defecto tras el constructor.
2. Getters/setters y valores límite o inválidos.
3. Estados visuales (`EstadoBoton`: `NORMAL`, `HOVER`, `ACTIVO`, `DESHABILITADO`) y su prioridad.
4. Eventos: `BotonBymetooEvent` (`estadoCambiado`, `botonPulsado`), cancelación de la navegación y `PropertyChangeEvent`.
5. Navegación entre ventanas (`pulsar()`, `navegar()`, `existeVentanaDestino()`).
6. Interacción real con ratón y teclado (casos de DI-3.2.3).

Fuera de alcance de este plan: lógica de negocio de grupos y gastos; rendimiento (DI-3.4.5); seguridad (DI-3.5.6); integración en la app (DI-3.5.4).

## 2. Herramientas

| Herramienta | Uso |
|---|---|
| **JUnit 5 (Jupiter)** | Pruebas unitarias. `@Test`, `@ParameterizedTest`, `@MethodSource`, `@BeforeEach`, `assertEquals`, `assertThrows`, `assertDoesNotThrow`. |
| **AssertJ Swing** (`assertj-swing-junit`) | Pruebas de GUI (DI-3.3.5): un robot mueve el ratón, hace clic y pulsa teclas sobre un `JFrame` real. |
| Renderizado a `BufferedImage` | Comprobar el color pintado en cada estado: se llama a `paint(g)` sobre una imagen y se lee un píxel. Funciona sin pantalla (*headless*). |
| Listener contador | Clase de ayuda en los tests que implementa `BotonBymetooListener` y `PropertyChangeListener`, cuenta las llamadas y guarda el último evento. |

> **Nota sobre TestFX.** Las issues DI-3.3 y DI-3.3.5 nombran TestFX, pero TestFX solo sirve para JavaFX y el componente es Swing (`JPanel`). Para Swing se usa **AssertJ Swing**, que ofrece lo mismo (robot, *fixtures*, comprobaciones sobre componentes).

Reglas comunes:

- Todo acceso a componentes Swing se hace en el EDT con `SwingUtilities.invokeAndWait(...)` (o `GuiActionRunner.execute(...)` en AssertJ Swing).
- En las pruebas unitarias se llama a `setNavegacionAutomatica(false)` salvo en las de navegación. Así `pulsar()` no intenta abrir ventanas de la app, que no existen en el proyecto del componente. En modo *headless*, si la ventana no existe, `pulsar()` lanza `IllegalStateException` en vez de mostrar el diálogo.
- Para la navegación se crean en `test/` dos ventanas falsas (`VentanaPruebaA`, `VentanaPruebaB`, que heredan de `JFrame` y tienen constructor sin parámetros) y se apunta `ventanaDestino` a ellas.
- Las pruebas de GUI necesitan pantalla. Sin pantalla se saltan con `Assumptions.assumeFalse(GraphicsEnvironment.isHeadless())`.
- El proyecto se compila con **Java 25** (`javac.source=25`): usa constructores con código antes de `super()` y la variable `_`. Los tests necesitan el mismo JDK.
- Las librerías de test se añaden en NetBeans a *Test Libraries* (`javac.test.classpath`) desde una carpeta `componentes/lib/`. Los tests van en `componentes/test/com/bymetoo/componentes/`.

Clases de prueba previstas:

| Clase | Tipo | Secciones |
|---|---|---|
| `BotonBymetooValoresTest` | Unitaria | 4.1, 4.2 |
| `BotonBymetooEstadosTest` | Unitaria + renderizado | 4.3 |
| `BotonBymetooEventosTest` | Unitaria | 4.4 |
| `BotonBymetooNavegacionTest` | Unitaria (con ventanas falsas) | 4.5 |
| `BotonBymetooGuiTest` | GUI (AssertJ Swing) | DI-3.2.3 (`casos-interaccion.md`) |

## 3. Criterio de aceptación

Un caso de prueba se considera **pasado** cuando se cumplen las dos condiciones:

1. El resultado obtenido **coincide con el resultado esperado** de la tabla.
2. **No se lanza ninguna excepción no prevista.** Si el caso espera una excepción (por ejemplo `IllegalArgumentException` con un radio negativo), se comprueba con `assertThrows` y el caso pasa si se lanza esa excepción y no otra.

Si falla cualquiera de las dos, el caso está **fallado** y se abre una issue (DI-3.3.6).

Los colores se comparan canal a canal (RGBA) de forma exacta en los getters. En las pruebas por píxel se admite ±2 por canal por el antialiasing.

El plan se da por superado cuando pasan todos los casos de prioridad **Alta** y no queda ningún fallo abierto de gravedad Alta.

## 4. Casos de prueba

Colores del diseño (DI-3.1.2) y constante que los define en el código:

| Uso | Valor | Constante |
|---|---|---|
| Fondo | `#FAFAFA` | `COLOR_FONDO_POR_DEFECTO` |
| Hover | `#FAFAFA` | `COLOR_HOVER_POR_DEFECTO` |
| Activo (verde menta) | `#81DAAB` | `COLOR_ACTIVO_POR_DEFECTO` |
| Borde oscuro | `#1A202C` | `COLOR_BORDE_OSCURO` |
| Borde suave | `#E2E8E5` | `COLOR_BORDE_SUAVE` |
| Texto e icono | `#1A202C` | `COLOR_TEXTO_POR_DEFECTO` |
| Radio de las esquinas | `12` | `RADIO_BORDE_POR_DEFECTO` |

### 4.1 Valores por defecto

Parametrizado con las 8 subclases salvo que se indique otra cosa.

| ID | Propiedad | Resultado esperado | Prioridad |
|---|---|---|---|
| D-01 | `getEstado()` | `EstadoBoton.NORMAL` | Alta |
| D-02 | `isEnabled()` / `isActivo()` | `true` / `false` | Alta |
| D-03 | `getTexto()` | Texto de la tabla del apartado 1 («Inicio», «Perfil», …, «Nueva compra», «Salir») | Alta |
| D-04 | `getVentanaDestino()` | `VENTANA_POR_DEFECTO` de cada clase | Alta |
| D-05 | `getEstilo()` | El de la tabla del apartado 1 | Alta |
| D-06 | `isNavegacionAutomatica()` / `isCerrarVentanaActual()` | `true` / `true` | Media |
| D-07 | `getColorFondo()`, `getColorHover()`, `getColorActivo()` | `#FAFAFA`, `#FAFAFA`, `#81DAAB` | Media |
| D-08 | `getColorBorde()` | `#E2E8E5` en `CONTORNO_SUAVE`; `#1A202C` en `MENU` y `CONTORNO` | Media |
| D-09 | `getColorTexto()` | `#1A202C` | Media |
| D-10 | `getRadioBorde()` | `12` | Media |
| D-11 | `getIconoPersonalizado()` | `null` (se usa el icono de `TipoIcono`) | Baja |
| D-12 | `getPreferredSize()` | `MENU`: al menos 120×50. `CONTORNO*`: al menos 140×38 | Media |
| D-13 | `isFocusable()` / `isOpaque()` | `true` / `false` | Baja |
| D-14 | `getBotonBymetooListeners()` | Array vacío | Baja |

### 4.2 Getters/setters y valores límite

| ID | Propiedad | Entrada | Resultado esperado | Prioridad |
|---|---|---|---|---|
| L-01 | `texto` | `"Mis gastos"` | `getTexto()` devuelve `"Mis gastos"` | Alta |
| L-02 | `texto` | `""` | Se guarda `""`; se pinta solo el icono, sin error | Media |
| L-03 | `texto` | `null` | Se guarda `""`; sin excepción | Alta |
| L-04 | `texto` | 500 caracteres | Se guarda; `getPreferredSize().width` crece por encima del mínimo; se pinta sin error | Media |
| L-05 | `texto` | Caracteres especiales y emoji (`"ñ € 😀 <b>"`) | Se guarda tal cual; sin excepción | Media |
| L-06 | `ventanaDestino` | `"  vista.UserView  "` | Se guarda sin espacios: `"vista.UserView"` | Media |
| L-07 | `ventanaDestino` | `null` | Se guarda `""` | Alta |
| L-08 | `radioBorde` | `0` | Se guarda `0` (esquinas rectas) | Media |
| L-09 | `radioBorde` | `-1` | `IllegalArgumentException`; el valor anterior se conserva | Alta |
| L-10 | `radioBorde` | `Integer.MAX_VALUE` | Se guarda y se pinta sin error | Baja |
| L-11 | `colorFondo`, `colorHover`, `colorActivo`, `colorBorde`, `colorTexto`, `colorTextoDeshabilitado` | Un color válido | El getter devuelve ese color | Alta |
| L-12 | Mismos colores | `null` | `NullPointerException` con el nombre de la propiedad en el mensaje; el valor anterior se conserva | Alta |
| L-13 | Colores | Alpha `0` y alpha `255` | Se guardan y se pintan sin error | Baja |
| L-14 | `iconoPersonalizado` | Un `ImageIcon` 16×16 | Se muestra ese icono en lugar del de `TipoIcono` | Baja |
| L-15 | `iconoPersonalizado` | `null` | Se vuelve al icono de `TipoIcono` | Baja |
| L-16 | `font` | `new Font(SANS_SERIF, BOLD, 20)` | `getFont()` devuelve esa fuente; el tamaño preferido se recalcula | Baja |
| L-17 | `font` | `null` | Sin excepción; se conserva la fuente anterior | Media |
| L-18 | `addBotonBymetooListener` | `null` | Se ignora; la lista de listeners sigue igual | Media |

### 4.3 Estados visuales

Prioridad del estado (de mayor a menor): **`DESHABILITADO` → `ACTIVO` → `HOVER` → `NORMAL`**.

| Estado | Cómo se provoca en la prueba unitaria |
|---|---|
| `NORMAL` | Botón recién creado |
| `HOVER` | Se envía un `MouseEvent.MOUSE_ENTERED` al botón (`dispatchEvent`) |
| `ACTIVO` | `setActivo(true)` |
| `DESHABILITADO` | `setEnabled(false)` |

**Lógica de estados** (`getEstado()`)

| ID | Secuencia | Resultado esperado | Prioridad |
|---|---|---|---|
| E-01 | `MOUSE_ENTERED` | `HOVER` | Alta |
| E-02 | `MOUSE_ENTERED` → `MOUSE_EXITED` | `NORMAL` | Alta |
| E-03 | `setActivo(true)` | `ACTIVO` | Alta |
| E-04 | `setActivo(true)` → `setActivo(false)` | `NORMAL` | Alta |
| E-05 | `setEnabled(false)` | `DESHABILITADO` | Alta |
| E-06 | `setEnabled(false)` → `setEnabled(true)` | `NORMAL` | Alta |
| E-07 | `setActivo(true)` → `setEnabled(false)` | `DESHABILITADO` (gana a `ACTIVO`) | Alta |
| E-08 | `setActivo(true)` → `setEnabled(false)` → `setEnabled(true)` | `ACTIVO` (se conserva) | Media |
| E-09 | `MOUSE_ENTERED` → `setActivo(true)` | `ACTIVO` (gana a `HOVER`) | Media |
| E-10 | `MOUSE_ENTERED` → `setEnabled(false)` → `setEnabled(true)` | `NORMAL` (el hover se borra al deshabilitar) | Media |
| E-11 | `setEnabled(false)` → `MOUSE_ENTERED` | Sigue `DESHABILITADO` | Alta |

**Aspecto pintado** (renderizado a `BufferedImage` de 140×38 o 120×50)

| ID | Estilo | Estado | Resultado esperado | Prioridad |
|---|---|---|---|---|
| P-01 | `CONTORNO` | `NORMAL` | Píxel central de fondo `#FAFAFA`; borde `#1A202C` | Media |
| P-02 | `CONTORNO_SUAVE` | `NORMAL` | Fondo `#FAFAFA`; borde `#E2E8E5` | Media |
| P-03 | `CONTORNO*` | `ACTIVO` | Fondo `#81DAAB` | Alta |
| P-04 | `CONTORNO*` | Esquina `(0,0)` | Transparente (fuera del redondeo de radio 12) | Baja |
| P-05 | `MENU` | `NORMAL` | Sin fondo (transparente) y sin borde | Media |
| P-06 | `MENU` | `HOVER` y `ACTIVO` | Se pinta el fondo `colorHover` | Media |
| P-07 | Todos | `DESHABILITADO` | El texto usa `colorTextoDeshabilitado`; el cursor es `DEFAULT_CURSOR` | Media |
| P-08 | Todos | Cualquiera menos `DESHABILITADO` | Cursor `HAND_CURSOR` | Baja |
| P-09 | `CONTORNO*` | `DESHABILITADO` con `activo = true` | Se pinta como deshabilitado: el fondo **no** es verde | Media |

### 4.4 Eventos

Se registra el listener contador con `addBotonBymetooListener` y/o `addPropertyChangeListener`.

| ID | Acción | Resultado esperado | Prioridad |
|---|---|---|---|
| EV-01 | `setActivo(true)` | `estadoCambiado` ×1; `getTipo() = CAMBIO_ESTADO`, `getEstadoAnterior() = NORMAL`, `getEstadoNuevo() = ACTIVO`, `getBoton()` es el botón | Alta |
| EV-02 | `setActivo(true)` dos veces | `estadoCambiado` ×1 (la segunda no cambia nada) | Alta |
| EV-03 | `MOUSE_ENTERED` y `MOUSE_EXITED` | `estadoCambiado` ×2: `NORMAL→HOVER` y `HOVER→NORMAL` | Alta |
| EV-04 | `pulsar()` con navegación desactivada | `botonPulsado` ×1 con `getTipo() = PULSADO` y `getVentanaDestino()` igual a la del botón | Alta |
| EV-05 | `pulsar()` con el botón dentro de un `JFrame` mostrado (no es su ventana destino) | Orden: `estadoCambiado(NORMAL→ACTIVO)` → `botonPulsado` → `estadoCambiado(ACTIVO→NORMAL)`. Sin ventana (`isDisplayable() = false`) el botón se queda en `ACTIVO`: se comprueba aparte y se anota | Media |
| EV-06 | `setEnabled(false)` y `pulsar()` | `botonPulsado` ×0 | Alta |
| EV-07 | Dos listeners registrados y `pulsar()` | Los dos reciben `botonPulsado` | Media |
| EV-08 | `removeBotonBymetooListener(l)` y `pulsar()` | `l` no recibe nada | Alta |
| EV-09 | Listener que llama a `e.cancelarNavegacion()` | `isNavegacionCancelada() = true`; no se abre ninguna ventana (ver N-03) | Alta |
| EV-10 | Listener que lanza `RuntimeException` en `botonPulsado` | 🔶 Comportamiento a decidir con Roberto: ahora la excepción sale de `pulsar()` y los listeners siguientes no reciben el evento | Baja |
| EV-11 | `PropertyChangeListener` y `setTexto("X")` | `PropertyChangeEvent` con nombre `PROP_TEXTO`, valor antiguo y nuevo correctos | Media |
| EV-12 | Mismo valor dos veces (`setTexto("X")` ×2) | Un solo `PropertyChangeEvent` (Swing no avisa si el valor no cambia) | Media |
| EV-13 | `PropertyChangeListener` en `PROP_ESTADO` y `setActivo(true)` | Se recibe `NORMAL → ACTIVO` | Media |
| EV-14 | `addPropertyChangeListener(l)` y `setTexto("X")` | 🔶 `l` recibe el evento **una sola vez**. Riesgo: el listener se registra en `super` y en `propertySupport`; si los dos avisan, llegaría ×2 | Media |

### 4.5 Navegación

Se usan las ventanas falsas `VentanaPruebaA` y `VentanaPruebaB` de `test/`. Necesita pantalla (se salta en *headless*).

| ID | Acción | Resultado esperado | Prioridad |
|---|---|---|---|
| N-01 | `existeVentanaDestino()` con `"vista.NoExiste"` y con `""` | `false` | Alta |
| N-02 | `existeVentanaDestino()` con `VentanaPruebaB` y con `"java.lang.String"` | `true` / `false` (no es una ventana) | Media |
| N-03 | Botón dentro de `VentanaPruebaA`, destino `VentanaPruebaB`, `pulsar()` | Se abre `VentanaPruebaB` y se cierra `VentanaPruebaA` | Alta |
| N-04 | Igual que N-03 con `setCerrarVentanaActual(false)` | Se abre B y A sigue abierta | Media |
| N-05 | Botón dentro de su ventana destino, al mostrarse (`addNotify`) | `isActivo() = true`, estado `ACTIVO` | Alta |
| N-06 | Destino inexistente, al mostrarse (`addNotify`) | Se deshabilita y el *tooltip* es «Pantalla no disponible: …» | Alta |
| N-07 | `navegar()` con destino `""` | Devuelve `null`, sin abrir nada | Media |
| N-08 | `navegar()` con una clase sin constructor público sin parámetros | `IllegalStateException` con mensaje claro | Media |
| N-09 | `navegar()` estando ya en la ventana destino | Devuelve la ventana actual; no abre otra | Media |

### 4.6 Interacción con ratón y teclado

Los casos de clic, doble clic, serie de clics, hover, teclado y cambios de estado no válidos están en [`casos-interaccion.md`](casos-interaccion.md) (DI-3.2.3). Se automatizan en `BotonBymetooGuiTest` (DI-3.3.5).

## 5. Observaciones previas (revisión del código, sin ejecutar)

Detectadas al preparar el plan. Se confirman al ejecutar las pruebas y, si fallan, se registran como issues (DI-3.3.6).

| # | Observación | Casos | Gravedad propuesta |
|---|---|---|---|
| O-1 | `COLOR_HOVER_POR_DEFECTO` es igual a `COLOR_FONDO_POR_DEFECTO` (`#FAFAFA`). En los botones `CONTORNO*` el hover no se ve; en `MENU`, `HOVER` y `ACTIVO` se pintan igual y casi no se distinguen del fondo. El diseño del Sprint 3 marcaba un tinte para hover y otro más oscuro para activo en el menú. | P-06, CI-11 | Media |
| O-2 | `colorTextoDeshabilitado` tiene el mismo valor que `colorTexto`. El estado deshabilitado no se distingue visualmente (pauta de accesibilidad del Sprint 2: no depender solo del cursor). | P-07 | Media |
| O-3 | El doble clic llama dos veces a `pulsar()` (dos `mouseReleased`). Con navegación activa puede intentar abrir la ventana dos veces. | CI-06 | Media |
| O-4 | `paintComponent` lo marca el propio código como «solo un ejemplo»; el feedback visual definitivo es de Elias (DI-3.2.1). Los casos P-* se revisan cuando lo entregue. | P-* | — |

## 6. Ejecución y registro

- Ejecución en NetBeans: *Run → Test Project* (Alt+F6) o `ant test` desde `componentes/`.
- Informes JUnit en `componentes/build/test/results/`. El resumen de cada ejecución se guarda en `docs/qa/` del repositorio.
- Cada ejecución se registra con: fecha, rama/commit, casos ejecutados, pasados, fallados e issues abiertas.

| Fecha | Commit | Casos ejecutados | Pasados | Fallados | Issues |
|---|---|---|---|---|---|
| | | | | | |

## 7. Dependencias

| Tarea | Responsable | Para qué la necesita este plan | Estado (24/11) |
|---|---|---|---|
| DI-3.1.1 Clase del componente | Roberto | Clases y API | Hecha (rama, falta PR a `main`) |
| DI-3.1.2 Bocetos y propiedades | Elias | Colores y estados esperados | Hecha (colores en `elias-branch`) |
| DI-3.2.2 Evento propio y listener | Roberto | Sección 4.4 | Hecha |
| DI-3.2.1 Feedback visual | Elias | Casos P-* definitivos | En curso |
| DI-3.3.4 Tests en la construcción | Pablo | Que `ant` falle si un test falla | Pendiente |

## 8. Fuentes

- Código de `BymetooBotones` (ramas `roberto-branch` y `elias-branch`, 22/11/2026) y su `README.md`.
- Issues #133 (DI-3.1.3), #150 (DI-3.1.1), #103 (DI-3.1.2), #152 (DI-3.2.2) del repositorio `Diseno_Interfaces-By-Me-Too`.
- JUnit 5 User Guide: https://junit.org/junit5/docs/current/user-guide/
- AssertJ Swing, documentación: https://assertj-swing.readthedocs.io/
- TestFX (solo JavaFX): https://github.com/TestFX/TestFX
- Oracle, *Concurrency in Swing* (EDT): https://docs.oracle.com/javase/tutorial/uiswing/concurrency/
- Oracle, *How to Write a Property Change Listener*: https://docs.oracle.com/javase/tutorial/uiswing/events/propertychangelistener.html
- JEP 513, *Flexible Constructor Bodies* (Java 25): https://openjdk.org/jeps/513
