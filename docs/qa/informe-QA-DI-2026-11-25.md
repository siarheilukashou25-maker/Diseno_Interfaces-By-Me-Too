# Informe QA · DI Sprint 3 · 25/11/2026

**Autor:** Sergei (DevOps & QA) · **Componente:** `BotonBymetoo` (8 botones) · **Código probado:** PR #195 de Roberto (`roberto-branch`, commit `3ed41d1`)

## 1. Resumen

| Tarea | Resultado |
|---|---|
| DI-3.3.6 (#139) · verificación de #188, #189 y #190 | **Corregidos.** Los tests O-1a, O-1b y O-2 pasan con el código del PR #195. |
| DI-3.3.1 (#137) · tests actualizados al nuevo diseño | 109 tests, 109 pasan. |
| DI-3.3.5 (#138) · clics reales con AssertJ Swing | 10 tests nuevos, 10 pasan (3 ejecuciones seguidas, sin fallos aleatorios). |
| DI-3.4.3 (#141) · .jar y Javadoc | Javadoc: 0 errores y 0 avisos. El .jar coincide con el código fuente. 2 observaciones. |
| DI-3.5.6 (#145) · seguridad básica | 12 tests, 10 pasan. **2 hallazgos** de gravedad baja. |
| DI-3.4.5 (#142) · uso de recursos | Programa de medición y valores de referencia. Falta la medición con VisualVM en Windows. |

**Total: 131 tests, 129 pasan, 2 fallan a propósito** (hallazgos de seguridad SEG-02 y SEG-04).

> ⚠️ **El PR #195 borra la carpeta `test/`, las librerías de `lib/` y su configuración en `nbproject`.** Hay que recuperarlas antes de fusionarlo o justo después.

## 2. Verificación de los errores (#139)

| Error | Test | Antes (24/11) | Ahora (PR #195) |
|---|---|---|---|
| #188 el hover no se ve en CONTORNO | O-1a | Falla | Pasa: hover verde #81DAAB |
| #189 en el menú HOVER = ACTIVO | O-1b | Falla | Pasa: hover rgba(26,32,44,0.08) y activo rgba(26,32,44,0.14) |
| #190 texto deshabilitado = texto normal | O-2 | Falla | Pasa: #1A202C con alpha 100 |

Los tres tests se quedan como **pruebas de regresión**.

## 3. Cambios en los tests (nuevo diseño del PR #195)

Roberto cambió a propósito varios valores por defecto: hover, activo, bordes y radio. 11 tests comprobaban los valores antiguos y se han actualizado:

- **D-07 a D-10:** hover y activo según el estilo; borde #E2E8E5 en todos; texto deshabilitado con alpha; radio 6; separación icono-texto 12.
- **P-01:** el borde de CONTORNO ahora es #E2E8E5.
- **P-06:** en el menú, hover y activo se comparan con su color real. Se pintan sobre blanco porque tienen transparencia.
- **L-09:** comprueba que el radio no cambia, sin fijar el valor 12.

Tests nuevos para lo que añadió el PR:

| ID | Qué comprueba |
|---|---|
| P-10 | Mientras se mantiene pulsado se pinta `colorActivo`; al soltar fuera vuelve a hover |
| P-11 | Si el ratón sale mientras está pulsado, deja de pintarse pulsado |
| P-12 | Deshabilitado: apretar el ratón no lo pinta pulsado |
| P-13 | Con el ratón encima, el borde usa `colorBordeHover` |
| L-11, L-12 | `colorBordeHover` se guarda y no admite `null` |
| L-19, L-20 | `separacionIcono` se guarda; si es negativa, lanza excepción y no cambia |
| EV-15 | `colorBordeHover` y `separacionIcono` avisan con PropertyChange |

## 4. Clics reales con AssertJ Swing (#138)

Clase `BotonBymetooGuiTest`. Un robot mueve el ratón y pulsa teclas sobre una ventana real. Se usa AssertJ Swing porque TestFX solo sirve para JavaFX.

| ID | Caso | Resultado |
|---|---|---|
| G-01 | Pasar el ratón: HOVER y al salir NORMAL | ✔ |
| G-02 | Clic izquierdo: `botonPulsado` una vez | ✔ |
| G-03 | Clic derecho y central: no pulsa | ✔ |
| G-04 | Espacio e Intro con foco: pulsa | ✔ |
| G-05 | Se llega al botón con Tab | ✔ |
| G-06 | Deshabilitado: ni clic ni teclado | ✔ |
| G-07 | Apretar, arrastrar fuera y soltar: no cuenta | ✔ |
| G-08 | Doble clic: cuenta 2 pulsaciones (observación O-3 del plan) | ✔ (se documenta el comportamiento) |
| G-09 | Clic real: abre la ventana destino y cierra la actual | ✔ |
| G-10 | Mantener pulsado no pulsa hasta soltar | ✔ |

Sin pantalla se saltan (17 tests en total). **Mientras se ejecutan no hay que tocar el ratón.**

## 5. Comprobación del .jar y del Javadoc (#141)

| Comprobación | Resultado |
|---|---|
| Clases del .jar = clases compiladas del código fuente | ✔ 30 clases y 16 iconos, iguales |
| No incluye tests ni `.java` | ✔ |
| Manifest: 8 botones marcados `Java-Bean: True` | ✔ |
| BeanInfo: icono 16×16 y 32×32, 22 propiedades y 2 eventos por botón | ✔ los 8 |
| Serializable y con constructor vacío (JavaBean) | ✔ los 8 |
| Javadoc con `-Xdoclint:all` | ✔ 0 errores, 0 avisos |
| Versión de las clases | Java 25 (class 69) |

**Observaciones**

1. **El .jar necesita Java 25 o superior.** Con Java 21 da `UnsupportedClassVersionError`. Hay que decirlo en el README o en la guía de instalación (#129).
2. **La app `ByMeToo` enlaza el .jar con una ruta absoluta del ordenador de Roberto:** `C:\Users\12081\Downloads\BymetooBotones\dist\BymetooBotones.jar` (`ByMeToo/nbproject/project.properties`). En cualquier otro ordenador la app no compila. Debe ser relativa, `../BymetooBotones/dist/BymetooBotones.jar`. Afecta a #144 y #161.
3. #159 (module-info) está cerrada, pero no hay ningún `module-info.java` en el repositorio.

## 6. Seguridad básica (#145)

Clase `BotonBymetooSeguridadTest`.

| ID | Caso | Resultado |
|---|---|---|
| SEG-01 | `ventanaDestino` con una clase que no es ventana: no se llama a su constructor | ✔ |
| SEG-02 | Esa clase tampoco debería inicializarse (bloque `static`) | ✘ **hallazgo** |
| SEG-03 | Destinos raros (`java.lang.Runtime`, `ProcessBuilder`, rutas, `;rm -rf /`…): no crea nada y solo lanza `IllegalStateException` | ✔ 7 casos |
| SEG-04 | Un texto que empieza por `<html>` se muestra tal cual | ✘ **hallazgo** |
| SEG-05 | Texto de 100 000 caracteres: no se cuelga | ✔ |
| SEG-06 | Serializar y deserializar conserva los datos | ✔ |

**Hallazgos (gravedad baja)**

- **SEG-02:** `crearVentana()` usa `Class.forName(nombre, true, …)`. Así ejecuta el bloque `static` de la clase *antes* de comprobar que es una ventana. Arreglo de una palabra: `true` → `false`.
- **SEG-04:** `JLabel` interpreta HTML. Si algún día el texto viene del usuario (por ejemplo, el nombre de un grupo), se puede cambiar el aspecto o cargar imágenes de Internet. Arreglo de una línea en `inicializarInterfaz()`: `etiqueta.putClientProperty("html.disable", Boolean.TRUE);`.

Con los dos arreglos aplicados, los 121 tests sin pantalla pasan (comprobado).

## 7. Uso de recursos (#142)

Programa `MedicionRecursos` (en `test/`, se ejecuta con **Run File**). Tiene 4 escenarios separados por pausas, para verlos en VisualVM. Valores de referencia en Linux con JDK 21 (la medición final se hace en Windows con JDK 26 y VisualVM):

| Escenario | Tiempo | Memoria retenida | Lectura |
|---|---|---|---|
| E1 crear 1 000 botones | 182 ms | 2,9 MB | ≈ 2,9 KB por botón |
| E2 10 000 cambios hover/normal | 119 ms | — | ≈ 0,006 ms por cambio |
| E3 abrir/cerrar 200 ventanas con 8 botones | 1,5 s | 0,4 MB | Sin fuga apreciable |
| E4 navegar 100 veces con el botón | 1,4 s | 0,05 MB | Sin fuga |

Al terminar quedan 2 hilos vivos y 1 ventana ya cerrada pendiente del GC.

**Criterio de aceptación propuesto:** menos de 10 KB por botón; menos de 1 ms por cambio de estado; tras E3 y E4, el heap vuelve a su nivel después del GC (sin escalera en la gráfica); ningún hilo nuevo al terminar.

## 8. Observaciones de diseño (para decidir con el equipo, no son errores)

- **O-5:** en los botones con contorno, hover y activo usan el mismo verde #81DAAB. Si estás en «Repartos» y pasas el ratón por «Gastos», se ven dos botones verdes iguales.
- **O-6:** CONTORNO y CONTORNO_SUAVE ahora se pintan igual (el mismo borde #E2E8E5). El estilo solo sirve para el tamaño.
- **O-3** (sigue igual): el doble clic cuenta como dos pulsaciones (G-08).

## 9. Fuentes

- AssertJ Swing, documentación: https://assertj.github.io/doc/#assertj-swing
- JUnit 5 User Guide (assumptions, tags, JUnit Platform Runner): https://junit.org/junit5/docs/current/user-guide/
- Java SE API, `Class.forName(String, boolean, ClassLoader)`: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Class.html#forName(java.lang.String,boolean,java.lang.ClassLoader)
- Java Tutorials, *How to Use HTML in Swing Components* (`html.disable`): https://docs.oracle.com/javase/tutorial/uiswing/components/html.html
- VisualVM, documentación: https://visualvm.github.io/documentation.html
- javadoc, opción `-Xdoclint`: https://docs.oracle.com/en/java/javase/21/docs/specs/man/javadoc.html
