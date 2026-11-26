package com.bymetoo.componentes;

/**
 * Botón "Repartos" de Bymetoo.
 * <p>
 * Al pulsarlo abre la pantalla de repartos (vista.RepartosView) y cierra la ventana en la que estás.
 * Lleva el icono REPARTOS y el estilo CONTORNO.
 * </p>
 *
 * <pre>{@code
 * BotonRepartos boton = new BotonRepartos();
 * panelMenu.add(boton);
 * }</pre>
 *
 * Es final porque BotonBymetoo es sealed y solo deja heredar a estos botones.
 *
 * @author Equipo Bymetoo
 */
public final class BotonRepartos extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.RepartosView";

    /**
     * Crea el botón Repartos.
     */
    public BotonRepartos() {
        super("Repartos", TipoIcono.REPARTOS, EstiloBoton.CONTORNO, VENTANA_POR_DEFECTO);
    }
}
