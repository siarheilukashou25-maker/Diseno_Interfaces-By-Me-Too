# DI-3.4.6 · Plan de pruebas de integración (#144)

**Versión:** 1.0 (25/11/2026) · **Autor:** Sergei (QA) · **Estado:** Plan listo; ejecución **bloqueada por #161**

## 1. Objetivo

Comprobar que los 8 botones de `BymetooBotones.jar` funcionan **dentro de la app real `ByMeToo`**: se ven con el estilo correcto, marcan la pantalla activa y navegan a la ventana correcta sin dejar ventanas abiertas.

Los tests unitarios y de clics reales (144 tests) prueban el componente aislado. Este plan prueba la unión componente + app.

## 2. Estado de partida (revisión de `main`, 25/11)

| Punto | Estado | Impacto |
|---|---|---|
| Las vistas de `ByMeToo/src/vista` usan `BotonBymetoo` | ❌ Todas usan `JButton` normales | Sin #161 no hay nada que integrar |
| Enlace al .jar en `ByMeToo/nbproject/project.properties` | ❌ Ruta absoluta `C:\Users\12081\...` | En otro PC no compila |
| Java de la app y del .jar | El .jar es Java 25 (class 69) | La app debe compilarse y ejecutarse con JDK 25 o superior |
| `vista/icons/RepartosView.java` | Copia distinta de `vista/RepartosView.java` | Aclarar cuál es la buena antes de cambiar botones |
| `ResumenView`: «Pagar con Bizum» usa texto `<html>` | Es `JButton`, no `BotonBymetoo` | Si se cambia por un `BotonBymetoo` y se aplica el arreglo de SEG-04, el salto de línea dejará de funcionar |

**Requisitos para empezar:** #161 hecho, ruta relativa `../BymetooBotones/dist/BymetooBotones.jar`, Clean and Build de los dos proyectos sin errores.

## 3. Qué botón va en cada ventana

Según los textos actuales de las vistas:

| Ventana | Botones que deberían ser `BotonBymetoo` | Activo esperado |
|---|---|---|
| `MainView` | Inicio, Perfil, Compartir, Salir | Inicio |
| `UserView` | Inicio, Perfil | Perfil |
| `GroupView` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen, Compartir, Salir | Gastos |
| `RepartosView` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen | Repartos |
| `CompraWindow` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen, Salir | Nueva compra |
| `ResumenView` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen | Resumen |
| `LoginView` | Ninguno (sus botones no son de navegación) | — |

Los botones de acción (Añadir, Editar, Pagado, Crear grupo…) se quedan como `JButton`.

## 4. Casos de prueba

Precondición común: app arrancada desde `ByMeToo.java`, sesión iniciada, un grupo de prueba creado.

### 4.1 Montaje y aspecto

| ID | Caso | Resultado esperado |
|---|---|---|
| INT-01 | Clean and Build de `ByMeToo` en un PC que no es el de Roberto | Compila sin errores |
| INT-02 | Abrir cada vista en Matisse (Design) | Los botones aparecen en la paleta y en el diseño, sin errores de carga |
| INT-03 | Abrir cada ventana de la tabla 3 | Cada botón muestra su texto e icono; estilo MENU en Inicio/Perfil, CONTORNO_SUAVE en Compartir/Gastos, CONTORNO en el resto |
| INT-04 | Mirar el botón de la pantalla actual | Solo ese botón está ACTIVO (columna «Activo esperado») |
| INT-05 | Cambiar el tamaño de la ventana | Los botones no se cortan ni se solapan |

### 4.2 Navegación

| ID | Caso | Resultado esperado |
|---|---|---|
| INT-06 | Desde `MainView`, pulsar Perfil | Se abre `UserView` y se cierra `MainView` |
| INT-07 | Desde `UserView`, pulsar Inicio | Se abre `MainView` |
| INT-08 | Desde `GroupView`, recorrer Gastos → Repartos → Nueva compra → Resumen → Gastos | Cada clic abre su ventana; en cada una el activo es el correcto |
| INT-09 | Pulsar el botón de la pantalla en la que ya estás | No se abre otra copia de la ventana |
| INT-10 | Doble clic en cualquier botón de navegación | Se abre **una** ventana destino (comportamiento CI-11) |
| INT-11 | Navegar 20 veces entre ventanas y mirar `Window.getWindows()` o VisualVM | No se acumulan ventanas visibles; memoria estable (como E4 de #142) |
| INT-12 | Salir | Se cierra la ventana actual / la app según lo que decida el equipo |
| INT-13 | Un listener de la app cancela la navegación (por ejemplo, compra sin guardar) | No cambia de ventana (CI-08) |

### 4.3 Datos entre ventanas

| ID | Caso | Resultado esperado |
|---|---|---|
| INT-14 | Crear una compra en `CompraWindow` y pulsar Resumen | El resumen muestra la compra nueva |
| INT-15 | Navegar Gastos → Repartos → Gastos | Los datos del grupo no se pierden |

> INT-14 e INT-15 dependen de que la ventana destino reciba el grupo actual. Con `ventanaDestino` y constructor vacío no se pueden pasar datos: hay que decidir con Roberto si se navega con un listener del Controlador en lugar de la navegación automática.

### 4.4 Accesibilidad y teclado

| ID | Caso | Resultado esperado |
|---|---|---|
| INT-16 | Recorrer la ventana con Tab | Se llega a todos los botones de navegación en orden lógico |
| INT-17 | Espacio o Intro sobre un botón | Navega igual que el clic |

## 5. Cómo se ejecuta

1. **Manual** (todos los casos): checklist de este documento, una columna por PC (Windows y otro sistema si se puede). Captura de pantalla de INT-03/INT-04 por ventana.
2. **Automático** (INT-06..INT-11, INT-16, INT-17): clase `ByMeTooIntegracionTest` en `ByMeToo/test`, con AssertJ Swing como `BotonBymetooGuiTest`. Busca los botones por nombre (`setName`), así que en #161 hay que dar un nombre a cada botón (`btnInicio`, `btnGastos`…).

## 6. Criterio de aceptación

- INT-01..INT-17 en ✔, o los fallos registrados como issue con la plantilla de bug.
- Ninguna ventana duplicada ni abierta de más tras navegar.
- Resultado añadido al informe final de QA (#146).

## 7. Dependencias

- #161 (Roberto): usar los botones en las vistas.
- Bug de la ruta absoluta del .jar.
- Decisión sobre cómo pasar datos entre ventanas (sección 4.3).

## 8. Fuentes

- Java Tutorials, *JavaBeans*: https://docs.oracle.com/javase/tutorial/javabeans/
- AssertJ Swing, documentación: https://assertj.github.io/doc/#assertj-swing
- Java SE API, `Window.getWindows()`: https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/java/awt/Window.html#getWindows()
