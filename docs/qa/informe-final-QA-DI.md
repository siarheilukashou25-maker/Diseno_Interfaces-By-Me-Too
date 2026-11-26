# DI · Informe final de QA del Sprint 3 (#146)

**Versión:** 0.2 (26/11/2026) · **Código revisado:** `main` (commit `24e79c4`) · **Autor:** Sergei (DevOps & QA) · **Componente:** `BymetooBotones` (8 botones JavaBean Swing)

> Integración probada el 26/11 sobre `elias-branch` (#115). Quedan solo las comprobaciones manuales marcadas con **[ACTUALIZAR]**.

## 1. Resumen

| Área | Resultado a 26/11 |
|---|---|
| Tests automáticos | **144 tests, 144 pasan.** |
| Errores de diseño #188, #189, #190 | Corregidos en el PR #195 y verificados con tests de regresión. |
| Casos de interacción (#135) | 38/38 casos con test. |
| .jar y Javadoc (#141) | .jar `ByMeToo/Jar/BymetooBotones.jar` correcto (incluye los arreglos de seguridad). Javadoc: 0 errores y 0 avisos. Falta volver a generarlo con `AyudaContextual`. |
| Uso de recursos (#142) | Superado: 2,3 KB por botón, sin fugas. |
| Seguridad (#145) | 2 hallazgos de gravedad baja (#198, #199), corregidos y verificados. |
| Integración en la app (#144) | 29 botones en 6 ventanas. Navegación correcta en 29/29 clics, sin ventanas duplicadas. 1 hallazgo (H-1: «Gastos» llevaba a Inicio), ya corregido. |

**Veredicto:** el componente es **apto**: todos los errores registrados están corregidos y todos los tests pasan. 

## 2. Tests automáticos

Carpeta `componentes/test/com/bymetoo/componentes/`. JUnit 5 (ejecutado desde NetBeans con `@RunWith(JUnitPlatform.class)`) y AssertJ Swing para los clics reales.

| Clase | Qué prueba | Tests | Pasan |
|---|---|---|---|
| `BotonBymetooValoresTest` | Valores por defecto y propiedades (D, L) | 51 | 51 |
| `BotonBymetooEstadosTest` | Estados y pintado (E, P) | 29 | 29 |
| `BotonBymetooEventosTest` | Eventos y PropertyChange (EV) | 16 | 16 |
| `BotonBymetooNavegacionTest` | Navegación entre ventanas (N) | 10 | 10 |
| `BotonBymetooHallazgosTest` | Regresión de #188–#190 (O-1a, O-1b, O-2) | 3 | 3 |
| `BotonBymetooSeguridadTest` | Seguridad básica (SEG) | 12 | 12 |
| `BotonBymetooGuiTest` | Clics reales (G-01..G-10) | 10 | 10 |
| `BotonBymetooInteraccionTest` | Casos CI que faltaban | 13 | 13 |
| **Total** | | **144** | **144** |

> NetBeans cuenta algunos tests dos veces y muestra «220 passed». El número real es 144.

**Entornos:** Windows, JDK 26.0.2, NetBeans 31 (Sergei). Linux, JDK 21 con pantalla virtual (comprobación cruzada). Última ejecución completa: 26/11 sobre `main` (`24e79c4`), 144/144.

## 3. Errores corregidos

| Issue | Error | Corregido en | Test de regresión |
|---|---|---|---|
| #188 | El hover no se ve en CONTORNO | PR #195 | O-1a |
| #189 | En el menú HOVER = ACTIVO | PR #195 | O-1b |
| #190 | El texto deshabilitado se ve igual que el normal | PR #195 | O-2 |
| #198 | `crearVentana()` ejecuta el bloque `static` de clases que no son ventanas (gravedad baja) | commit `299e12e` | SEG-02 |
| #199 | El texto con `<html>` se interpreta como HTML (gravedad baja) | commit `299e12e` | SEG-04 |
| #200 | La app enlaza el .jar con ruta absoluta (gravedad alta) | commits `231b062`, `240d4d4` y `d2fb24f`: ruta relativa `Jar/BymetooBotones.jar` | Revisión de `ByMeToo/nbproject/project.properties` |

## 4. Errores abiertos

Ninguno. Registro completo en #139: 6 errores (#188–#190, #198–#200), los 6 corregidos y verificados.

## 5. Observaciones (no son errores, decide el equipo)

- **O-3:** el doble clic cuenta como dos pulsaciones. Con navegación solo abre una ventana (CI-11).
- **O-5:** en los botones con contorno, hover y activo usan el mismo verde #81DAAB.
- **O-6:** CONTORNO y CONTORNO_SUAVE se pintan igual; solo cambia el tamaño.
- **EV-10:** si un listener lanza una excepción en `botonPulsado`, los listeners siguientes no reciben el aviso.
- El .jar necesita **Java 25** o superior (indicarlo en la guía de instalación, #129).
- El .jar de `ByMeToo/Jar/` (commit `8929ba7`) es anterior a `AyudaContextual` (añadida el 26/11): hay que volver a generarlo (#126). `AyudaContextual` todavía no tiene tests.
- #159 está cerrada pero no hay `module-info.java` en el repositorio.

## 6. Rendimiento (#142)

Windows, JDK 26.0.2, VisualVM 2.2.2:

| Escenario | Resultado | Criterio |
|---|---|---|
| Crear 1 000 botones | 123 ms · ≈ 2,3 KB por botón | < 10 KB ✔ |
| 10 000 cambios de estado | ≈ 0,005 ms por cambio | < 1 ms ✔ |
| Abrir/cerrar 200 ventanas | 0,39 MB retenidos, el heap vuelve a su nivel | Sin fugas ✔ |
| Navegar 100 veces | 0,03 MB retenidos | Sin fugas ✔ |

## 7. Integración en la app (#144)

Plan: `docs/qa/plan-pruebas-integracion.md` (17 casos INT-01..INT-17).

Resultados completos y capturas: `docs/qa/integracion/resultados-integracion.md`.

| Comprobación | Resultado |
|---|---|
| Compila con el componente | ✔ |
| Texto, icono y estilo en las 6 ventanas | ✔ |
| Botón activo de la pantalla actual | ✔ 6/6 tras corregir H-1 |
| Navegación con clic real | ✔ 29/29 |
| Pulsar el botón de la pantalla actual no abre otra copia | ✔ 5/5 |
| Doble clic abre una sola ventana | ✔ 6/6 |
| Foco con Tab | ✔ 29/29 |
| Matisse y cambio de tamaño | **[ACTUALIZAR]** comprobar en NetBeans |

**H-1 (gravedad media):** `BotonGastos` tenía como destino `vista.MainView`, igual que Inicio. En `GroupView` no salía ningún botón activo y «Gastos» llevaba a Inicio. Corregido (`VENTANA_POR_DEFECTO = "vista.GroupView"`) y verificado el 26/11.

«Compartir» y «Salir» siguen siendo `JButton` en `MainView`, `GroupView` y `CompraWindow`.

## 8. Definition of Done del componente

| Criterio | Estado |
|---|---|
| Los 8 botones funcionan como JavaBean (constructor vacío, serializables, BeanInfo) | ✔ |
| Se pueden arrastrar desde la paleta de NetBeans | **[ACTUALIZAR]** comprobar con el .jar final |
| Tests unitarios y de interacción pasan | ✔ 144/144 |
| Sin errores de gravedad alta abiertos | ✔ (#200 corregido) |
| Javadoc sin errores | ✔ 0 errores, 0 avisos |
| .jar generado y verificado | ✔ (volver a generarlo con `AyudaContextual`, #126) |
| Uso de recursos dentro del criterio | ✔ |
| Integrado en la app `ByMeToo` | ✔ en `elias-branch` (falta PR a `main`); H-1 corregido |

## 9. Documentos relacionados

- `docs/qa/plan-pruebas.md` · plan de pruebas
- `docs/qa/casos-interaccion.md` · casos CI y trazabilidad
- `docs/qa/informe-QA-DI-2026-11-25.md` · informe del 25/11
- `docs/qa/plan-pruebas-integracion.md` · plan de integración
- `docs/qa/integracion/resultados-integracion.md` · resultados de integración y capturas

## 10. Fuentes

- JUnit 5 User Guide: https://junit.org/junit5/docs/current/user-guide/
- AssertJ Swing, documentación: https://assertj.github.io/doc/#assertj-swing
- VisualVM, documentación: https://visualvm.github.io/documentation.html
