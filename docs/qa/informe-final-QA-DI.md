# DI · Informe final de QA del Sprint 3 (#146)

**Versión:** BORRADOR 0.1 (25/11/2026) · **Autor:** Sergei (DevOps & QA) · **Componente:** `BymetooBotones` (8 botones JavaBean Swing)

> Borrador. Los puntos marcados con **[ACTUALIZAR]** se rellenan antes de la defensa del 30/11, cuando estén #161 y los arreglos de SEG-02, SEG-04 y la ruta del .jar.

## 1. Resumen

| Área | Resultado a 25/11 |
|---|---|
| Tests automáticos | **144 tests, 142 pasan.** Los 2 que fallan son los hallazgos SEG-02 y SEG-04 (a propósito, hasta que se arreglen). |
| Errores de diseño #188, #189, #190 | Corregidos en el PR #195 y verificados con tests de regresión. |
| Casos de interacción (#135) | 38/38 casos con test. |
| .jar y Javadoc (#141) | .jar correcto, Javadoc 0 errores y 0 avisos. |
| Uso de recursos (#142) | Superado: 2,3 KB por botón, sin fugas. |
| Seguridad (#145) | 2 hallazgos de gravedad baja, con arreglo propuesto. |
| Integración en la app (#144) | Plan listo. **[ACTUALIZAR]** ejecución tras #161. |

**Veredicto provisional:** el componente es apto. **[ACTUALIZAR]** veredicto final cuando estén cerrados los errores abiertos de la sección 4.

## 2. Tests automáticos

Carpeta `BymetooBotones/test/com/bymetoo/componentes/`. JUnit 5 (ejecutado desde NetBeans con `@RunWith(JUnitPlatform.class)`) y AssertJ Swing para los clics reales.

| Clase | Qué prueba | Tests | Pasan |
|---|---|---|---|
| `BotonBymetooValoresTest` | Valores por defecto y propiedades (D, L) | 51 | 51 |
| `BotonBymetooEstadosTest` | Estados y pintado (E, P) | 29 | 29 |
| `BotonBymetooEventosTest` | Eventos y PropertyChange (EV) | 16 | 16 |
| `BotonBymetooNavegacionTest` | Navegación entre ventanas (N) | 10 | 10 |
| `BotonBymetooHallazgosTest` | Regresión de #188–#190 (O-1a, O-1b, O-2) | 3 | 3 |
| `BotonBymetooSeguridadTest` | Seguridad básica (SEG) | 12 | 10 |
| `BotonBymetooGuiTest` | Clics reales (G-01..G-10) | 10 | 10 |
| `BotonBymetooInteraccionTest` | Casos CI que faltaban | 13 | 13 |
| **Total** | | **144** | **142** |

> NetBeans cuenta algunos tests dos veces y muestra «220 passed». El número real es 144.

**Entornos:** Windows, JDK 26.0.2, NetBeans 31 (Sergei). Linux, JDK 21 con pantalla virtual (comprobación cruzada). **[ACTUALIZAR]** confirmar la última ejecución completa en Windows.

## 3. Errores corregidos

| Issue | Error | Corregido en | Test de regresión |
|---|---|---|---|
| #188 | El hover no se ve en CONTORNO | PR #195 | O-1a |
| #189 | En el menú HOVER = ACTIVO | PR #195 | O-1b |
| #190 | El texto deshabilitado se ve igual que el normal | PR #195 | O-2 |

## 4. Errores abiertos

| Issue | Error | Gravedad | Arreglo propuesto | Estado |
|---|---|---|---|---|
| **[nº]** | SEG-02: `crearVentana()` ejecuta el bloque `static` de clases que no son ventanas | Baja | `Class.forName(nombre, false, …)` | **[ACTUALIZAR]** |
| **[nº]** | SEG-04: el texto con `<html>` se interpreta | Baja | `etiqueta.putClientProperty("html.disable", Boolean.TRUE)` | **[ACTUALIZAR]** |
| **[nº]** | La app enlaza el .jar con ruta absoluta (`C:\Users\12081\...`) | Alta | Ruta relativa `../BymetooBotones/dist/BymetooBotones.jar` | **[ACTUALIZAR]** |

Con los dos arreglos de seguridad aplicados, todos los tests pasan (comprobado el 25/11).

## 5. Observaciones (no son errores, decide el equipo)

- **O-3:** el doble clic cuenta como dos pulsaciones. Con navegación solo abre una ventana (CI-11).
- **O-5:** en los botones con contorno, hover y activo usan el mismo verde #81DAAB.
- **O-6:** CONTORNO y CONTORNO_SUAVE se pintan igual; solo cambia el tamaño.
- **EV-10:** si un listener lanza una excepción en `botonPulsado`, los listeners siguientes no reciben el aviso.
- El .jar necesita **Java 25** o superior (indicarlo en la guía de instalación, #129).
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

**[ACTUALIZAR]** tabla con el resultado de cada caso y capturas de las ventanas.

## 8. Definition of Done del componente

| Criterio | Estado |
|---|---|
| Los 8 botones funcionan como JavaBean (constructor vacío, serializables, BeanInfo) | ✔ |
| Se pueden arrastrar desde la paleta de NetBeans | **[ACTUALIZAR]** (#161) |
| Tests unitarios y de interacción pasan | ✔ salvo SEG-02 y SEG-04 (abiertos) |
| Sin errores de gravedad alta abiertos | ✘ ruta absoluta del .jar **[ACTUALIZAR]** |
| Javadoc sin errores | ✔ 0 errores, 0 avisos |
| .jar generado y verificado | ✔ (repetir con el .jar final, #126) **[ACTUALIZAR]** |
| Uso de recursos dentro del criterio | ✔ |
| Integrado en la app `ByMeToo` | **[ACTUALIZAR]** (#144, #161) |

## 9. Documentos relacionados

- `docs/qa/plan-pruebas.md` · plan de pruebas
- `docs/qa/casos-interaccion.md` · casos CI y trazabilidad
- `docs/qa/informe-QA-DI-2026-11-25.md` · informe del 25/11
- `docs/qa/plan-pruebas-integracion.md` · plan de integración

## 10. Fuentes

- JUnit 5 User Guide: https://junit.org/junit5/docs/current/user-guide/
- AssertJ Swing, documentación: https://assertj.github.io/doc/#assertj-swing
- VisualVM, documentación: https://visualvm.github.io/documentation.html
