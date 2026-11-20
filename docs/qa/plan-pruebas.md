# Plan de pruebas — Botones personalizados `MenuButton` y `GroupButton`

Sprint 3 · QA · Proyecto **By Me Too** (Java Swing, NetBeans/Ant)

## 1. Objetivo y alcance

Verificar que los dos botones personalizados del menú cumplen la especificación de diseño (tabla de propiedades y bocetos de estados):

| Componente | Uso en la app | Botones del boceto |
|---|---|---|
| `MenuButton` (Botón Menú General) | Menú lateral | Inicio, Perfil |
| `GroupButton` (Botón de Grupo) | Acciones dentro del grupo | Compartir, Gastos, Repartos, Nueva compra, Resumen, Salir |

Se prueba:

1. Valores por defecto tras el constructor.
2. Límites (valores frontera) de las propiedades.
3. Estados visuales: Normal, Hover, Activo y Deshabilitado.
4. Eventos: `ActionEvent`, ratón y cambios de propiedad.

Fuera de alcance: lógica de negocio de grupos y gastos, rendimiento y accesibilidad (auditada en DI-2.6).

> **Supuesto.** En la rama `main` todavía no existen las clases `MenuButton` y `GroupButton`. El plan se basa en la tabla de diseño, cuyas propiedades (`borderPainted`, `iconTextGap`, `PreferredSize`) son las de `JButton`. Por eso se asume que ambas clases heredan de `JButton` y que el estado Activo se representa con `setSelected(true)`. Los nombres de las propiedades propias (`arc`, colores de fondo por estado, color de borde) se ajustarán al código real cuando esté implementado.

## 2. Herramientas

| Herramienta | Uso |
|---|---|
| **JUnit 5 (Jupiter)** | Pruebas unitarias: valores por defecto, límites y eventos. `@Test`, `@ParameterizedTest`, `@BeforeEach`, `assertEquals`, `assertDoesNotThrow`. |
| **AssertJ Swing** (`assertj-swing-junit`) | Pruebas de GUI: el robot mueve el ratón, hace clic y comprueba el estado del componente dentro de un `JFrame` real. |
| `java.awt.Robot` | Alternativa sin librerías externas para simular hover y clic. |
| Renderizado a `BufferedImage` | Comprobar el color real pintado en cada estado: se llama a `paint(g)` sobre una imagen y se lee el píxel del fondo. Funciona también en modo *headless*. |

Reglas comunes:

- Todo acceso a componentes Swing se hace en el EDT con `SwingUtilities.invokeAndWait(...)` (o `GuiActionRunner.execute(...)` en AssertJ Swing).
- Las librerías de JUnit 5 se añaden en NetBeans a *Test Libraries* (`javac.test.classpath`); los tests van en `ByMeToo/test/`.
- Las pruebas de GUI necesitan pantalla; en un entorno sin pantalla se marcan con `Assumptions.assumeFalse(GraphicsEnvironment.isHeadless())`.

Clases de prueba previstas:

| Clase | Tipo | Contenido |
|---|---|---|
| `MenuButtonTest` | Unitaria (JUnit 5) | Secciones 4.1, 4.2, 4.4 para `MenuButton` |
| `GroupButtonTest` | Unitaria (JUnit 5) | Secciones 4.1, 4.2, 4.4 para `GroupButton` |
| `BotonesEstadosTest` | Renderizado (JUnit 5) | Sección 4.3, colores por estado |
| `BotonesGuiTest` | GUI (AssertJ Swing) | Hover, clic y deshabilitado con ratón real |

## 3. Criterio de aceptación

Un caso de prueba se considera **pasado** cuando:

1. El resultado obtenido **coincide con el resultado esperado** indicado en la tabla.
2. **No se lanza ninguna excepción** durante la ejecución (incluidos los valores frontera e inválidos).

Si no se cumple cualquiera de las dos condiciones, el caso está **fallado**. El plan se da por superado cuando pasan todos los casos.

Para los colores con transparencia se compara cada canal RGBA de forma exacta. En las pruebas por píxel se admite una tolerancia de ±2 por canal por el antialiasing.

## 4. Casos de prueba

Conversión de los valores del diseño a `java.awt.Color`:

| Valor de diseño | `Color` esperado |
|---|---|
| Texto `#1A202C` | `new Color(26, 32, 44)` |
| `rgba(26, 32, 44, 0.08)` | `new Color(26, 32, 44, 20)` (0,08 × 255 ≈ 20) |
| `rgba(26, 32, 44, 0.14)` | `new Color(26, 32, 44, 36)` (0,14 × 255 ≈ 36) |
| `#FAFAFA` | `new Color(250, 250, 250)` |
| `#81DAAB` | `new Color(129, 218, 171)` |
| `#E2E8E5` | `new Color(226, 232, 229)` |

### 4.1 Valores por defecto

**`MenuButton`**

