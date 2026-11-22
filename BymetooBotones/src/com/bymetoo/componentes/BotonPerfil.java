package com.bymetoo.componentes;

/**
 * Botón "Perfil" de Bymetoo.
 * <p>
 * Al pulsarlo abre el perfil del usuario (vista.UserView) y cierra la ventana en la que estás.
 * Lleva el icono PERFIL y el estilo MENU.
 * </p>
 *
 * <pre>{@code
 * BotonPerfil boton = new BotonPerfil();
 * panelMenu.add(boton);
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public class BotonPerfil extends BotonBymetoo {

    private static final long serialVersionUID = 1L;

    /** Ventana a la que va el botón por defecto. */
    public static final String VENTANA_POR_DEFECTO = "vista.UserView";

    /**
     * Crea el botón Perfil.
     */
    public BotonPerfil() {
        super("Perfil", TipoIcono.PERFIL, EstiloBoton.MENU, VENTANA_POR_DEFECTO);
    }
}
