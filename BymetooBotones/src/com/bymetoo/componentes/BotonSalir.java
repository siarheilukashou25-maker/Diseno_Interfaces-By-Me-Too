package com.bymetoo.componentes;

/**
 * Botón "Salir" de Bymetoo.
 * <p>
 * Al pulsarlo abre el login (para cerrar sesión) (vista.LoginView) y cierra la ventana en la que estás.
 * Lleva el icono SALIR y el estilo CONTORNO.
 * </p>
 *
 * <pre>{@code
 * BotonSalir boton = new BotonSalir();
 * panelMenu.add(boton);
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public class BotonSalir extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.LoginView";

    /**
     * Crea el botón Salir.
     */
    public BotonSalir() {
        super("Salir", TipoIcono.SALIR, EstiloBoton.CONTORNO, VENTANA_POR_DEFECTO);
    }
}
