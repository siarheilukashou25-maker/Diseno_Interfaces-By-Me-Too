package com.bymetoo.componentes;

/**
 * Botón "Gastos" de Bymetoo.
 * <p>
 * Al pulsarlo abre la pantalla de los gastos (vista.MainView) y cierra la ventana en la que estás.
 * Lleva el icono GASTOS y el estilo CONTORNO_SUAVE.
 * </p>
 *
 * <pre>{@code
 * BotonGastos boton = new BotonGastos();
 * panelMenu.add(boton);
 * }</pre>
 *
 * Es final porque BotonBymetoo es sealed y solo deja heredar a estos botones.
 *
 * @author Equipo Bymetoo
 */
public final class BotonGastos extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.MainView";

    /**
     * Crea el botón Gastos.
     */
    public BotonGastos() {
        super("Gastos", TipoIcono.GASTOS, EstiloBoton.CONTORNO_SUAVE, VENTANA_POR_DEFECTO);
    }
}
