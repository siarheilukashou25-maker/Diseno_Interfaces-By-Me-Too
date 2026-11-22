package com.bymetoo.componentes;

/**
 * Botón "Compartir" de Bymetoo.
 * <p>
 * Al pulsarlo abre la pantalla del grupo (vista.GroupView) y cierra la ventana en la que estás.
 * Lleva el icono COMPARTIR y el estilo CONTORNO_SUAVE.
 * </p>
 *
 * <pre>{@code
 * BotonCompartir boton = new BotonCompartir();
 * panelMenu.add(boton);
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public class BotonCompartir extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.GroupView";

    /**
     * Crea el botón Compartir.
     */
    public BotonCompartir() {
        super("Compartir", TipoIcono.COMPARTIR, EstiloBoton.CONTORNO_SUAVE, VENTANA_POR_DEFECTO);
    }
}
