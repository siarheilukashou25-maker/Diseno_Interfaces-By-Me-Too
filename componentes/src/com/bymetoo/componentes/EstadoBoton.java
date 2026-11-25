package com.bymetoo.componentes;

/**
 * Los cuatro estados que puede tener un botón, igual que en el boceto.
 *
 * <pre>{@code
 * if (boton.getEstado() == EstadoBoton.ACTIVO) {
 *     System.out.println("Estás en " + boton.getTexto());
 * }
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public enum EstadoBoton {

    /** Estado normal. */
    NORMAL,

    /** El ratón está encima. */
    HOVER,

    /** El botón está seleccionado (es la pantalla en la que estás). */
    ACTIVO,

    /** El botón no se puede pulsar. */
    DESHABILITADO
}
