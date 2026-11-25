package com.bymetoo.componentes;

/**
 * Botón "Resumen" de Bymetoo.
 * <p>
 * Al pulsarlo abre el resumen de gastos (vista.ResumenView) y cierra la ventana en la que estás.
 * Lleva el icono RESUMEN y el estilo CONTORNO.
 * </p>
 *
 * <pre>{@code
 * BotonResumen boton = new BotonResumen();
 * panelMenu.add(boton);
 * }</pre>
 *
 * Es final porque BotonBymetoo es sealed y solo deja heredar a estos botones.
 *
 * @author Equipo Bymetoo
 */
public final class BotonResumen extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.ResumenView";

    /**
     * Crea el botón Resumen.
     */
    public BotonResumen() {
        super("Resumen", TipoIcono.RESUMEN, EstiloBoton.CONTORNO, VENTANA_POR_DEFECTO);
    }
}