| ID | Propiedad | Resultado esperado |
|---|---|---|
| MB-D01 | `arc` | `0` (rectangular estricto) |
| MB-D02 | Fondo Normal | Transparente, alpha = `0` |
| MB-D03 | Fondo Hover | `Color(26, 32, 44, 20)` |
| MB-D04 | Fondo Activo | `Color(26, 32, 44, 36)` |
| MB-D05 | `isBorderPainted()` | `false` |
| MB-D06 | `getForeground()` | `Color(26, 32, 44)` (`#1A202C`) |
| MB-D07 | `getHorizontalAlignment()` | `SwingConstants.LEFT` (icono a la izquierda) |
| MB-D08 | `getPreferredSize()` | Tamaño fijo definido en la clase, igual en todas las instancias e independiente del texto |
| MB-D09 | `isEnabled()` / `isSelected()` | `true` / `false` |
| MB-D10 | `isContentAreaFilled()` | `false` (el fondo lo pinta el componente) |

**`GroupButton`**

| ID | Propiedad | Resultado esperado |
|---|---|---|
| GB-D01 | `arc` | `12` o `15`, según el valor fijado en la clase |
| GB-D02 | Fondo Normal | `Color(250, 250, 250)` (`#FAFAFA`) |
| GB-D03 | Fondo Hover | `Color(250, 250, 250)` (mantiene la base) |
| GB-D04 | Fondo Activo | `Color(129, 218, 171)` (`#81DAAB`) |
| GB-D05 | Fondo Deshabilitado | `Color(250, 250, 250)` |
| GB-D06 | Borde | Línea continua de `1` px en `Color(226, 232, 229)` (`#E2E8E5`) |
| GB-D07 | `getForeground()` | `Color(26, 32, 44)` |
| GB-D08 | `getHorizontalAlignment()` | `SwingConstants.LEFT` |
| GB-D09 | Margen externo (`margin` / `gap`) | Valor por defecto definido en la clase, mayor o igual que `0` |
| GB-D10 | `isEnabled()` / `isSelected()` | `true` / `false` |

### 4.2 Límites de las propiedades

Se usa `@ParameterizedTest` con `@ValueSource` / `@CsvSource`. En todos los casos se exige además que no haya excepción (`assertDoesNotThrow`).

> **Supuesto.** El diseño no fija qué hacer con valores inválidos. Para cumplir el criterio «sin excepciones», se espera que los valores negativos se ajusten a `0` y que `null` restaure el valor por defecto. Si el equipo decide otro comportamiento, se actualiza la columna «Esperado».

| ID | Componente | Propiedad | Entrada | Esperado |
|---|---|---|---|---|
| L-01 | `MenuButton` | `arc` | `0` | `0` |
| L-02 | `GroupButton` | `arc` | `12` | `12` |
| L-03 | `GroupButton` | `arc` | `15` | `15` |
| L-04 | Ambos | `arc` | `-1` | `0` |
| L-05 | Ambos | `arc` | `Integer.MAX_VALUE` | Se guarda y el botón se pinta sin error |
| L-06 | Ambos | `iconTextGap` | `0` | `0` (icono pegado al texto) |
| L-07 | Ambos | `iconTextGap` | `4` (valor de Swing) y `8` | Se guarda el valor indicado |
| L-08 | Ambos | `iconTextGap` | `-1` | `0` |
| L-09 | Ambos | Alpha del fondo | `0` | Fondo invisible |
| L-10 | Ambos | Alpha del fondo | `255` | Fondo opaco |
| L-11 | Ambos | Color de fondo / borde | `null` | Se restaura el color por defecto |
| L-12 | `GroupButton` | Grosor de borde | `1` | Borde de 1 px (único valor del diseño) |
| L-13 | Ambos | Texto | `""` | Solo se muestra el icono; sin error |
| L-14 | Ambos | Texto | `null` | Se trata como `""`; sin error |
| L-15 | Ambos | Texto | 200 caracteres | `MenuButton` mantiene su `PreferredSize` fijo; se pinta sin error |
| L-16 | Ambos | Icono | `null` | Solo texto, alineado a la izquierda |
| L-17 | `GroupButton` | `margin` / `gap` | `0` | Sin espaciado externo |
| L-18 | `GroupButton` | `margin` / `gap` | negativo | `0` |

### 4.3 Estados visuales

Prioridad del estado (de mayor a menor): **Deshabilitado → Activo → Hover → Normal**.

Método: se crea el botón con tamaño fijo, se pone en el estado indicado y se pinta en un `BufferedImage` (`TYPE_INT_ARGB`). Se lee el píxel del fondo (cerca de una esquina, lejos del texto y del icono, y dentro del radio `arc` en `GroupButton`).

| Estado | Cómo se provoca |
|---|---|
| Normal | Botón recién creado |
| Hover | `getModel().setRollover(true)` (unitaria) o ratón encima (GUI) |
| Activo | `setSelected(true)` |
| Deshabilitado | `setEnabled(false)` |

**`MenuButton`**

| ID | Estado | Resultado esperado |
|---|---|---|
| MB-E01 | Normal | Píxel de fondo transparente (alpha `0`); sin borde |
| MB-E02 | Hover | Fondo `rgba(26, 32, 44, 0.08)`; esquinas rectas (`arc = 0`) |
| MB-E03 | Activo | Fondo `rgba(26, 32, 44, 0.14)`, más oscuro que Hover |
| MB-E04 | Deshabilitado | Fondo transparente; texto e icono con opacidad menor que en Normal |
| MB-E05 | Deshabilitado + rollover | Se pinta como Deshabilitado, no como Hover |
| MB-E06 | Activo + rollover | Se pinta como Activo |

