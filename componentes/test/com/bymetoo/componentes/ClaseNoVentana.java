package com.bymetoo.componentes;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clases de prueba que <b>no</b> son ventanas (DI-3.5.6). Cuentan cuántas
 * veces se inicializan (bloque static) y cuántas se crean (constructor), para
 * comprobar que el botón no ejecuta código de clases que no son ventanas.
 * <p>
 * Los contadores están en esta clase y no en las clases contadas, para que
 * leerlos desde el test no las inicialice.
 * </p>
 */
public final class ClaseNoVentana {

    /** Veces que se ha llamado al constructor de {@link ConConstructor}. */
    static final AtomicInteger CREADA = new AtomicInteger();
    /** Veces que se ha ejecutado el bloque static de {@link ConStatic}. */
    static final AtomicInteger INICIALIZADA = new AtomicInteger();

    private ClaseNoVentana() {
    }

    /** No es una ventana; cuenta las veces que se crea. */
    public static class ConConstructor {
        /** Constructor público sin parámetros. */
        public ConConstructor() {
            CREADA.incrementAndGet();
        }
    }

    /** No es una ventana; cuenta las veces que se inicializa la clase. */
    public static class ConStatic {
        static {
            INICIALIZADA.incrementAndGet();
        }

        /** Constructor público sin parámetros. */
        public ConStatic() {
        }
    }
}
