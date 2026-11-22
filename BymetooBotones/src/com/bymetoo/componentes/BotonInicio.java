package com.bymetoo.componentes;

/**
 * Botón "Inicio" de Bymetoo.
 * <p>
 * Al pulsarlo abre la pantalla principal (vista.MainView) y cierra la ventana en la que estás.
 * Lleva el icono USUARIOS y el estilo MENU.
 * </p>
 *
 * <pre>{@code
 * BotonInicio boton = new BotonInicio();
 * panelMenu.add(boton);
 * }</pre>
 *
 * Es final porque BotonBymetoo es sealed y solo deja heredar a estos botones.
 *
 * @author Equipo Bymetoo
 */
public final class BotonInicio extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.MainView";

    /**
     * Crea el botón Inicio.
     */
    public BotonInicio() {
        super("Inicio", TipoIcono.USUARIOS, EstiloBoton.MENU, VENTANA_POR_DEFECTO);
    }
}
