package com.bymetoo.componentes;

import javax.swing.JFrame;

/**
 * Ventana falsa para las pruebas de navegación. Tiene que ser pública y con
 * constructor sin parámetros, igual que las vistas reales de la app.
 */
public class VentanaPruebaB extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Crea la ventana de prueba B. */
    public VentanaPruebaB() {
        super("Ventana de prueba B");
        setSize(300, 120);
    }
}
