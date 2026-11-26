# DI-3.5.4 · Resultados de las pruebas de integración (#144)

**Fecha:** 26/11/2026 · **Código:** rama `elias-branch` (commit `172d351`, #115) con el componente de `main` (`24e79c4`) · **Plan:** `docs/qa/plan-pruebas-integracion.md`

## 1. Cómo se ha probado

Programa `Integracion.java` (AssertJ Swing): abre cada ventana de la app, busca todos los `BotonBymetoo`, anota su estado y hace **un clic real con el ratón en cada botón**. Después cuenta las ventanas abiertas. También prueba un doble clic por ventana. Ejecutado en Linux (JDK 21, pantalla virtual) y con capturas de cada ventana.

## 2. Botones encontrados

| Ventana | `BotonBymetoo` | Activo |
|---|---|---|
| `MainView` | Inicio, Perfil | Inicio ✔ |
| `UserView` | Inicio, Perfil, Salir | Perfil ✔ |
| `GroupView` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen | **ninguno** ✘ |
| `RepartosView` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen | Repartos ✔ |
| `CompraWindow` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen | Nueva compra ✔ |
| `ResumenView` | Inicio, Perfil, Gastos, Repartos, Nueva compra, Resumen | Resumen ✔ |

29 botones en total, con el estilo correcto (MENU en Inicio/Perfil, CONTORNO_SUAVE en Gastos, CONTORNO en el resto).

## 3. Resultado por caso

| ID | Caso | Resultado |
|---|---|---|
| INT-01 | La app compila con el componente | ✔ (comprobar también en NetBeans con Clean and Build) |
| INT-02 | Los botones se cargan en Matisse | ⏳ comprobar en NetBeans (los `.form` ya usan los botones) |
| INT-03 | Texto, icono y estilo en cada ventana | ✔ ver capturas |
| INT-04 | Solo el botón de la pantalla actual está activo | ✔ 6/6 tras corregir H-1 |
| INT-05 | Cambiar el tamaño de la ventana | ⏳ comprobar a mano |
| INT-06..INT-08 | Navegación entre ventanas | ✔ **29/29 clics** abren la ventana correcta y cierran la anterior |
| INT-09 | Pulsar el botón de la pantalla actual | ✔ no abre otra copia (5/5) |
| INT-10 | Doble clic | ✔ abre una sola ventana (6/6) |
| INT-11 | No se acumulan ventanas | ✔ tras cada clic queda 1 sola ventana visible |
| INT-12 | Salir | ✔ en `UserView` abre `LoginView` |
| INT-13 | Cancelar la navegación desde la app | No aplica: la app no cancela nunca (listeners vacíos) |
| INT-14, INT-15 | Datos entre ventanas | No aplica todavía: las ventanas muestran datos fijos de ejemplo |
| INT-16, INT-17 | Tab y Espacio/Intro | ✔ los 29 botones reciben el foco; el teclado ya está probado en el componente (G-04, G-05, CI-16) |

## 4. Hallazgos

**H-1 · `BotonGastos` lleva a la pantalla de inicio (gravedad media).**
Su `ventanaDestino` por defecto es `vista.MainView`, igual que Inicio. Por eso:
- en `GroupView` (la pantalla de gastos del grupo) ningún botón sale activo;
- pulsar «Gastos» desde Repartos, Nueva compra o Resumen te lleva a Inicio, no a los gastos del grupo.

Arreglo: en `BotonGastos.java`, `VENTANA_POR_DEFECTO = "vista.GroupView"`, y volver a generar el .jar.

**Corregido y verificado (26/11, rama `qa-fix-gastos`):** con el arreglo, en `GroupView` sale activo «Gastos» y desde Repartos, Nueva compra y Resumen «Gastos» abre `GroupView` (4/4). Tests del componente: 144/144.

**Observaciones (no bloquean)**
- «Compartir» y «Salir» siguen siendo `JButton` en `MainView`, `GroupView` y `CompraWindow` (y «Salir» solo es `BotonBymetoo` en `UserView`).
- Quedan `JButton` «Repartos» sin usar en `GroupView` (`jButton3`) y `CompraWindow` (`jButton7`).
- Los botones no tienen nombre (`setName`); no hace falta para estas pruebas, pero ayudaría a automatizarlas en el futuro.
- El código de #115 está en `elias-branch`; falta el PR a `main`.

## 5. Capturas

`MainView.png`, `UserView.png`, `GroupView.png`, `RepartosView.png`, `CompraWindow.png`, `ResumenView.png` (en esta carpeta).
