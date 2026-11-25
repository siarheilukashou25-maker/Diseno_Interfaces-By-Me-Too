package com.bymetoo.componentes;

import javax.swing.JFrame;

/** Ventana sin constructor público sin parámetros, para el caso N-08. */
public class SinConstructorVacio extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Único constructor, con un parámetro.
     *
     * @param titulo el título
     */
    public SinConstructorVacio(String titulo) {
        super(titulo);
    }
}
