package com.bymetoo.componentes;

import javax.swing.JFrame;

/**
 * Ventana falsa para las pruebas de navegación. Tiene que ser pública y con
 * constructor sin parámetros, igual que las vistas reales de la app.
 */
public class VentanaPruebaA extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Crea la ventana de prueba A. */
    public VentanaPruebaA() {
        super("Ventana de prueba A");
        setSize(300, 120);
    }
}
