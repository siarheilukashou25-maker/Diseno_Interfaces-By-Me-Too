package com.bymetoo.componentes;

/**
 * Los tres estilos de botón que hay en el boceto.
 * <ul>
 *   <li>MENU: sin borde (Inicio y Perfil).</li>
 *   <li>CONTORNO_SUAVE: con borde (Compartir y Gastos).</li>
 *   <li>CONTORNO: con borde (Repartos, Nueva compra, Resumen y Salir).</li>
 * </ul>
 *
 * <pre>{@code
 * EstiloBoton estilo = new BotonInicio().getEstilo(); // MENU
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public enum EstiloBoton {

    /** Botón de menú sin borde. */
    MENU,

    /** Botón con borde (Compartir y Gastos). */
    CONTORNO_SUAVE,

    /** Botón con borde (Repartos, Nueva compra, Resumen y Salir). */
    CONTORNO
}
