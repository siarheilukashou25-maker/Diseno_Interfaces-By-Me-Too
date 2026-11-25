# By Me Too! / Comprame! — Base técnica y arquitectura

Repositorio del proyecto **By Me Too! / Comprame!** (TFG de DAM en Java / Java Swing con Maven y Git/GitHub). Este documento recoge la organización técnica del repositorio, el versionado y las decisiones de construcción y empaquetado para la tarea **DI-3.1**.

---

## Estructura del repositorio

* `componentes/` — Componentes visuales reutilizables en desarrollo (botones, recursos, pruebas y configuración del proyecto).
* `ByMeToo/` — Aplicación principal y pantallas del proyecto.
* `docs/` — Documentación por sprints (`Sprint1/`, `Sprint2/`, `Sprint3/`), calidad (`qa/`) y documentación técnica/arquitectura.

---

## Control de versiones y flujo de trabajo

* **Rama principal (`main`):** integra los cambios validados y estables del repositorio.
* **Ramas de trabajo:** desarrollo por integrante cada uno en su rama , integrando en `main` mediante fusiones controladas / Pull Requests.
* **Limpieza del repositorio:** exclusión de archivos de sistema (como `.DS_Store`) y archivos temporales/de compilación mediante `.gitignore`.

---

## Versionado semántico (`SemVer`)

Se adopta la convención **`MAYOR.MENOR.PARCHE`** (`X.Y.Z`) para cuando se empaqueten versiones del proyecto:

* **`MAYOR` (`X`):** cambios grandes o incompatibles con versiones anteriores.
* **`MENOR` (`Y`):** nuevas funcionalidades, pantallas o componentes compatibles.
* **`PARCHE` (`Z`):** corrección de errores o ajustes menores sin romper compatibilidad.

> **Estado actual:** la aplicación está en construcción (pantallas iniciales creadas sin funcionalidad y componentes visuales repetidos en desarrollo). Por ahora **no se ha generado ningún `.jar` de prueba ni versión empaquetada**; `SemVer` se aplicará cuando comiencen las entregas/builds.

---

## Construcción y empaquetado (Windows)

* **Herramienta de build:** **Maven** (`pom.xml`).
* **Sistema / formato objetivo:** **Windows** (`.exe`).
* **Herramienta de instalador elegida:** `jpackage` *(o pon aquí `Inno Setup` / `Launch4j` si habéis elegido esa en concreto)*.
* **Estado de ejecución:** por ahora apenas hay componentes y pantallas en desarrollo, por lo que no se ha montado aún un empaquetado funcional ni un instalador final; queda fijada la herramienta y la base en Maven para fases posteriores.

---

## Documentación ampliada

Para detalles de sprints, pruebas, diagramas y especificaciones técnicas adicionales, consulta la carpeta `docs/`.