# BymetooBotones – Biblioteca de botones personalizados

Biblioteca de componentes gráficos desarrollada en **Java Swing** que proporciona ocho botones personalizados reutilizables en aplicaciones de escritorio.

Los componentes permiten mantener una apariencia visual homogénea y facilitar la navegación entre las distintas ventanas de una aplicación.

## Características

- Ocho botones personalizados para aplicaciones Java Swing.
- Integración en formularios mediante código o la paleta de componentes de NetBeans.
- Personalización de textos, colores, iconos y estilos.
- Sistema de eventos personalizados.
- Ayuda contextual mediante la tecla F1.
- Distribución como biblioteca Java mediante un archivo JAR.

## Requisitos

Para utilizar la biblioteca se necesita:

- Java JDK/JRE compatible con la versión con la que se ha compilado la biblioteca.
- Un entorno de desarrollo compatible con proyectos Java Swing.
- Para utilizar la paleta visual, se recomienda Apache NetBeans.

## Descarga

La biblioteca se distribuye desde la sección **Releases** del repositorio de GitHub.

**Última versión publicada: v1.0**

[Consultar las versiones y descargar BymetooBotones](https://github.com/siarheilukashou25-maker/Diseno-Interfaces-By-Me-Too/releases)

En los archivos adjuntos de cada versión se encuentra:

- `BymetooBotones.jar`: biblioteca necesaria para utilizar los componentes.
- Documentación Javadoc: referencia de las clases y métodos públicos.

## Instalación en un proyecto Java

La forma directa de instalar la biblioteca consiste en añadir el archivo `BymetooBotones.jar` al proyecto que vaya a utilizar los componentes.

### Apache NetBeans

1. Descargar `BymetooBotones.jar` desde la sección Releases.
2. Abrir en NetBeans el proyecto Java Swing en el que se quieran utilizar los botones.
3. En el panel de proyectos, desplegar el apartado **Libraries**.
4. Hacer clic derecho sobre **Libraries** y seleccionar **Add JAR/Folder**.
5. Buscar y seleccionar el archivo `BymetooBotones.jar`.
6. Confirmar la operación.

Una vez añadido, las clases de la biblioteca estarán disponibles para el proyecto.

### Añadir los botones a la paleta de NetBeans

Para poder utilizar los componentes visualmente desde el diseñador de formularios:

1. Abrir NetBeans.
2. Acceder al menú **Tools → Palette → Swing/AWT Components**.
3. Seleccionar **Add from JAR...**.
4. Buscar el archivo `BymetooBotones.jar`.
5. Seleccionar los botones que se quieran incorporar.
6. Elegir una categoría de la paleta.
7. Finalizar el asistente.

Los componentes aparecerán en la paleta y podrán arrastrarse directamente a los formularios Swing en modo **Design**.

## Instalación en otros entornos de desarrollo

La biblioteca también puede utilizarse desde otros entornos compatibles con Java.

El procedimiento general consiste en:

1. Descargar `BymetooBotones.jar`.
2. Añadir el archivo JAR al classpath del proyecto.
3. Importar las clases necesarias desde `com.bymetoo.componentes`.
4. Crear y utilizar los botones desde el código Java o desde el diseñador visual del entorno, si es compatible.

## Generar la biblioteca desde el código fuente

También es posible obtener el archivo JAR compilando el proyecto.

Para ello:

1. Clonar o descargar el repositorio.
2. Abrir la carpeta `componentes` como proyecto en Apache NetBeans.
3. Ejecutar **Clean and Build**.
4. Una vez finalizado el proceso, localizar el archivo generado.

La ruta del archivo es:

`componentes/dist/BymetooBotones.jar`

El proyecto utiliza Apache Ant para automatizar la compilación.

## Documentación técnica

Para consultar información detallada sobre el funcionamiento y la utilización de los componentes, consultar:

- **[Manual técnico](docs/ManualTecnico.md):** arquitectura, clases, propiedades, estados, estilos, eventos, navegación, ejemplos de integración y mantenimiento.
- **[Documentación de eventos y ayuda contextual](docs/DI-3.2-eventos-y-ayuda.md):** información sobre los eventos personalizados y la ayuda contextual.

La documentación Javadoc también está disponible entre los archivos adjuntos de las versiones publicadas.

## Estructura del proyecto

```text
componentes/
├── src/
│   ├── com/bymetoo/componentes/
│   └── iconos/
├── test/
├── lib/
├── docs/
│   ├── ManualTecnico.md
│   └── DI-3.2-eventos-y-ayuda.md
├── build.xml
├── manifest.mf
└── README.md
```

## Distribución y versiones

Las versiones publicadas de la biblioteca se encuentran en la sección **Releases** de GitHub.

Cada versión puede incluir el archivo JAR y la documentación correspondiente para facilitar su descarga e integración en otros proyectos.

Para consultar las novedades, correcciones y cambios de cada versión, revisar las notas de publicación de GitHub.
