# DI-3.3.1 · Tests unitarios con JUnit: resultados

Issue #137 (padre #136) · QA: Siarhei · Plan: [`plan-pruebas.md`](plan-pruebas.md) · Casos: [`casos-interaccion.md`](casos-interaccion.md)

## 1. Qué se ha implementado

Tests en `componentes/test/com/bymetoo/componentes/`:

| Clase | Casos del plan | Tests |
|---|---|---|
| `BotonBymetooValoresTest` | D-01 … D-14, L-01 … L-18 | Valores por defecto de los 8 botones (parametrizado), getters/setters, valores inválidos (`null`, negativos, textos largos, emoji) |
| `BotonBymetooEstadosTest` | E-01 … E-11, P-01 … P-09 | Lógica de estados y prioridad; colores pintados leídos píxel a píxel |
| `BotonBymetooEventosTest` | EV-01 … EV-14, CI-38 | `estadoCambiado`, `botonPulsado`, orden de los eventos, cancelación de la navegación, `PropertyChangeEvent` |
| `BotonBymetooNavegacionTest` | N-01 … N-09 | Apertura y cierre de ventanas con ventanas falsas |
| `BotonBymetooHallazgosTest` | O-1, O-2 | Requisitos de diseño que hoy no se cumplen (fallan a propósito) |
| `Utilidades`, `ListenerContador`, `VentanaPruebaA/B`, `SinConstructorVacio` | — | Ayudas: EDT, eventos de ratón simulados, listener contador, ventanas falsas |

Todos los accesos a Swing se hacen en el EDT. Los tests que necesitan pantalla se saltan solos en modo *headless* (`assumeFalse(GraphicsEnvironment.isHeadless())`).

## 2. Cómo ejecutarlos en NetBeans

Los proyectos Ant de NetBeans solo lanzan tests con el runner de JUnit 4. Por eso cada clase de test lleva `@RunWith(JUnitPlatform.class)`: el runner de JUnit 4 la arranca y por debajo se ejecutan los tests de JUnit 5.

Librerías en `componentes/lib/`:

| JAR | Para qué |
|---|---|
| `junit-platform-console-standalone-1.11.4.jar` | JUnit 5 (Jupiter, tests parametrizados y plataforma) |
| `junit-platform-runner-1.11.4.jar` | Puente `@RunWith(JUnitPlatform.class)` |
| `junit-4.13.2.jar`, `hamcrest-core-1.3.jar` | Runner de JUnit 4 que usa NetBeans |

1. *Project Properties → Sources → Test Package Folders → Add Folder* → `test`.
2. *Project Properties → Libraries → Compile Tests → Classpath → + → Add JAR/Folder* → los 4 JAR de `lib/`. No añadir las librerías «JUnit 4.x» ni «JUnit 5.x» de NetBeans.
3. *Clean and Build* y después *Run → Test Project* (Alt+F6). Los informes quedan en `build/test/results/`.

NetBeans cuenta también como «test» cada grupo de pruebas parametrizadas, así que el total que muestra es mayor que 99 (109 en la ejecución de control).

Desde terminal (Windows, con el proyecto ya compilado por NetBeans):

```powershell
cd BymetooBotones
java -jar lib\junit-platform-console-standalone-1.11.4.jar execute -cp "build\classes;build\test\classes" --select-package com.bymetoo.componentes
```

## 3. Resultado de la ejecución

| Fecha | Código probado | Entorno | Tests | Pasados | Fallados | Saltados |
|---|---|---|---|---|---|---|
| 24/11/2026 | `elias-branch` (22/11) | JDK 21 + Xvfb (pantalla virtual) | 99 | 96 | 3 | 0 |
| 24/11/2026 | `elias-branch` (22/11) | JDK 21, *headless* | 99 | 89 | 3 | 7 (necesitan pantalla) |
| 24/11/2026 | `elias-branch` (22/11) | JDK 21 + Xvfb, runner de JUnit 4 (como NetBeans) | 109 | 106 | 3 | 0 |
| 24/11/2026 | `elias-branch` (22/11) | JDK 26, NetBeans en Windows (runner de JUnit 4) | 118 | 115 | 3 | 0 |

> **Nota.** Las dos primeras ejecuciones se hicieron en un entorno con JDK 21, con tres cambios de sintaxis de Java 25 solo en la copia de prueba. La ejecución válida es la de NetBeans con JDK 26 sobre el código original, sin cambios: mismo resultado (solo fallan los 3 hallazgos).

### Fallos

Los 3 fallos son los de `BotonBymetooHallazgosTest`, previstos en el plan (apartado 5):

| Test | Mensaje | Observación del plan | Gravedad | Para |
|---|---|---|---|---|
| O-1a | El fondo en HOVER es igual que en NORMAL (`#FAFAFA`) | O-1 | Media | Elias (feedback visual, DI-3.2.1) |
| O-1b | En el menú HOVER y ACTIVO se pintan igual | O-1 | Media | Elias |
| O-2 | `colorTextoDeshabilitado` es igual que `colorTexto` (`#1A202C`) | O-2 | Media | Roberto / Elias |

Se registran como issues en DI-3.3.6 (#139).

### Comportamientos anotados (pasan, pero hay que decidirlos)

| Test | Comportamiento actual |
|---|---|
| EV-05b | Si el botón no está dentro de una ventana, tras `pulsar()` se queda en `ACTIVO` |
| CI-10 (plan) | El doble clic pulsa dos veces (se probará con ratón real en DI-3.3.5) |

## 4. Fuentes

- JUnit 5 User Guide, *Parameterized Tests* y *Assumptions*: https://junit.org/junit5/docs/current/user-guide/
- JUnit Platform Console Launcher: https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher
- Oracle, *Concurrency in Swing*: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/
- Apache NetBeans, *Writing JUnit Tests in NetBeans IDE*: https://netbeans.apache.org/tutorial/main/kb/docs/java/junit-intro/
