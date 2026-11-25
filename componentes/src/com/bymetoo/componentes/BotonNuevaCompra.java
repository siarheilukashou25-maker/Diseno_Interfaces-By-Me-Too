package com.bymetoo.componentes;

/**
 * Botón "Nueva compra" de Bymetoo.
 * <p>
 * Al pulsarlo abre la pantalla para añadir una compra (vista.CompraWindow) y cierra la ventana en la que estás.
 * Lleva el icono NUEVA_COMPRA y el estilo CONTORNO.
 * </p>
 *
 * <pre>{@code
 * BotonNuevaCompra boton = new BotonNuevaCompra();
 * panelMenu.add(boton);
 * }</pre>
 *
 * Es final porque BotonBymetoo es sealed y solo deja heredar a estos botones.
 *
 * @author Equipo Bymetoo
 */
public final class BotonNuevaCompra extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.CompraWindow";

    /**
     * Crea el botón Nueva compra.
     */
    public BotonNuevaCompra() {
        super("Nueva compra", TipoIcono.NUEVA_COMPRA, EstiloBoton.CONTORNO, VENTANA_POR_DEFECTO);
    }
}