**`GroupButton`**

| ID | Estado | Resultado esperado |
|---|---|---|
| GB-E01 | Normal | Fondo `#FAFAFA`; borde 1 px `#E2E8E5`; esquinas redondeadas |
| GB-E02 | Hover | Fondo `#FAFAFA` (igual que Normal) |
| GB-E03 | Activo | Fondo `#81DAAB` (verde menta) |
| GB-E04 | Deshabilitado | Fondo `#FAFAFA`; texto e icono con opacidad menor que en Normal |
| GB-E05 | Deshabilitado + seleccionado | Se pinta como Deshabilitado, no verde |
| GB-E06 | Esquina exterior | El píxel `(0, 0)` queda fuera del redondeo: transparente |

**Transiciones**

| ID | Secuencia | Resultado esperado |
|---|---|---|
| T-01 | Normal → Hover → Normal | El fondo vuelve exactamente al color de Normal |
| T-02 | Normal → Activo → Normal | `setSelected(false)` restaura el fondo de Normal |
| T-03 | Deshabilitado → Habilitado | Vuelve al aspecto de Normal |
| T-04 | Activo → Deshabilitado → Habilitado | Vuelve a Activo (la selección se conserva) |

### 4.4 Eventos

| ID | Componente | Acción | Resultado esperado |
|---|---|---|---|
| EV-01 | Ambos | `doClick()` con 1 `ActionListener` | El listener recibe exactamente 1 `ActionEvent`; `getSource()` es el botón |
| EV-02 | Ambos | `doClick()` con 2 listeners | Los dos reciben el evento |
| EV-03 | Ambos | `removeActionListener(l)` y `doClick()` | `l` no recibe nada |
| EV-04 | Ambos | `setEnabled(false)` y `doClick()` | No se dispara ningún `ActionEvent` |
| EV-05 | Ambos | `setActionCommand("gastos")` y `doClick()` | `getActionCommand()` del evento es `"gastos"` |
| EV-06 | Ambos | `setSelected(true)` con `ItemListener` / `ChangeListener` | Se notifica el cambio de estado |
| EV-07 | Ambos | Cambiar `arc`, colores o `iconTextGap` con `PropertyChangeListener` | Se recibe `PropertyChangeEvent` con valor antiguo y nuevo correctos |
| EV-08 | Ambos | Asignar el mismo valor que ya tenía | No se dispara `PropertyChangeEvent` |
| EV-09 | Ambos | Cambiar cualquier propiedad visual | El componente se repinta (se llama a `repaint()`) |

### 4.5 Pruebas de GUI (AssertJ Swing)

Los botones se colocan en un `JFrame` de prueba con `GroupLayout` (como en Matisse) y se manejan con `JButtonFixture`.

| ID | Pasos | Resultado esperado |
|---|---|---|
| GUI-01 | Mover el ratón sobre `MenuButton` «Inicio» | `getModel().isRollover()` es `true`; el fondo pasa a Hover |
| GUI-02 | Sacar el ratón del botón | Vuelve a Normal |
| GUI-03 | Clic en `GroupButton` «Gastos» | Se dispara 1 `ActionEvent` |
| GUI-04 | Clic en «Gastos» y luego en «Repartos» (grupo de selección única) | Solo «Repartos» queda en Activo (verde) |
| GUI-05 | Clic en un botón deshabilitado | Sin evento; aspecto sin cambios |
| GUI-06 | Tecla Espacio con el foco en el botón | Mismo efecto que un clic |
| GUI-07 | Redimensionar la ventana | `MenuButton` conserva su tamaño fijo; ambos se pintan sin errores |
| GUI-08 | Captura de cada estado | Comparación visual con el boceto «Bocetos de Componentes Botones» |

## 5. Ejecución y registro

- Ejecución en NetBeans: *Run → Test Project* (Alt+F6) o `ant test` desde `ByMeToo/`.
- Los informes de JUnit se guardan en `ByMeToo/build/test/results/`.
- Cada ejecución se registra con: fecha, rama/commit, ID del caso, resultado (pasado/fallado) y, si falla, la descripción y la captura.

| Fecha | Commit | Casos ejecutados | Pasados | Fallados | Observaciones |
|---|---|---|---|---|---|
| | | | | | |

## 6. Fuentes

- Especificación de propiedades de `MenuButton` y `GroupButton` y bocetos de estados (Normal, Hover, Activo, Deshabilitado) entregados por el equipo.
- JUnit 5 User Guide: https://junit.org/junit5/docs/current/user-guide/
- AssertJ Swing: https://joel-costigliola.github.io/assertj/assertj-swing.html
- Oracle, *Concurrency in Swing* (EDT): https://docs.oracle.com/javase/tutorial/uiswing/concurrency/
- Javadoc de `javax.swing.AbstractButton` (`setIconTextGap`, `setBorderPainted`, `doClick`).
